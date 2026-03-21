package li.kelp.vuetale

import li.kelp.vuetale.javascript.JSEngine
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import java.util.logging.Logger
import kotlin.test.assertTrue

class SampleTest {
    companion object {
        val jsEngine = JSEngine()
        val logger = Logger.getLogger("SampleTest")

        @JvmStatic
        @AfterAll
        fun teardown() {
            jsEngine.close()
        }
    }

    @Test
    fun loaderCreatesApp() {
        // Load loader.js from vuetale/core/ — this also loads renderer.js and the App component
        // The App component's setup() calls console.log("WORKS!") when the app is created.
        jsEngine.evalModuleResource("loader.js")

        // Trigger app creation — this should print "WORKS!" to stdout via console.log
        jsEngine.createUserApp("test-app")

        // Verify the USER_APPS global map was populated
        val res = jsEngine.evalScript("typeof USER_APPS !== 'undefined' && USER_APPS.has('test-app')")
        val exists = res.asBoolean()
        logger.info("USER_APPS has 'test-app': $exists")

        assertTrue(exists, "Expected USER_APPS to contain 'test-app' after createUserApp()")
    }
}
