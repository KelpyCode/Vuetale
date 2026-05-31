
package li.kelp.vuetale.app

import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.reference.V8ValueObject
import li.kelp.vuetale.events.EventRegistry
import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.RootElement
import java.lang.reflect.Array as JArray
import java.lang.reflect.Modifier
import java.util.IdentityHashMap
import java.util.logging.Logger

data class Dependency(var origin: String, var name: String, var dependents: Int)

class App(val owner: String, val type: AppType, var componentPath: String? = null) {

    companion object {
        private const val MAX_SETDATA_NORMALIZE_DEPTH = 8
    }

    private val logger: Logger = Logger.getLogger("App $owner-$type")
    private fun getEngine() = JSEngine.instance

    val dependencies: MutableMap<String, Dependency> = mutableMapOf()

    /** Persisted copy of every setData call – re-pushed to V8 after a hot-reload. */
    private val dataCache: MutableMap<String, Any?> = LinkedHashMap()

    var isMounted = false
        private set

    var root: RootElement = RootElement()

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
        root = RootElement().also { it.app = this }
        hasStructuralChanges = false
        removedElementSelectors.clear()
        insertedElements.clear()
        dirtyElementIds.clear()
        // closeAll() wraps each V8 callback.close() in runCatching internally,
        // so this is safe even when V8 is already torn down.
        eventRegistry.closeAll()
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
                        val normalized = normalizeForJs(value)
                        engine.runOnV8Thread {
                            engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, normalized).close()
                        }
                    }
                } catch (e: Exception) {
                    logger.warning("Failed to re-push cached data for app '${getId()}': ${e.message}")
                }
            }
        }
    }

    private fun getDependencyKey(origin: String): String {
        val random = (0..5).map { ('a'..'z').random() }.joinToString("")
        return origin.replace(Regex("[^A-Za-z0-9]"), "") + "VT" + random
    }

    fun addDependency(origin: String, asName: String? = null) {
        if (dependencies.get(origin) == null) {
            val key = asName ?: getDependencyKey(origin)
            dependencies[origin] = Dependency(origin, key, 0)
        }
        val dep = dependencies[origin]
        dep!!.dependents++
    }

    fun removeDependency(origin: String) {
        val dep = dependencies[origin] ?: return
        dep.dependents--
        if (dep.dependents <= 0) {
            dependencies.remove(origin)
        }
    }

    fun getDependencyName(origin: String) = dependencies[origin]?.name

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
            logger.fine("Skipping setData('$key') because JSEngine is not alive")
            return
        }
        val normalized = normalizeForJs(value)
        // Fire-and-forget: setData is often called from latency-sensitive game threads.
        // Waiting on V8 here causes visible freezes when payloads are large (e.g. non-empty arrays)
        // or when teardown/render work is in progress.
        engine.submitToV8Thread {
            try {
                if (value != null && isJvmFunction(value)) {
                    val hostId = JSEngine.instance.bridge.registerHostCallback(getId(), value)
                    engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, mapOf("_vtHostFnId" to hostId)).close()
                } else {
                    engine.loaderCtx.invoke<V8Value>("setAppData", getId(), key, normalized).close()
                }
            } catch (e: Exception) {
                logger.warning("setData failed for app '${getId()}' key='$key' (queued to V8): ${e.message}")
            }
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

    private fun normalizeForJs(value: Any?): Any? {
        return normalizeForJsInternal(value, 0, IdentityHashMap())
    }

    private fun normalizeForJsInternal(value: Any?, depth: Int, seen: IdentityHashMap<Any, Boolean>): Any? {
        if (value == null) return null
        if (depth > MAX_SETDATA_NORMALIZE_DEPTH) {
            return value.toString()
        }
        if (isJvmFunction(value)) return value

        return when (value) {
            is String, is Number, is Boolean -> value
            is Char -> value.toString()
            is Enum<*> -> value.name
            is Map<*, *> -> {
                val out = LinkedHashMap<String, Any?>()
                value.forEach { (k, v) ->
                    out[k?.toString() ?: "null"] = normalizeForJsInternal(v, depth + 1, seen)
                }
                out
            }

            is Iterable<*> -> value.map { normalizeForJsInternal(it, depth + 1, seen) }
            else -> {
                val cls = value.javaClass
                if (cls.isArray) {
                    val len = JArray.getLength(value)
                    val out = ArrayList<Any?>(len)
                    for (i in 0 until len) {
                        out.add(normalizeForJsInternal(JArray.get(value, i), depth + 1, seen))
                    }
                    return out
                }

                if (isSimpleJdkType(cls)) {
                    return value.toString()
                }

                if (seen.put(value, true) != null) {
                    return null
                }
                try {
                    val out = LinkedHashMap<String, Any?>()
                    var c: Class<*>? = cls
                    while (c != null && c != Any::class.java) {
                        c.declaredFields.forEach { f ->
                            if (Modifier.isStatic(f.modifiers)) return@forEach
                            if (f.isSynthetic) return@forEach
                            if (f.name.startsWith("$")) return@forEach
                            runCatching {
                                f.isAccessible = true
                                out[f.name] = normalizeForJsInternal(f.get(value), depth + 1, seen)
                            }
                        }
                        c = c.superclass
                    }
                    out
                } finally {
                    seen.remove(value)
                }
            }
        }
    }

    private fun isSimpleJdkType(cls: Class<*>): Boolean {
        val name = cls.name
        return name.startsWith("java.time.") ||
            name == "java.util.UUID" ||
            name.startsWith("java.math.")
    }

    fun mount() {
        if (isMounted) {
            logger.warning("Tried to mount but App '${getId()}' is already mounted")
            return
        }
        // Set the current app ID context so any setTimeout/setInterval calls during
        // component setup (onMounted, etc.) are tagged with this app's ID for later
        // cancellation when the app is dismissed.
        getEngine().evalScript("""
            globalThis.__vt_currentAppId = '${getId()}';
            _vt.getUserApp('${getId()}').mount(_vt.getUserAppRef('${getId()}'));
            globalThis.__vt_currentAppId = null;
        """.trimIndent())
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

        // Use fire-and-forget (non-blocking) script dispatch so that the calling thread
        // (typically the world tick thread) is never blocked waiting for the V8 thread.
        // Blocking here caused InterruptedException when the server shut down or
        // programmatic closePage() was called from a tick handler.
        if (engine.isAlive) {
            engine.evalScriptAsync(
                "try { _vt.cancelTimersForApp('$appId'); } catch(e) {}" +
                "\ntry { var __a = _vt.getUserApp('$appId'); if(__a) __a.unmount(); } catch(e) {}"
            )
        }

        isMounted = false
        eventRegistry.closeAll()
        // Also unregister any host callbacks tied to this app
        runCatching {
            try {
                JSEngine.instance.bridge.unregisterHostCallbacksForApp(getId())
            } catch (e: Exception) {
                logger.fine("Failed to unregister host callbacks for ${getId()} during unmount: ${e.message}")
            }
        }
    }

    init {
        root.app = this
        createApp()
        updateReference()
    }
}