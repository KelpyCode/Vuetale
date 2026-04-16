package li.kelp.vuetale.javascript

import li.kelp.vuetale.app.AppManager
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * Watches the Vite output directory (`vuetale/core/`) for `.js` file changes and
 * performs a hot reload when files are written by Vite's `--watch` mode.
 *
 * **Two-tier reload strategy:**
 * 1. **Partial HMR** – when exactly one compiled `.vue.js` component file changes and
 *    `__VUE_HMR_RUNTIME__` is available (Vue dev build loaded), the module is
 *    re-evaluated in the live V8 runtime via [JSEngine.reloadModule].  The HMR snippet
 *    injected by [HmrIdsPlugin] calls `__VUE_HMR_RUNTIME__.reload` automatically,
 *    updating component instances in-place with no engine restart.
 * 2. **Full restart** – all other changes (non-component files, multi-file changes, or
 *    when the Vue dev build is not present) trigger a full [JSEngine] restart and app
 *    remount – the previous behaviour.
 *
 * Only active when [DevConfig.isDevMode] is `true`.  The watcher thread is a daemon
 * thread and is restarted automatically if it dies unexpectedly.  A 400 ms debounce
 * prevents multiple rapid reloads when Vite writes several files in quick succession.
 */
object HotReloadManager {

    private val logger = Logger.getLogger("HotReloadManager")

    private val debounceScheduler = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "vuetale-hotreload-debounce").also { it.isDaemon = true }
    }

    @Volatile private var debounceTask: ScheduledFuture<*>? = null
    @Volatile private var started = false

    /** Absolute paths of every .js file that changed during the current debounce window. */
    private val pendingChanges = mutableSetOf<String>()
    private val pendingChangesLock = Any()

    // ── Public API ─────────────────────────────────────────────────────────

    /**
     * Start the file watcher if dev mode is enabled and not yet started.
     * Safe to call on every [JSEngine] init – subsequent calls are no-ops.
     */
    fun startIfNeeded() {
        if (!DevConfig.isDevMode || started) return
        started = true

        val path = DevConfig.devResourcesPath!!.resolve("")
        if (!Files.isDirectory(path)) {
            logger.warning("Hot reload watch path does not exist: $path – watching skipped")
            return
        }

        startWatcherThread(path)
        logger.info("Hot reload watching: $path")
    }

    // ── File watching ──────────────────────────────────────────────────────

    private fun startWatcherThread(watchPath: Path) {
        Thread({
            try {
                watchDirectory(watchPath)
            } catch (e: Exception) {
                logger.severe("Hot reload watcher crashed: ${e.message} – restarting in 2 s")
                Thread.sleep(2_000)
                // Restart the watcher thread so we never stop watching permanently
                startWatcherThread(watchPath)
            }
        }, "vuetale-hotreload").also {
            it.isDaemon = true
            it.start()
        }
    }

    private fun watchDirectory(watchPath: Path) {
        val watchService = FileSystems.getDefault().newWatchService()
        val keyToPath = mutableMapOf<WatchKey, Path>()

        fun registerDir(dir: Path) {
            val key = dir.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
            )
            keyToPath[key] = dir
        }

        Files.walk(watchPath).filter { Files.isDirectory(it) }.forEach { registerDir(it) }

        while (true) {
            val key = try {
                watchService.take()
            } catch (_: InterruptedException) {
                break
            }

            val dir = keyToPath[key] ?: run { key.reset(); continue }

            for (event in key.pollEvents()) {
                try {
                    val kind = event.kind()
                    if (kind == StandardWatchEventKinds.OVERFLOW) continue

                    @Suppress("UNCHECKED_CAST")
                    val changed = dir.resolve((event as WatchEvent<Path>).context())

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(changed)) {
                        registerDir(changed)
                    }

                    if (changed.toString().endsWith(".js")) {
                        logger.fine("Detected change: $changed")
                        scheduleReload(changed.toAbsolutePath().toString())
                    }
                } catch (e: Exception) {
                    logger.warning("Error processing watch event: ${e.message}")
                }
            }

            if (!key.reset()) {
                keyToPath.remove(key)
                if (keyToPath.isEmpty()) break
            }
        }
    }

    private fun scheduleReload(changedAbsPath: String) {
        synchronized(pendingChangesLock) { pendingChanges.add(changedAbsPath) }
        debounceTask?.cancel(false)
        debounceTask = debounceScheduler.schedule(::performHotReload, 400, TimeUnit.MILLISECONDS)
    }

    // ── Hot reload ─────────────────────────────────────────────────────────

    /** Convert an absolute filesystem path to a classpath-relative path, or null if outside devResourcesPath. */
    private fun toClasspathPath(absPath: String): String? {
        val resourcesPath = DevConfig.devResourcesPath ?: return null
        return try {
            resourcesPath.relativize(Path.of(absPath)).toString().replace('\\', '/')
        } catch (_: Exception) {
            null
        }
    }

    private fun performHotReload() {
        val changes = synchronized(pendingChangesLock) {
            val copy = pendingChanges.toSet()
            pendingChanges.clear()
            copy
        }

        logger.info("Hot reload triggered – ${changes.size} file(s) changed")

        // ── Attempt partial HMR for a single compiled .vue component ──────
        if (changes.size == 1) {
            val absPath = changes.first()
            val classpathPath = toClasspathPath(absPath)
            if (classpathPath != null && isVueComponent(classpathPath)) {
                logger.fine("Attempting partial HMR for: $classpathPath")
                val success = runCatching {
                    JSEngine.instance.reloadModule(classpathPath)
                }.getOrElse { e ->
                    logger.warning("Partial HMR threw: ${e.message}")
                    false
                }
                if (success) {
                    logger.info("Hot reload complete – partial HMR applied (no restart)")
                    return
                }
                logger.info("Partial HMR unavailable – falling back to full restart")
            }
        }

        // ── Full engine restart ────────────────────────────────────────────
        performFullRestart()
    }

    /**
     * Returns true for compiled Vue SFC output files (e.g. `components/Foo.vue.js`,
     * `pages/Bar.vue.js`).  These are the files that carry `__hmrId` injected by
     * [HmrIdsPlugin] and can therefore be hot-swapped without a full restart.
     */
    private fun isVueComponent(classpathPath: String): Boolean =
        classpathPath.endsWith(".vue.js")

    private fun performFullRestart() {
        logger.info("Hot reload: full engine restart...")

        val mountedAppIds = AppManager.apps.values
            .filter { it.isMounted }
            .map { it.getId() }

        // Reset Kotlin-side state without touching V8 (it is about to be torn down)
        AppManager.apps.values.forEach { it.forceReset() }

        try {
            JSEngine.restart()
        } catch (e: Exception) {
            logger.severe("JSEngine restart failed: ${e.message}")
            return
        }

        // Re-create each app's Vue counterpart in the new engine
        AppManager.apps.values.forEach { app ->
            runCatching { app.reinitializeInEngine() }
                .onFailure { logger.warning("Failed to reinitialize app '${app.getId()}': ${it.message}") }
        }

        // Re-mount apps that were previously mounted
        mountedAppIds.forEach { id ->
            AppManager.apps[id]?.let { app ->
                runCatching { app.mount() }
                    .onFailure { logger.warning("Failed to remount app '$id': ${it.message}") }

                // The client still has the old DOM with old element IDs.
                // Discard any incremental tracking accumulated during re-mount and
                // force a full clear+appendInline so onDirty never tries to reference
                // new generated IDs that don't yet exist in the client's UI.
                app.insertedElements.clear()
                app.removedElementSelectors.clear()
                app.dirtyElementIds.clear()
                app.hasStructuralChanges = true

                // isDirty=true triggers onDirty on the next V8 tick
                app.isDirty = true
            }
        }

        logger.info("Hot reload complete – full restart, ${mountedAppIds.size} app(s) remounted")
    }
}
