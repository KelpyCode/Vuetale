package li.kelp.vuetale

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.app.AppType
import li.kelp.vuetale.commands.TestCommand
import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.javascript.ModuleRegistry

class Plugin(init: JavaPluginInit) : JavaPlugin(init) {

    /** Captured during setup so shutdown can close without re-initializing. */
    private var jsEngine: JSEngine? = null

    override fun setup() {
        logger.atInfo().log("Hello World!")
        //val app = AppManager.createApp("test", AppType.Page)
        // JSEngine.instance is initialised lazily the first time App accesses it.
        // Register this JAR's resources under the 'vt:@core' alias so other mods
        // can import from 'vt:@core/components/...' etc.
        ModuleRegistry.registerModule("core", Plugin::class.java)

        // Capture it here so we can close it on shutdown.
        //jsEngine = JSEngine.instance

        //app.mount()

        this.getCommandRegistry().registerCommand(TestCommand())
    }

    override fun shutdown() {
        // Cleanly stop the Node.js event-loop pump and release V8 resources.
        jsEngine?.close()
        jsEngine = null
    }
}