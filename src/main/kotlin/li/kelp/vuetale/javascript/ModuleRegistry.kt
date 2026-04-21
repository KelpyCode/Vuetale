package li.kelp.vuetale.javascript

import java.nio.file.Path
import java.util.logging.Logger

/**
 * Registry mapping short module aliases (e.g. `"core"`) to the [ClassLoader] that owns
 * the corresponding JAR's resources, and optionally to a live dev-mode filesystem path
 * so that Vite's watch-mode output is picked up for that alias without restarting the server.
 *
 * ### Why this exists
 * When a third-party mod depends on Vuetale, its Vite project generates JS output into
 * `src/main/resources/vuetale/<alias>/`.  At runtime, imports of the form
 * `@<alias>/components/Foo` must resolve to the classpath path `vuetale/<alias>/components/Foo.js`
 * – but that path lives inside the *mod's* JAR, not the Vuetale JAR.
 *
 * [ModuleRegistry.registerModule] records the [ClassLoader] obtained from the caller's
 * class reference so [JSEngine]'s classpath resolver can load resources from the right JAR.
 *
 * ### Usage (in your plugin's init)
 * ```kotlin
 * // Production – JAR resources only
 * ModuleRegistry.registerModule("myMod", MyPlugin::class.java)
 *
 * // Dev mode – also read live Vite output from the filesystem
 * ModuleRegistry.registerModule("myMod", MyPlugin::class.java,
 *     devResourcesPath = Path.of("C:/Projects/MyMod/src/main/resources"))
 * ```
 *
 * The alias must match the `name` field in that mod's `vuetale-plugin.json` / `manifest.json`.
 */
object ModuleRegistry {
    private val logger = Logger.getLogger("ModuleRegistry")

    data class ModuleEntry(
        val alias: String,
        val classLoader: ClassLoader,
        /** Absolute path to `src/main/resources/` for live dev-mode reloads. `null` in production. */
        val devResourcesPath: Path? = null,
    )

    private val modules: MutableMap<String, ModuleEntry> = mutableMapOf()

    /**
     * Register a module alias pointing at the JAR that contains [anchorClass].
     *
     * The dev resources path is resolved automatically from `vuetale-dev.properties`
     * (entry `vuetale.devResourcesPath.<alias>`).  Pass [devResourcesPathOverride] only
     * if you need to override that value programmatically.
     *
     * Safe to call multiple times with the same alias – the last registration wins.
     */
    fun registerModule(alias: String, anchorClass: Class<*>, devResourcesPathOverride: Path? = null) {
        val loader = anchorClass.classLoader
            ?: ClassLoader.getSystemClassLoader()
        val devPath = devResourcesPathOverride ?: DevConfig.aliasDevResourcesPaths[alias]
        modules[alias] = ModuleEntry(alias, loader, devPath)
        val devNote = if (devPath != null) " (dev path: $devPath)" else ""
        logger.info("ModuleRegistry: registered alias '@$alias' via ${anchorClass.name}$devNote")

        // If the hot-reload watcher is already running, start watching the new path immediately.
        if (devPath != null) {
            HotReloadManager.watchPluginPath(devPath)
        }
    }

    /** Kotlin convenience overload. */
    inline fun <reified T> registerModule(alias: String, devResourcesPathOverride: Path? = null) =
        registerModule(alias, T::class.java, devResourcesPathOverride)

    /**
     * If [DevConfig.isDevMode] and the entry for this alias has a [ModuleEntry.devResourcesPath],
     * try to read [classpathPath] from the filesystem first.
     * Returns `null` if not in dev mode, no dev path is registered, or the file does not exist.
     */
    fun loadDevText(classpathPath: String): String? {
        if (!DevConfig.isDevMode) return null
        val alias = extractAlias(classpathPath) ?: return null
        val entry = modules[alias] ?: return null
        val devPath = entry.devResourcesPath ?: return null
        val file = devPath.resolve(classpathPath).toFile()
        if (!file.exists()) return null
        return file.readText(Charsets.UTF_8)
    }

    /**
     * Load the text content of [classpathPath] (e.g. `"vuetale/core/components/Foo.js"`)
     * using the [ClassLoader] registered under the matching alias.
     *
     * Returns `null` if the alias is not registered or the resource is not found.
     */
    fun loadText(classpathPath: String): String? {
        val alias = extractAlias(classpathPath) ?: return null
        val entry = modules[alias] ?: return null
        return entry.classLoader
            .getResource(classpathPath)
            ?.openStream()
            ?.bufferedReader(Charsets.UTF_8)
            ?.readText()
    }

    /** Return all registered [ModuleEntry] objects. */
    fun entries(): Collection<ModuleEntry> = modules.values

    /**
     * Return all registered aliases.
     */
    fun aliases(): Set<String> = modules.keys.toSet()

    /**
     * Extract the module alias from a classpath path like `"vuetale/core/..."` → `"core"`.
     * Returns `null` if the path does not match the `vuetale/<alias>/` pattern.
     */
    fun extractAlias(path: String): String? {
        val normalized = path.trimStart('/')
        if (!normalized.startsWith("vuetale/")) return null
        val rest = normalized.removePrefix("vuetale/")
        val slash = rest.indexOf('/')
        return if (slash > 0) rest.substring(0, slash) else null
    }
}

