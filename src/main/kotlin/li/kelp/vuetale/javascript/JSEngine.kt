package li.kelp.vuetale.javascript

import com.caoccao.javet.enums.V8AwaitMode
import com.caoccao.javet.exceptions.JavetException
import com.caoccao.javet.interop.V8Host
import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.interop.callback.IV8ModuleResolver
import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.reference.IV8Module
import com.caoccao.javet.values.reference.V8ValueObject
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * Core JavaScript engine wrapper built on Javet/Node.js.
 *
 * Responsibilities:
 *  - Own a single [V8Runtime] (backed by Node.js) for the lifetime of the mod.
 *  - Confine ALL V8 access to a dedicated daemon thread ("vuetale-v8") so the
 *    Node.js runtime is never touched from multiple threads simultaneously.
 *  - Expose a classpath-aware ES-module resolver so every `import` in
 *    bundled JS files is satisfied from JAR resources.
 *  - Boot the Vue IIFE + the loader.js entry-point.
 *  - Bridge Kotlin host objects to JavaScript via [JavetProxyConverter].
 *  - Automatically pump the Node.js event loop at ~50 ms intervals so that
 *    setTimeout / setInterval / Promises fire without any external tick call.
 */
class JSEngine : AutoCloseable {

    companion object {
        val instance: JSEngine by lazy { JSEngine() }
        private val logger: Logger = Logger.getLogger("JSEngine")

        private val DOM_MOCK = """
            if (typeof document === 'undefined') {
                globalThis.document = {
                    querySelector: function(sel) {
                        return { tag: 'div', selector: sel, children: null };
                    },
                    createElement: function(tag) {
                        return { tag: tag, children: null };
                    }
                };
            }
        """.trimIndent()
    }

    // ── Dedicated V8 thread ────────────────────────────────────────────────

    /**
     * Single-threaded executor that is the sole owner of the V8/Node.js runtime.
     * Every V8 API call – including the periodic event-loop pump – runs here.
     * The thread inherits the context class-loader of the thread that first
     * instantiated JSEngine (typically the Hytale server thread), ensuring JAR
     * resources are reachable.
     */
    private val v8Executor: ScheduledExecutorService = run {
        val ctxLoader = Thread.currentThread().contextClassLoader
        Executors.newSingleThreadScheduledExecutor { r ->
            Thread(r, "vuetale-v8").also {
                it.isDaemon = true
                it.contextClassLoader = ctxLoader
            }
        }
    }

    // ── V8 state (only ever accessed from v8Executor thread) ──────────────

    /** Converter that wraps/unwraps Kotlin objects as V8 Proxies. */
    val converter: JavetProxyConverter = JavetProxyConverter()

    private lateinit var v8Runtime: V8Runtime

    /** VueBridge instance shared with the JS side via globalThis.ktBridge. */
    lateinit var bridge: VueBridge
        private set

    /** Cache of already-compiled ES modules keyed by their resolved classpath path. */
    private val moduleCache: MutableMap<String, IV8Module> = mutableMapOf()

    /**
     * The _vt global object set by loader.js – holds createUserApp,
     * getUserApp, registerUserAppRef, etc.
     */
    lateinit var loaderCtx: V8ValueObject
        private set

    /** Future for the repeating tick task – kept so we can cancel it on close(). */
    private val tickFuture: ScheduledFuture<*>

    // ── Lifecycle ──────────────────────────────────────────────────────────

    init {
        // 1. Initialize the entire V8 stack on the dedicated thread.
        //    Blocks the calling thread until init is complete.
        v8Executor.submit(::initOnV8Thread).get()

        // 2. Start the Node.js event-loop pump: every 50 ms (~20 Hz).
        tickFuture = v8Executor.scheduleAtFixedRate(::tickInternal, 50, 50, TimeUnit.MILLISECONDS)

        logger.info("JSEngine initialized – Node.js event loop running at ~20 Hz on thread 'vuetale-v8'")
    }

    /** All V8 object creation and script execution happens here, on the V8 thread. */
    private fun initOnV8Thread() {
        v8Runtime = V8Host.getNodeInstance().createV8Runtime()
        v8Runtime.setConverter(converter)
        v8Runtime.setV8ModuleResolver(ClasspathModuleResolver())

        bridge = VueBridge(v8Runtime, converter)

        // Wire up console -> JVM logger.
        v8Runtime.globalObject.set("ktConsole", JsConsole())
        v8Runtime.getExecutor("""
            globalThis.console = {
                log:   (...a) => ktConsole.log  (a.map(x => String(x)).join(' ')),
                info:  (...a) => ktConsole.info (a.map(x => String(x)).join(' ')),
                warn:  (...a) => ktConsole.warn (a.map(x => String(x)).join(' ')),
                error: (...a) => ktConsole.error(a.map(x => String(x)).join(' ')),
                debug: (...a) => ktConsole.debug(a.map(x => String(x)).join(' ')),
            };
        """.trimIndent()).executeVoid()

        v8Runtime.globalObject.set("ktBridge", bridge)

        // Load Vue as a plain IIFE script -> sets globalThis.Vue
        val vueIife = loadClasspathText("vue.js")
            ?: error("vue.js not found on classpath")
        v8Runtime.getExecutor(vueIife).executeVoid()
        v8Runtime.getExecutor("if (typeof Vue !== 'undefined') globalThis.Vue = Vue;").executeVoid()

        // Minimal DOM stub (Vue needs document.querySelector for mount).
        v8Runtime.getExecutor(DOM_MOCK).executeVoid()

        // Load loader.js as an ES module; it sets globalThis._vt.
        compileAndEvalModule("vuetale/core/loader.js")

        loaderCtx = v8Runtime.globalObject.get("_vt")
            ?: error("globalThis._vt was not set after loading loader.js")
    }

    /** Called repeatedly by the scheduler on the V8 thread. Pumps the Node.js event loop. */
    private fun tickInternal() {
        runCatching { v8Runtime.await(V8AwaitMode.RunOnce) }
            .onFailure { logger.warning("V8 tick error: ${it.message}") }
    }

    // ── Public API (thread-safe – dispatches to V8 thread) ────────────────

    /**
     * Submit [block] to the V8 thread, block the caller until it completes,
     * and return its result.  Use this whenever you need to call a raw V8 API
     * from outside the V8 thread (e.g. accessing [loaderCtx] directly).
     */
    fun <T> runOnV8Thread(block: () -> T): T =
        v8Executor.submit(Callable { block() }).get()

    /**
     * Evaluate a plain (non-module) JavaScript snippet on the V8 thread.
     * Microtasks (Promises, Vue scheduler) are flushed immediately after.
     */
    fun evalScript(script: String) {
        v8Executor.submit {
            v8Runtime.getExecutor(script).executeVoid()
            runCatching { v8Runtime.await(V8AwaitMode.RunNoWait) }
        }.get()
    }

    // ── Module resolver (runs on V8 thread during import resolution) ───────

    private inner class ClasspathModuleResolver : IV8ModuleResolver {
        @Throws(JavetException::class)
        override fun resolve(
            v8Runtime: V8Runtime,
            resourceName: String,
            v8ModuleReferrer: IV8Module?
        ): IV8Module {
            val resolvedPath = resolveModulePath(resourceName, v8ModuleReferrer?.resourceName)
            return moduleCache.getOrPut(resolvedPath) {
                val source = loadClasspathText(resolvedPath)
                    ?: error("ES module not found: $resolvedPath (imported as '$resourceName')")
                v8Runtime.getExecutor(source)
                    .setResourceName(resolvedPath)
                    .compileV8Module()
            }
        }
    }

    private fun resolveModulePath(resourceName: String, referrerPath: String?): String {
        if (!resourceName.startsWith(".") && !resourceName.startsWith("/")) {
            val withExt = if (resourceName.endsWith(".js")) resourceName else "$resourceName.js"
            return "vuetale/core/$withExt"
        }
        if (referrerPath != null && resourceName.startsWith(".")) {
            val base = referrerPath.substringBeforeLast("/")
            return normalizePath("$base/$resourceName")
        }
        return resourceName.trimStart('/')
    }

    private fun normalizePath(raw: String): String {
        val stack = mutableListOf<String>()
        for (part in raw.split("/")) {
            when (part) {
                "", "."  -> {}
                ".."     -> if (stack.isNotEmpty()) stack.removeLast()
                else     -> stack.add(part)
            }
        }
        return stack.joinToString("/")
    }

    private fun compileAndEvalModule(path: String): IV8Module {
        val source = loadClasspathText(path) ?: error("Module not found: $path")
        val module = moduleCache.getOrPut(path) {
            v8Runtime.getExecutor(source)
                .setResourceName(path)
                .compileV8Module()
        }
        module.instantiate()
        module.execute<V8Value>().close()
        return module
    }

    private fun loadClasspathText(path: String): String? {
        val normalized = path.trimStart('/')
        val loaders = listOf(
            Thread.currentThread().contextClassLoader,
            JSEngine::class.java.classLoader
        )
        for (loader in loaders) {
            loader?.getResource(normalized)?.let { url ->
                return url.openStream().bufferedReader(Charsets.UTF_8).readText()
            }
        }
        JSEngine::class.java.getResource("/$normalized")?.let { url ->
            return url.openStream().bufferedReader(Charsets.UTF_8).readText()
        }
        return null
    }

    // ── Shutdown ───────────────────────────────────────────────────────────

    override fun close() {
        // Stop the periodic tick first (let the current invocation finish).
        tickFuture.cancel(false)

        // Run V8 cleanup on the V8 thread, then shut down the executor.
        v8Executor.submit {
            moduleCache.values.forEach { runCatching { it.close() } }
            moduleCache.clear()
            runCatching { if (::loaderCtx.isInitialized) loaderCtx.close() }
            runCatching { v8Runtime.globalObject.delete("ktConsole") }
            runCatching { v8Runtime.close() }
        }.get()

        v8Executor.shutdown()
        v8Executor.awaitTermination(5, TimeUnit.SECONDS)
        logger.info("JSEngine closed")
    }
}
