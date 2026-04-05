package li.kelp.vuetale

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.app.AppType

class Plugin(init: JavaPluginInit) : JavaPlugin(init) {
    override fun setup() {
        logger.atInfo().log("Hello World!")

        val app = AppManager.createApp("test", AppType.Page)

        app.mount()
    }
}