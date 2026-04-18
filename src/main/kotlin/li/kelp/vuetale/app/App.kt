package li.kelp.vuetale.app

import com.caoccao.javet.values.V8Value
import li.kelp.vuetale.events.EventRegistry
import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.RootElement
import java.util.logging.Logger

data class Dependency(var origin: String, var name: String, var dependents: Int)

class App(val owner: String, val type: AppType, var componentPath: String? = null) {
    private val logger: Logger = Logger.getLogger("App $owner-$type")
    private fun getEngine() = JSEngine.instance

    val dependencies: MutableMap<String, Dependency> = mutableMapOf()

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
        if (componentPath != null) {
            logger.info("Creating app '${getId()}' with component: $componentPath")
            getEngine().preloadComponent(componentPath!!)
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
    }

    /**
     * Re-create this app's Vue counterpart inside a freshly started [JSEngine].
     * Must be called after [forceReset] and after the engine has been restarted.
     */
    internal fun reinitializeInEngine() {
        createApp()
        updateReference()
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

    fun mount() {
        if (isMounted) {
            logger.warning("Tried to mount but App '${getId()}' is already mounted")
            return
        }
        getEngine().evalScript("_vt.getUserApp('${getId()}').mount(_vt.getUserAppRef('${getId()}'));")
        logger.info("Mounted App '${getId()}'")
        isMounted = true
    }

    /**
     * Swap the rendered component at runtime without unmounting/remounting the app.
     * Calls `_vt.navigateTo(id, path)` in JS which updates the reactive path ref.
     *
     * @param path  Module path understood by the Javet module resolver, e.g. `"@core/pages/Dashboard"`.
     */
    fun navigateTo(path: String) {
        componentPath = path
        val engine = getEngine()
        engine.preloadComponent(path)
        engine.runOnV8Thread {
            engine.loaderCtx.invoke<V8Value>("navigateTo", getId(), path).close()
        }
    }

    fun unmount() {
        if (!isMounted) {
            logger.warning("Tried to unmount but App '${getId()}' is not mounted")
            return
        }
        getEngine().evalScript("_vt.getUserApp('${getId()}').unmount();")
        isMounted = false
        eventRegistry.closeAll()
    }

    init {
        root.app = this
        createApp()
        updateReference()
    }
}