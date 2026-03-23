package li.kelp.vuetale

import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.app.AppType
import li.kelp.vuetale.javascript.JSEngine


fun main() {
    println("Vuetale test")

//    val jsEngine = JSEngine()

//    jsEngine.createUserApp("test-app")

    val app = AppManager.createApp("test", AppType.Page)

    app.mount()

    println("Done")

    // Keep alive
    while (true) {
        Thread.sleep(1000)
    }
}
