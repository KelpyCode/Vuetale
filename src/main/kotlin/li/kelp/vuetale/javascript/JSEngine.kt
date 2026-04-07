package li.kelp.vuetale.javascript

import com.caoccao.javet.exceptions.JavetException
import com.caoccao.javet.interop.V8Host
import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.interop.callback.IV8ModuleResolver
import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.reference.IV8Module
import com.caoccao.javet.values.reference.V8ValueObject

/**
 * Core JavaScript engine wrapper built on Javet/V8.
 *
 * Responsibilities:
 *  - Own a single [V8Runtime] for the lifetime of the mod.
 *  - Expose a classpath-aware ES-module resolver so every `import` in
 *    bundled JS files is satisfied from JAR resources.
 *  - Boot the Vue IIFE + the loader.js entry-point.
 *  - Bridge Kotlin host objects to JavaScript via [JavetProxyConverter].
 */
class JSEngine : AutoCloseable {

    /** Converter that wraps/unwraps Kotlin objects as V8 Proxies. */
    val converter: JavetProxyConverter = JavetProxyConverter()

    private val v8Runtime: V8Runtime = V8Host.getV8Instance().createV8Runtime()

    /** VueBridge instance shared with the JS side via globalThis.ktBridge. */
    val bridge: VueBridge = VueBridge(v8Runtime, converter)

    /** Cache of already-compiled ES modules keyed by their resolved classpath path. */
    private val moduleCache: MutableMap<String, IV8Module> = mutableMapOf()

    /**
     * The _vt global object set by loader.js – holds createUserApp,
     * getUserApp, registerUserAppRef, etc.
     */
    lateinit var loaderCtx: V8ValueObject
        private set

    companion object {
        val instance: JSEngine by lazy { JSEngine() }

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

    init {
        v8Runtime.setConverter(converter)
        v8Runtime.setV8ModuleResolver(ClasspathModuleResolver())

        // 0. Wire up console -> JVM logger.
        //    JS args are joined to a string on the JS side to avoid any
        //    converter issues with complex objects.
        v8Runtime.globalObject.set("ktConsole", JsConsole())
        v8Runtime.getExecutor("""
            globalThis.console = {
                log:   (...a) => ktConsole.log  (a.map(x => (x !== null && x !== undefined) ? String(x) : String(x)).join(' ')),
                info:  (...a) => ktConsole.info (a.map(x => String(x)).join(' ')),
                warn:  (...a) => ktConsole.warn (a.map(x => String(x)).join(' ')),
                error: (...a) => ktConsole.error(a.map(x => String(x)).join(' ')),
                debug: (...a) => ktConsole.debug(a.map(x => String(x)).join(' ')),
            };
        """.trimIndent()).executeVoid()

        v8Runtime.globalObject.set("ktBridge", bridge)

        // 1. Load Vue as a plain IIFE script -> sets globalThis.Vue
        val vueIife = loadClasspathText("vue.js")
            ?: error("vue.js not found on classpath")
        v8Runtime.getExecutor(vueIife).executeVoid()
        v8Runtime.getExecutor("if (typeof Vue !== 'undefined') globalThis.Vue = Vue;").executeVoid()

        // 2. Minimal DOM stub (Vue needs document.querySelector for mount).
        v8Runtime.getExecutor(DOM_MOCK).executeVoid()

        // 3. Load loader.js as an ES module; it sets globalThis._vt.
        compileAndEvalModule("vuetale/core/loader.js")

        loaderCtx = v8Runtime.globalObject.get("_vt")
            ?: error("globalThis._vt was not set after loading loader.js")
    }

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

    /** Evaluate a plain (non-module) JavaScript snippet; result is discarded. */
    fun evalScript(script: String) {
        v8Runtime.getExecutor(script).executeVoid()
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

    override fun close() {
        moduleCache.values.forEach { runCatching { it.close() } }
        moduleCache.clear()
        runCatching { if (::loaderCtx.isInitialized) loaderCtx.close() }
        runCatching { v8Runtime.globalObject.delete("ktConsole") }
        runCatching { v8Runtime.close() }
    }
}
