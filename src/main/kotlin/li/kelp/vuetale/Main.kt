package li.kelp.vuetale

import li.kelp.vuetale.javascript.JSEngine


fun main() {
    println("Vuetale test")

    val jsEngine = JSEngine()

    val loader = jsEngine.evalModuleResource("loader.js")
    jsEngine.createUserApp("test-app")



    println("Done")
}
