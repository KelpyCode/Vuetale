package li.kelp.vuetale

import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.app.AppType


fun main() {
    println("Vuetale test")

    val app = AppManager.createApp("test", AppType.Page)
    // JSEngine.instance is initialised lazily inside AppManager/App above;
    // capture it here via the companion object so we can close it on exit.
    val engine = li.kelp.vuetale.javascript.JSEngine.instance

    app.mount()

    println("Done – Node.js event loop is running automatically on 'vuetale-v8' thread")

    // Keep the JVM alive; the V8 event-loop is pumped by JSEngine's internal scheduler.
    try {
        while (true) {
            Thread.sleep(1000)
        }
    } finally {
        engine.close()
    }
}
