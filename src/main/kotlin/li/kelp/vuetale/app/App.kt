package li.kelp.vuetale.app

import com.caoccao.javet.values.V8Value
import li.kelp.vuetale.events.EventRegistry
import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.RootElement
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger

class App(val owner: String, val type: AppType, var componentPath: String? = null) {
    private val logger: Logger = Logger.getLogger("App $owner-$type")
    private fun getEngine() = JSEngine.instance

    /** Persisted copy of every setData call – re-pushed to V8 after a hot-reload. */
    private val dataCache: MutableMap<String, Any?> = LinkedHashMap()

    var isMounted = false
        private set

    /**
     * The current page/hud object that "owns" this app – set when a
     * [li.kelp.vuetale.hytale.VuetaleUIPage] or similar takes over the app.
     * Used by [onDismiss] to prevent a stale page from destroying an app that
     * has already been claimed by a newer page instance.
     */
    @Volatile
    var currentOwner: Any? = null

    /**
     * True when [unmount] has been called and the JS-side Vue app instance has been torn
     * down.  Vue apps cannot be remounted after [app.unmount()], so [mount] must call
     * [createUserApp] again to get a fresh Vue instance before calling [app.mount()].
     * Set by [unmount], cleared by [createApp] (which calls createUserApp in JS).
     */
    @Volatile
    private var needsJsRecreation = false

    var root: RootElement = RootElement()

    @Volatile
    private var destroyInFlight: CompletableFuture<Unit>? = null

    /** Holds all live Vue→Hytale event bindings registered for this app's elements. */
    val eventRegistry = EventRegistry(this)

    /**
     * Set to `true` by [markDirty] when any element mutation (insert / remove / patchProp)
     * occurs after the initial mount.  [JSEngine]'s tick loop resets this flag and fires
     * [onDirty] once per event-loop tick, after the full Vue render batch has completed.
     */
    @Volatile
    var isDirty = false

    /**
     * Called by [JSEngine]'s tick when [isDirty] is true.
     *
     * **Threading note:** this callback is invoked on the `vuetale-v8` daemon thread.
     * If your Hytale page calls `sendUpdate()` here, wrap it with `world.execute { … }` (or
     * the equivalent server-thread dispatcher) to satisfy Hytale's thread-safety requirements.
     */
    @Volatile
    var onDirty: (() -> Unit)? = null

    /**
     * Set to true by VueBridge.patchProp when a property is *removed* (set to null/undefined).
     * Removal cannot be expressed as a targeted `set` command, so a full clear + re-render is
     * required.  Element-level insert/remove is tracked separately via [removedElementSelectors]
     * and [insertedElements] so those cases can use targeted commands instead.
     */
    var hasStructuralChanges: Boolean = false

    /** Selectors (e.g. `"#vtabc123"`) of elements removed during the last Vue render batch. */
    val removedElementSelectors: MutableList<String> = mutableListOf()

    /** Elements inserted during the last Vue render batch, paired with their parent selector. */
    data class InsertedElement(val child: Element, val parentSelector: String)

    val insertedElements: MutableList<InsertedElement> = mutableListOf()

    /**
     * Raw element IDs (no `#`) whose Hytale properties were patched in the last Vue batch.
     * Used by VuetaleUIPage.onDirty to emit targeted `set` commands instead of a full
     * clear + appendInline when no structural changes occurred.
     */
    val dirtyElementIds: MutableSet<String> = mutableSetOf()

    /* Signal that the element tree has changed.  The actual [onDirty] notification is
     * deferred to the next [JSEngine] tick so that Vue's entire render batch is applied
     * before a new UI frame is sent to the client.
     */
    fun markDirty() {
        isDirty = true
        // onDirty is NOT called here – JSEngine.tickInternal() fires it post-batch.
    }

    fun getId(): String {
        return AppManager.getAppId(owner, type)
    }

    private fun createApp() {
        val resolvedPath = componentPath?.removePrefix("vt:")
        if (resolvedPath != null) {
            componentPath = resolvedPath
            logger.info("Creating app '${getId()}' with component: $resolvedPath")
            try {
                getEngine().preloadComponent(resolvedPath)
            } catch (e: Exception) {
                val chain = generateSequence(e as Throwable) { it.cause }
                    .joinToString(" → ") { "${it.javaClass.simpleName}: ${it.message}" }
                logger.warning("Failed to preload component '$resolvedPath': $chain")
                throw e
            }
        } else {
            logger.warning("Creating app '${getId()}' with NO component path – navigateTo must be called before anything renders")
        }
        val engine = getEngine()
        engine.runOnV8Thread {
            val result = if (componentPath != null) {
                engine.loaderCtx.invoke<V8Value>("createUserApp", getId(), componentPath!!)
            } else {
                engine.loaderCtx.invoke<V8Value>("createUserApp", getId())
            }
            result.close()
        }
        needsJsRecreation = false
    }

    private fun updateReference() {
        getEngine().runOnV8Thread {
            getEngine().loaderCtx.invoke<V8Value>("registerUserAppRef", getId(), this@App).close()
        }
    }

    /**
     * Reset Kotlin-side state without calling V8 unmount.
     * Used by [li.kelp.vuetale.javascript.HotReloadManager] before tearing down the engine.
     * Does NOT touch V8 – all V8 references are considered dead at this point.
     */
    internal fun forceReset() {
        isMounted = false
        isDirty = false
        currentOwner = null
        needsJsRecreation = false
        root = RootElement().also { it.app = this }
        hasStructuralChanges = false
        removedElementSelectors.clear()
        insertedElements.clear()
        dirtyElementIds.clear()
        // V8 handles are closed on the V8 thread to avoid blocking callers.
        eventRegistry.closeAllAsync()
        // Unregister any host callbacks associated with this app to avoid memory leaks
        runCatching {
            try {
                JSEngine.instance.bridge.unregisterHostCallbacksForApp(getId())
            } catch (e: Exception) {
                logger.fine("Failed to unregister host callbacks for ${getId()}: ${e.message}")
            }
        }
    }

    /**
     * Re-create this app's Vue counterpart inside a freshly started [JSEngine].
     * Must be called after [forceReset] and after the engine has been restarted.
     */
    internal fun reinitializeInEngine() {
        createApp()
        updateReference()
        // Re-push all cached data so Vue sees the same state after a hot reload.
        if (dataCache.isNotEmpty()) {
            val engine = getEngine()
            dataCache.forEach { (key, value) ->
                // Avoid scheduling work if the engine is shutting down.
                if (!engine.isAlive) return@forEach
                try {
                    // If the cached value is a JVM function, we must register it again
                    // in the new engine to obtain a fresh hostId marker.
                    if (value != null && isJvmFunction(value)) {
                        val hostId = JSEngine.instance.bridge.registerHostCallback(getId(), value)
                        engine.runOnV8Thread {
                            engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, mapOf("_vtHostFnId" to hostId))
                                .close()
                        }
                    } else {
                        engine.runOnV8Thread {
                            engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, value).close()
                        }
                    }
                } catch (e: Exception) {
                    logger.warning("Failed to re-push cached data for app '${getId()}': ${e.message}")
                }
            }
        }
    }


    /**
     * Push a reactive data value to the Vue side for this app.
     * The value is immediately available via `useData("key")` in any component rendered
     * by this app.  Calling this method again with the same [key] updates the existing
     * reactive ref in-place, triggering Vue's reactivity system automatically.
     *
     * @param key   The string key used in `useData("key")` on the Vue side.
     * @param value Any JSON-serialisable JVM value (String, Number, Boolean, null).
     */
    fun setData(key: String, value: Any?) {
        dataCache[key] = value
        val engine = getEngine()
        if (!engine.isAlive) {
            // Engine is shutting down; skip sending to V8. Data is still cached and
            // will be re-pushed when the engine restarts.
            logger.fine("Skipping setData('$key') because JSEngine is not alive")
            return
        }
        try {
            // If value looks like a JVM function/functional object, register it and
            // send a hostFn marker to JS instead of the raw object.
            if (value != null && isJvmFunction(value)) {
                val hostId = JSEngine.instance.bridge.registerHostCallback(getId(), value)
                engine.submitToV8Thread {
                    engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, mapOf("_vtHostFnId" to hostId)).close()
                }
            } else {
                engine.submitToV8Thread {
                    engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, value).close()
                }
            }
        } catch (e: Exception) {
            // Swallow exceptions caused by engine shutdown; data remains in cache.
            logger.warning("setData failed for app '${getId()}' key='$key': ${e.message}")
        }
    }

    fun getData(key: String): V8Value? {
        val engine = getEngine()
        if (!engine.isAlive) {
            logger.fine("Skipping getData('$key') because JSEngine is not alive")
            return null
        }
        return try {
            engine.runOnV8Thread {
                engine.loaderCtx.invoke<V8Value>("getAppData", getId(), key)
            }
        } catch (e: Exception) {
            logger.warning("getData failed for app '${getId()}' key='$key': ${e.message}")
            null
        }
    }

    private fun isJvmFunction(value: Any): Boolean {
        // Accept Kotlin FunctionN and common Java functional interfaces
        val cls = value.javaClass
        if (cls.name.startsWith("kotlin.jvm.functions")) return true
        // Common single-method interfaces: Runnable, Callable, Consumer, Function
        val singleAbstract = cls.methods.count { java.lang.reflect.Modifier.isAbstract(it.modifiers) }
        // crude: check for any 'invoke' method or 'apply' etc.
        if (cls.methods.any { it.name == "invoke" || it.name == "apply" || it.name == "accept" || it.name == "call" }) return true
        return false
    }

    fun mount() {
        if (isMounted) {
            logger.warning("Tried to mount but App '${getId()}' is already mounted")
            return
        }

        val engine = getEngine()

        // Vue apps cannot be remounted after app.unmount(). If this app was previously
        // unmounted, we must call createUserApp() again to get a fresh Vue instance
        // before calling mount().  The createUserApp call is submitted to the V8 thread
        // AFTER the async unmount script (queued by unmount()), so V8 processes them in
        // the correct order: unmount → createUserApp → mount.
        if (needsJsRecreation) {
            engine.runOnV8Thread {
                val result = if (componentPath != null) {
                    engine.loaderCtx.invoke<V8Value>("createUserApp", getId(), componentPath!!)
                } else {
                    engine.loaderCtx.invoke<V8Value>("createUserApp", getId())
                }
                result.close()
            }
            // removeUserApp() clears USER_APPS_REF; recreate the JS container ref
            // before mount so Vue never receives an undefined mount target.
            updateReference()
            needsJsRecreation = false
        }

        // Set the current app ID context so any setTimeout/setInterval calls during
        // component setup (onMounted, etc.) are tagged with this app's ID for later
        // cancellation when the app is dismissed.
        engine.evalScript(
            """
            globalThis.__vt_currentAppId = '${getId()}';
            _vt.getUserApp('${getId()}').mount(_vt.getUserAppRef('${getId()}'));
            globalThis.__vt_currentAppId = null;
        """.trimIndent()
        )
        logger.info("Mounted App '${getId()}'")
        isMounted = true
    }

    /**
     * Swap the rendered component at runtime without unmounting/remounting the app.
     * Calls `_vt.navigateTo(id, path)` in JS which updates the reactive path ref.
     *
     * @param path  Module path understood by the Javet module resolver, e.g. `"vt:@core/pages/Dashboard"`.
     */
    fun navigateTo(path: String) {
        val resolvedPath = path.removePrefix("vt:")
        componentPath = resolvedPath
        val engine = getEngine()
        engine.preloadComponent(resolvedPath)
        engine.runOnV8Thread {
            engine.loaderCtx.invoke<V8Value>("navigateTo", getId(), resolvedPath).close()
        }
    }

    fun unmount() {
        if (!isMounted) {
            logger.warning("Tried to unmount but App '${getId()}' is not mounted")
            return
        }
        val appId = getId()
        val engine = getEngine()

        if (engine.isAlive) {
            engine.evalScriptAsync(
                "try { _vt.cancelTimersForApp('$appId'); } catch(e) {}" +
                        "\ntry { var __a = _vt.getUserApp('$appId'); if(__a) __a.unmount(); } catch(e) {}"
            )
        }

        isMounted = false
        onDirty = null
        isDirty = false
        needsJsRecreation = true  // Vue app can't be remounted; mount() will recreate it
        eventRegistry.closeAllAsync()
        // Also unregister any host callbacks tied to this app
        runCatching {
            try {
                JSEngine.instance.bridge.unregisterHostCallbacksForApp(getId())
            } catch (e: Exception) {
                logger.fine("Failed to unregister host callbacks for ${getId()} during unmount: ${e.message}")
            }
        }
    }

    /**
     * Non-blocking destruction path used by page/hud teardown.
     *
     * It schedules JS-side unmount + timer cancellation on the V8 thread and immediately
     * clears Kotlin-owned references so callers never wait on V8 cleanup.
     */
    fun destroyAsync(reason: String): CompletableFuture<Unit> {
        destroyInFlight?.let { existing ->
            if (!existing.isDone) return existing
        }

        val appId = getId()
        val engine = getEngine()
        val teardownFuture = CompletableFuture<Unit>()
        destroyInFlight = teardownFuture

        isMounted = false
        onDirty = null
        isDirty = false
        currentOwner = null
        needsJsRecreation = true
        hasStructuralChanges = false
        removedElementSelectors.clear()
        insertedElements.clear()
        dirtyElementIds.clear()

        eventRegistry.closeAllAsync()

        runCatching {
            try {
                JSEngine.instance.bridge.unregisterHostCallbacksForApp(appId)
            } catch (e: Exception) {
                logger.fine("Failed to unregister host callbacks for $appId during destroyAsync: ${e.message}")
            }
        }

        if (!engine.isAlive) {
            logger.fine("Skipping JS destroy for '$appId' because JSEngine is not alive")
            teardownFuture.complete(Unit)
            return teardownFuture
        }

        engine.submitToV8Thread {
            val script = """
                try { _vt.cancelTimersForApp('$appId'); } catch (e) {}
                try { var __a = _vt.getUserApp('$appId'); if (__a) __a.unmount(); } catch (e) {}
                try { _vt.removeUserApp('$appId'); } catch (e) {}
            """.trimIndent()
            runCatching { engine.evalScript(script) }
                .onSuccess {
                    teardownFuture.complete(Unit)
                }
                .onFailure {
                    logger.fine("destroyAsync JS teardown failed for '$appId' (reason=$reason): ${it.message}")
                    teardownFuture.completeExceptionally(it)
                }
        }

        return teardownFuture
    }

    init {
        root.app = this
        createApp()
        updateReference()
    }
}