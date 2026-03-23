package li.kelp.vuetale.app

import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.tree.RootElement
import java.util.logging.Logger

class App(val owner: String, val type: AppType) {
    private val logger: Logger = Logger.getLogger("App $owner-$type")
    private fun getEngine() = JSEngine.instance

    var isMounted = false
        private set

    var root: RootElement = RootElement()

    fun getId(): String {
        return AppManager.getAppId(owner, type)
    }

    private fun createApp() = getEngine().evalScript("_vt.createUserApp('${getId()}');")
    private fun updateReference() = getEngine().loaderCtx.getMember("registerUserAppRef").execute(getId(), this)

    fun mount() {
        if(isMounted) {
            logger.warning("Tried to mount but App '${getId()}' is already mounted")
            return
        }

        getEngine().evalScript("_vt.getUserApp('${getId()}').mount(_vt.getUserAppRef('${getId()}').getRoot());")
        isMounted = true
    }
    fun unmount() {
        if(!isMounted) {
            logger.warning("Tried to unmount but App '${getId()}' is not mounted")
            return
        }

        getEngine().evalScript("_vt.getUserApp('${getId()}').unmount();")
        isMounted = false
    }

    init {
        createApp()
        updateReference()
    }
}