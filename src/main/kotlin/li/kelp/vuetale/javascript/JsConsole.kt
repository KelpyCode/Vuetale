package li.kelp.vuetale.javascript

import java.util.logging.Logger

/**
 * Kotlin-side implementation of the JS `console` object.
 *
 * Javet does not provide a built-in console; we register an instance of this
 * class as `ktConsole` and then define `globalThis.console` via a small JS
 * shim that converts all arguments to strings before forwarding the call.
 */
class JsConsole {
    private val logger: Logger = Logger.getLogger("JS")

    fun log(msg: String)   = logger.info(msg)
    fun info(msg: String)  = logger.info(msg)
    fun warn(msg: String)  = logger.warning(msg)
    fun error(msg: String) = logger.severe(msg)
    fun debug(msg: String) = logger.fine(msg)
}

