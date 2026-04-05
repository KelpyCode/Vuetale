package li.kelp.vuetale.app

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

    fun getId(): String {
        return AppManager.getAppId(owner, type)
    }

    private fun createApp() = getEngine().evalScript("_vt.createUserApp('${getId()}');")
    private fun updateReference() = getEngine().loaderCtx.getMember("registerUserAppRef").execute(getId(), this)

    private fun getDependencyKey(origin: String): String {
        // Random chars
        var random = (0..5).map { ('a'..'z').random() }.joinToString("")

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

        getEngine().evalScript("_vt.getUserApp('${getId()}').mount(_vt.getUserAppRef('${getId()}').getRoot());")
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
    }

    init {
        root.app = this

        createApp()
        updateReference()
    }
}