package li.kelp.vuetale.app

import com.caoccao.javet.values.V8Value
import li.kelp.vuetale.events.EventRegistry
import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.tree.RootElement
import java.util.logging.Logger

data class Dependency(var origin: String, var name: String, var dependents: Int)

class App(val owner: String, val type: AppType) {
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
     * Signal that the element tree has changed.  The actual [onDirty] notification is
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

    private fun createApp() = getEngine().evalScript("_vt.createUserApp('${getId()}');")
    private fun updateReference() {
        getEngine().runOnV8Thread {
            getEngine().loaderCtx.invoke<V8Value>("registerUserAppRef", getId(), this@App).close()
        }
    }

    private fun getDependencyKey(origin: String): String {
        val random = (0..5).map { ('a'..'z').random() }.joinToString("")
        return origin.replace(Regex("[^A-Za-z0-9]"), "") + "_" + random
    }

    fun addDependency(origin: String) {
        if (dependencies.get(origin) == null) {
            val key = getDependencyKey(origin)
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