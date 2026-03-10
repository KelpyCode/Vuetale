package li.kelp.vuetale

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit

class Plugin(init: JavaPluginInit) : JavaPlugin(init) {
    override fun setup() {
        logger.atInfo().log("Hello World!")
    }
}