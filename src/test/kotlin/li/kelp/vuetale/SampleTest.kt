package li.kelp.vuetale

import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.V8ValueUndefined
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
        // loader.js is initialized by JSEngine startup; create app via _vt.
        jsEngine.runOnV8Thread {
            jsEngine.loaderCtx.invoke<V8Value>("createUserApp", "test-app").close()
        }

        // Verify getUserApp() returns a real value instead of undefined.
        val exists = jsEngine.runOnV8Thread {
            val value = jsEngine.loaderCtx.invoke<V8Value>("getUserApp", "test-app")
            try {
                value !is V8ValueUndefined
            } finally {
                value.close()
            }
        }
        logger.info("USER_APPS has 'test-app': $exists")

        assertTrue(exists, "Expected _vt.getUserApp('test-app') to return a value after createUserApp()")
    }
}
