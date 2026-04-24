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
import li.kelp.vuetale.app.AppManager
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
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
        @Volatile
        private var _instance: JSEngine? = null
        private val instanceLock = Any()

        val instance: JSEngine
            get() = _instance ?: synchronized(instanceLock) {
                _instance ?: JSEngine().also { _instance = it }
            }

        /**
         * Close the current [JSEngine] and create a fresh one.
         * Called by [HotReloadManager] during a hot reload cycle.
         * Thread-safe; blocks until the new engine is fully initialized.
         */
        fun restart(): JSEngine = synchronized(instanceLock) {
            _instance?.close()
            JSEngine().also { _instance = it }
        }

        private val logger: Logger = Logger.getLogger("JSEngine")

        /**
         * Monotonically-increasing counter incremented on every [JSEngine] instantiation.
         * Included in every module resource name so that V8 never confuses a module compiled
         * in generation N with one from generation N+1, even if their classpath paths are
         * identical.  This is the primary defence against any process-level module compilation
         * cache that Javet/Node.js may maintain between [V8Runtime] instances.
         */
        private val engineGeneration = AtomicInteger(0)

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
     * Maps each module's fake resource name (the flat path inside [moduleRootDir],
     * e.g. `vuetale-modules/vuetale-core-loader.js`) back to its real classpath path.
     *
     * Keyed by string instead of `IV8Module` instance because Javet re-wraps the same
     * underlying V8 handle in a new Java object every time it passes the referrer to
     * [ClasspathModuleResolver.resolve].  Identity-based lookup would miss the entry;
     * string lookup on `v8ModuleReferrer.resourceName` is stable.
     */
    private val modulePathIndex: MutableMap<String, String> = mutableMapOf()

    /**
     * Flat directory used as the parent for all compiled module resource names.
     *
     * Javet's [com.caoccao.javet.interop.executors.IV8Executor.setResourceName] calls
     * `process.chdir()` to the resource name's parent directory.  If that directory does
     * not exist the call fails and Javet logs a SEVERE.  By mapping all classpath paths to
     * flat filenames inside this pre-created temp directory, `chdir` always succeeds and
     * the log spam is eliminated while still giving every V8 module a unique identity string.
     *
     * **Linux CWD fix:** Javet/Node.js invokes `uv_chdir()` at the native C++ level (not
     * the JS `process.chdir` function) to change to this directory while compiling each
     * module.  On Linux this is a real OS-level `chdir()` that affects the entire JVM
     * process, breaking any relative-path file I/O made by the game server afterwards.
     * To neutralise this, we point [moduleRootDir] at the JVM's original working directory
     * (`System.getProperty("user.dir")`), so the native `chdir()` call is always a no-op
     * (the process is already there).  `user.dir` is set once at JVM startup and is never
     * updated by subsequent native `chdir()` calls, making it a stable anchor.
     *
     * Example: `"vuetale/core/loader.js"` becomes `"(user.dir)/g1-vuetale-core-loader.js"`
     */
    private val moduleRootDir: java.io.File by lazy {
        // Must be the server's actual working directory so that Javet's native uv_chdir()
        // call does not move us away from the directory Hytale resolves relative paths from.
        java.io.File(System.getProperty("user.dir"))
            .also { it.mkdirs() }
    }

    /**
     * This engine's generation number – stamped into every module resource name so that
     * V8 never reuses a compiled module from a previous [JSEngine] instance, regardless of
     * whether Javet maintains any process-level module cache between [V8Runtime] restarts.
     */
    private val generation: Int = engineGeneration.incrementAndGet()

    /**
     * Per-instance counter for partial hot-reloads.  Each [reloadModule] call gets a
     * unique suffix so the reloaded module's resource name is different from both the
     * original module AND every previous reload of the same file.
     */
    private val reloadSeq = AtomicInteger(0)

    /**
     * Convert a classpath path (e.g. `"vuetale/core/loader.js"`) to a flat absolute path
     * inside [moduleRootDir] (e.g. `".../vuetale-modules/g1-vuetale-core-loader.js"`).
     * The `g<generation>` prefix ensures cross-restart uniqueness.
     * The parent is always [moduleRootDir], which is guaranteed to exist.
     */
    private fun makeResourceName(classpathPath: String): String =
        java.io.File(moduleRootDir, "g${generation}-" + classpathPath.replace('/', '-').replace('\\', '-'))
            .absolutePath

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

        // 3. Start the hot-reload file watcher (no-op outside dev mode).
        HotReloadManager.startIfNeeded()

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
        v8Runtime.getExecutor(
            """
            globalThis.console = {
                log:   (...a) => ktConsole.log  (a.map(x => String(x)).join(' ')),
                info:  (...a) => ktConsole.info (a.map(x => String(x)).join(' ')),
                warn:  (...a) => ktConsole.warn (a.map(x => String(x)).join(' ')),
                error: (...a) => ktConsole.error(a.map(x => String(x)).join(' ')),
                debug: (...a) => ktConsole.debug(a.map(x => String(x)).join(' ')),
            };
        """.trimIndent()
        ).executeVoid()

        v8Runtime.globalObject.set("ktBridge", bridge)

        // Install timer wrapper before loading any third-party code so timers
        // created by Vue/loader.js are tracked.
        runCatching {
            v8Runtime.getExecutor(
                """
                (function() {
                    if (globalThis.__vt_timers_installed) return;
                    globalThis.__vt_timers_installed = true;
                    globalThis.__vt_timers = new Set();
                    const _setTimeout = globalThis.setTimeout;
                    const _setInterval = globalThis.setInterval;

                    globalThis.setTimeout = function(fn, t, ...args) {
                        const id = _setTimeout(function(...a) {
                            try { fn(...a); } catch (e) { console.error('__vt timer error', e); }
                            try { globalThis.__vt_timers.delete(id); } catch (e) {}
                        }, t, ...args);
                        try { globalThis.__vt_timers.add(id); } catch (e) {}
                        return id;
                    };

                    globalThis.setInterval = function(fn, t, ...args) {
                        const id = _setInterval(fn, t, ...args);
                        try { globalThis.__vt_timers.add(id); } catch (e) {}
                        return id;
                    };

                    globalThis.__vt_clearTimers = function() {
                        try {
                            for (const id of Array.from(globalThis.__vt_timers || [])) {
                                try { clearTimeout(id); clearInterval(id); } catch (e) {}
                            }
                            globalThis.__vt_timers && globalThis.__vt_timers.clear();
                        } catch (e) { /* ignore */ }
                    };
                })();
            """.trimIndent()
            ).executeVoid()
        }

        // Load Vue as a plain IIFE script -> sets globalThis.Vue.
        // In dev mode with useDevVue=true, prefer vue.dev.js (full warnings + __VUE_HMR_RUNTIME__).
        val vueIife = (if (DevConfig.useDevVue) loadClasspathText("vue.dev.js") else null)
            ?: loadClasspathText("vue.js")
            ?: error("vue.js not found on classpath")
        v8Runtime.getExecutor(vueIife).executeVoid()
        v8Runtime.getExecutor("if (typeof Vue !== 'undefined') globalThis.Vue = Vue;").executeVoid()

        // Minimal DOM stub (Vue needs document.querySelector for mount).
        v8Runtime.getExecutor(DOM_MOCK).executeVoid()

        // Load loader.js as an ES module; it sets globalThis._vt.
        compileAndEvalModule("vuetale/core/loader.js")

        // In dev mode, load the optional debug helper module.
        if (DevConfig.isDevMode) {
            runCatching { compileAndEvalModule("vuetale/core/debug.js") }
                .onFailure { logger.info("debug.js not found, skipping: ${it.message}") }
        }

        loaderCtx = v8Runtime.globalObject.get("_vt")
            ?: error("globalThis._vt was not set after loading loader.js")

        // Install a small timer registry wrapper so we can clear outstanding
        // setTimeout/setInterval callbacks during full engine restart.  Without
        // this, callbacks scheduled before a hot-reload may run against a torn
        // down ktBridge/global context and cause crashes.  The wrapper keeps a
        // Weak-ish registry of active timer ids and exposes __vt_clearTimers().
        runCatching {
            v8Runtime.getExecutor(
                """
                (function() {
                    if (globalThis.__vt_timers_installed) return;
                    globalThis.__vt_timers_installed = true;
                    globalThis.__vt_timers = new Set();
                    const _setTimeout = globalThis.setTimeout;
                    const _setInterval = globalThis.setInterval;

                    globalThis.setTimeout = function(fn, t, ...args) {
                        const id = _setTimeout(function(...a) {
                            try { fn(...a); } catch (e) { console.error('__vt timer error', e); }
                            try { globalThis.__vt_timers.delete(id); } catch (e) {}
                        }, t, ...args);
                        try { globalThis.__vt_timers.add(id); } catch (e) {}
                        return id;
                    };

                    globalThis.setInterval = function(fn, t, ...args) {
                        const id = _setInterval(fn, t, ...args);
                        try { globalThis.__vt_timers.add(id); } catch (e) {}
                        return id;
                    };

                    globalThis.__vt_clearTimers = function() {
                        try {
                            for (const id of Array.from(globalThis.__vt_timers || [])) {
                                try { clearTimeout(id); clearInterval(id); } catch (e) {}
                            }
                            globalThis.__vt_timers && globalThis.__vt_timers.clear();
                        } catch (e) { /* ignore */ }
                    };
                })();
            """.trimIndent()
            ).executeVoid()
        }
    }

    /** Called repeatedly by the scheduler on the V8 thread. Pumps the Node.js event loop. */
    private fun tickInternal() {
        runCatching { v8Runtime.await(V8AwaitMode.RunOnce) }
            .onFailure { logger.warning("V8 tick error: ${it.message}") }

        // After the full Vue render batch has run, notify any dirty apps exactly once.
        AppManager.apps.values.forEach { app ->
            if (app.isDirty) {
                app.isDirty = false
                runCatching { app.onDirty?.invoke() }
                    .onFailure { logger.warning("onDirty callback error for app '${app.getId()}': ${it.message}") }
            }
        }
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

    /**
     * Pre-load a Vue component module and register it in `_vt.COMPONENT_REGISTRY`
     * so that `App.vue` can retrieve it synchronously (bypassing dynamic `import()`
     * which is not supported in Javet's embedded Node.js runtime).
     *
     * This works by compiling a tiny one-shot ES module:
     * ```js
     * import __Component from '<aliasPath>';
     * globalThis._vt.registerComponent('<aliasPath>', __Component);
     * ```
     * The static `import` goes through [ClasspathModuleResolver] as normal.
     *
     * Must be called **before** [App.createApp] or [App.navigateTo] for the given path.
     * Must be called **before** [App.createApp] or [App.navigateTo] for the given path.
     * Must be called **before** [App.createApp] or [App.navigateTo] for the given path.
     *
     * @param aliasPath  Module alias path, e.g. `"@core/pages/Dashboard"`.
     */
    fun preloadComponent(aliasPath: String) {
        runOnV8Thread {
            val escapedPath = aliasPath.replace("'", "\\'")
            val source = "import __Comp from '$escapedPath'; globalThis._vt.registerComponent('$escapedPath', __Comp);"
            val fakeResourceName = makeResourceName("__preload__${aliasPath}")
            val module = v8Runtime.getExecutor(source)
                .setResourceName(fakeResourceName)
                .compileV8Module()
            modulePathIndex[fakeResourceName] = "__preload__${aliasPath}"
            module.instantiate()
            module.execute<V8Value>().close()
            logger.info("Preloaded component: $aliasPath")
        }
    }

    // ── Module resolver (runs on V8 thread during import resolution) ───────

    private inner class ClasspathModuleResolver : IV8ModuleResolver {
        @Throws(JavetException::class)
        override fun resolve(
            v8Runtime: V8Runtime,
            resourceName: String,
            v8ModuleReferrer: IV8Module?
        ): IV8Module {
            // Look up the referrer's classpath path via the stable resource-name string.
            // v8ModuleReferrer is re-wrapped in a new Java object on every call, so we
            // cannot use identity (IdentityHashMap); resourceName is stable.
            val referrerPath = v8ModuleReferrer?.resourceName?.let { modulePathIndex[it] }
            val resolvedPath = resolveModulePath(resourceName, referrerPath)
            // Try the resolved path, then fall back to <path without .js>/index.js
            // to support directory imports (e.g. @core/components/core → components/core/index.js).
            val (effectivePath, source) = run {
                val direct = loadSourceText(resolvedPath)
                if (direct != null) return@run resolvedPath to direct
                if (resolvedPath.endsWith(".js")) {
                    val indexPath = resolvedPath.removeSuffix(".js") + "/index.js"
                    val indexSrc = loadSourceText(indexPath)
                    if (indexSrc != null) return@run indexPath to indexSrc
                }
                null to null
            }
            if (effectivePath == null || source == null) {
                logger.warning("ES module not found: $resolvedPath (imported as '$resourceName'${if (referrerPath != null) " from '$referrerPath'" else ""})")
                error("ES module not found: $resolvedPath (imported as '$resourceName')")
            }
            return moduleCache.getOrPut(effectivePath) {
                val fakeResourceName = makeResourceName(effectivePath)
                val module = v8Runtime.getExecutor(source)
                    .setResourceName(fakeResourceName)
                    .compileV8Module()
                modulePathIndex[fakeResourceName] = effectivePath
                module
            }
        }
    }

    /** Manifest data cached per alias (e.g. "core" → ManifestData). */
    private val manifestCache: MutableMap<String, ManifestData> = mutableMapOf()

    private data class ManifestData(
        val pages: List<String>,
        val huds: List<String>,
        val components: List<String>,
    )

    /** Load and cache the manifest.json for a given module alias. */
    private fun getManifest(alias: String): ManifestData {
        return manifestCache.getOrPut(alias) {
            val source = loadSourceText("vuetale/$alias/manifest.json")
                ?: return@getOrPut ManifestData(emptyList(), emptyList(), emptyList())
            ManifestData(
                pages = parseJsonStringArray(source, "pages"),
                huds = parseJsonStringArray(source, "huds"),
                components = parseJsonStringArray(source, "components"),
            )
        }
    }

    private fun parseJsonStringArray(json: String, key: String): List<String> {
        val block = Regex(""""$key"\s*:\s*\[(.*?)\]""", setOf(RegexOption.DOT_MATCHES_ALL))
            .find(json)?.groupValues?.get(1) ?: return emptyList()
        return Regex(""""([^"]+)"""").findAll(block).map { it.groupValues[1] }.toList()
    }

    private fun resolveModulePath(resourceName: String, referrerPath: String?): String {
        // Strip the optional "vt:" scheme prefix used in Vue component imports.
        val resourceName = resourceName.removePrefix("vt:")
        // @alias/some/path  →  vuetale/<alias>/some/path.js
        // @alias/Name       →  shorthand: look up manifest to find pages/huds/components/Name.vue.js
        // @alias/some/path  →  vuetale/<alias>/some/path.js
        // @alias/Name       →  shorthand: look up manifest to find pages/huds/components/Name.vue.js
        if (resourceName.startsWith("@")) {
            val withoutAt = resourceName.removePrefix("@")
            val slash = withoutAt.indexOf('/')
            if (slash > 0) {
                val alias = withoutAt.substring(0, slash)
                val rest = withoutAt.substring(slash + 1)

                // Bare name (no sub-path) – try manifest lookup first
                if (!rest.contains('/')) {
                    val name = rest.removeSuffix(".vue.js").removeSuffix(".js")
                    val manifest = getManifest(alias)
                    manifest.pages.find { it == name || it.endsWith("/$name") }?.let { entry ->
                        val file = if (entry.contains('/')) "$entry.vue.js" else "pages/$entry.vue.js"
                        return "vuetale/$alias/$file"
                    }
                    manifest.huds.find { it == name || it.endsWith("/$name") }?.let { entry ->
                        val file = if (entry.contains('/')) "$entry.vue.js" else "huds/$entry.vue.js"
                        return "vuetale/$alias/$file"
                    }
                    manifest.components.find { it == name || it.endsWith("/$name") }?.let { entry ->
                        val file = if (entry.contains('/')) "$entry.vue.js" else "components/$entry.vue.js"
                        return "vuetale/$alias/$file"
                    }
                }

                val withExt = if (rest.endsWith(".js")) rest else "$rest.js"
                return "vuetale/$alias/$withExt"
            }
        }
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
                "", "." -> {}
                ".." -> if (stack.isNotEmpty()) stack.removeLast()
                else -> stack.add(part)
            }
        }
        return stack.joinToString("/")
    }

    private fun compileAndEvalModule(path: String): IV8Module {
        val source = loadSourceText(path) ?: error("Module not found: $path")
        val module = moduleCache.getOrPut(path) {
            val fakeResourceName = makeResourceName(path)
            val m = v8Runtime.getExecutor(source)
                .setResourceName(fakeResourceName)
                .compileV8Module()
            modulePathIndex[fakeResourceName] = path
            m
        }
        module.instantiate()
        module.execute<V8Value>().close()
        return module
    }

    /**
     * Load JS source for [path].
     *
     * Resolution order (first hit wins):
     * 1. Main dev filesystem: `DevConfig.devResourcesPath / path` (Vuetale's own Vite output).
     * 2. Per-alias dev filesystem: `ModuleRegistry` entry's `devResourcesPath / path` when the
     *    path belongs to a plugin alias that registered a dev path (plugin's own Vite output).
     * 3. Classpath / JAR resources via [loadClasspathText].
     */
    private fun loadSourceText(path: String): String? {
        if (DevConfig.isDevMode) {
            val fsFile = DevConfig.devResourcesPath!!.resolve(path).toFile()
            if (fsFile.exists()) return fsFile.readText(Charsets.UTF_8)

            // Check per-alias dev paths registered by third-party plugins.
            ModuleRegistry.loadDevText(path)?.let { return it }
        }
        return loadClasspathText(path)
    }

    private fun loadClasspathText(path: String): String? {
        val normalized = path.trimStart('/')
        val loaders = listOfNotNull(
            Thread.currentThread().contextClassLoader,
            JSEngine::class.java.classLoader,
            ClassLoader.getSystemClassLoader(),
        ).distinct()
        for (loader in loaders) {
            loader?.getResource(normalized)?.let { url ->
                return url.openStream().bufferedReader(Charsets.UTF_8).readText()
            }
        }
        JSEngine::class.java.getResource("/$normalized")?.let { url ->
            return url.openStream().bufferedReader(Charsets.UTF_8).readText()
        }
        // Try the ModuleRegistry – covers resources living in a third-party mod's JAR.
        ModuleRegistry.loadText(normalized)?.let { return it }
        return null
    }

    // ── Shutdown ───────────────────────────────────────────────────────────

    /**
     * Hot-reload a single compiled module **without** restarting the entire engine.
     *
     * The module source is re-read fresh from the filesystem (or classpath) and compiled
     * under a unique, per-reload resource name so V8 treats it as a brand-new module rather
     * than looking it up in its identity cache.  When the module executes, the HMR snippet
     * injected by [HmrIdsPlugin] automatically calls:
     * ```
     * __VUE_HMR_RUNTIME__.createRecord / reload(hmrId, newComp)
     * ```
     * which makes Vue update all live component instances in-place – no unmount/remount needed.
     *
     * **Requires** the Vue dev IIFE (`vue.dev.js`) to be loaded, which sets the global
     * `__VUE_HMR_RUNTIME__`.  When using the production build, this method returns `false`
     * and [HotReloadManager] falls back to a full engine restart.
     *
     * Must be called from **outside** the V8 thread – it dispatches internally via
     * [runOnV8Thread].
     *
     * @param classpathPath  Classpath-relative path of the changed file,
     *                       e.g. `"vuetale/core/components/Test.vue.js"`.
     * @return `true` if the partial HMR was applied successfully, `false` if the caller
     *         should fall back to a full engine restart.
     */
    fun reloadModule(classpathPath: String): Boolean = runOnV8Thread {
        try {
            // Partial HMR only works when Vue's dev build exposes __VUE_HMR_RUNTIME__
            val hasHmrRuntime = v8Runtime.getExecutor(
                "typeof __VUE_HMR_RUNTIME__ !== 'undefined'"
            ).executeBoolean()
            if (!hasHmrRuntime) {
                logger.info("Partial HMR skipped – __VUE_HMR_RUNTIME__ not available (load vue.dev.js for partial HMR)")
                return@runOnV8Thread false
            }

            val source = loadSourceText(classpathPath)
                ?: run {
                    logger.warning("Partial HMR: source not found for $classpathPath")
                    return@runOnV8Thread false
                }

            // Give this reload its own unique resource name so V8 never confuses it with the
            // original or with any previous reload of the same file.
            val reloadId = reloadSeq.incrementAndGet()
            val reloadResName = makeResourceName(classpathPath)
                .removeSuffix(".js") + "_vtR${reloadId}.js"

            // Register the mapping so relative imports from within this module resolve correctly
            modulePathIndex[reloadResName] = classpathPath

            val mod = v8Runtime.getExecutor(source)
                .setResourceName(reloadResName)
                .compileV8Module()

            mod.instantiate()
            mod.execute<V8Value?>()?.close()

            // Flush Vue's async scheduler so queued component updates run immediately
            runCatching { v8Runtime.await(V8AwaitMode.RunNoWait) }

            // Mark all mounted apps dirty → onDirty fires on the next tick → sendUpdate()
            AppManager.apps.values.forEach { app ->
                if (app.isMounted) app.isDirty = true
            }

            logger.info("Partial HMR applied: $classpathPath (reload #$reloadId)")
            true
        } catch (e: Exception) {
            logger.warning("Partial HMR failed for $classpathPath: ${e.message}")
            false
        }
    }

    override fun close() {
        // Stop the periodic tick first (let the current invocation finish).
        tickFuture.cancel(false)

        // Run V8 cleanup on the V8 thread, then shut down the executor.
        v8Executor.submit {
            moduleCache.values.forEach { runCatching { it.close() } }
            moduleCache.clear()
            modulePathIndex.clear()
            // First, clear any outstanding JS timers so their callbacks cannot run
            // against the soon-to-be-closed runtime (these callbacks often reference
            // global ktBridge and would crash after hot-reload).
            runCatching { v8Runtime.getExecutor("if (typeof __vt_clearTimers === 'function') { try { __vt_clearTimers(); } catch(e){} };").executeVoid() }
            // Remove references that JS callbacks may try to access.
            runCatching { v8Runtime.globalObject.delete("ktBridge") }
            runCatching { if (::loaderCtx.isInitialized) loaderCtx.close() }
            runCatching { v8Runtime.globalObject.delete("ktConsole") }
            runCatching { v8Runtime.close() }
        }.get()

        v8Executor.shutdown()
        v8Executor.awaitTermination(5, TimeUnit.SECONDS)
        logger.info("JSEngine closed")
    }
}
