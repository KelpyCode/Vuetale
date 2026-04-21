package li.kelp.vuetale.javascript

import java.nio.file.Path
import java.util.Properties
import java.util.logging.Logger

/**
 * Development-mode configuration for Vuetale.
 *
 * When [isDevMode] is true, [JSEngine] reads JS files directly from [devResourcesPath]
 * on the filesystem (bypassing the classpath / JAR) and `HotReloadManager` watches
 * the directory for changes to auto-restart the engine.
 *
 * Configuration is read from (in priority order):
 * 1. JVM system property (`-Dvuetale.devResourcesPath=...`)
 * 2. `vuetale-dev.properties` file in the process working directory
 *    (e.g. `run/vuetale-dev.properties`)
 *
 * **Important:** [devResourcesPath] must point to the **resources root**
 * (`src/main/resources/`), NOT to a subdirectory like `vuetale/core/`.
 * All module paths passed to `loadSourceText` are relative to this root
 * (e.g. `"vuetale/core/loader.js"`, `"vue.dev.js"`).
 *
 * Per-plugin aliases can register their own live Vite output directories:
 * ```
 * vuetale.devResourcesPath=C:/Projects/Vuetale/src/main/resources
 * vuetale.devResourcesPath.myMod=C:/Projects/MyMod/src/main/resources
 * vuetale.devVue=true
 * ```
 * When [ModuleRegistry.registerModule] is called for `"myMod"`, it automatically
 * picks up the path above so no code change is needed in the plugin.
 */
object DevConfig {
    private val logger = Logger.getLogger("DevConfig")

    /**
     * Absolute path to the `src/main/resources/` directory of the Vuetale project.
     * `null` when running in production / no dev config is found.
     */
    val devResourcesPath: Path?

    val isDevMode: Boolean get() = devResourcesPath != null

    /**
     * Per-alias dev resources paths read from `vuetale.devResourcesPath.<alias>` entries.
     * Populated even when [devResourcesPath] is null (but [isDevMode] must be true for
     * these paths to be consulted by [JSEngine]).
     */
    val aliasDevResourcesPaths: Map<String, Path>

    /**
     * Whether to load the Vue dev IIFE (`vue.dev.js`) instead of the production `vue.js`.
     */
    val useDevVue: Boolean

    private val props: Properties by lazy {
        val p = Properties()
        val propsFile = Path.of("vuetale-dev.properties").toFile()
        if (propsFile.exists()) propsFile.inputStream().use { p.load(it) }
        p
    }

    init {
        devResourcesPath = readDevResourcesPath()
        aliasDevResourcesPaths = readAliasDevResourcesPaths()
        useDevVue = readUseDevVue()
        if (isDevMode) {
            logger.info("Dev mode ENABLED – reading JS from filesystem: $devResourcesPath")
            if (aliasDevResourcesPaths.isNotEmpty()) {
                aliasDevResourcesPaths.forEach { (alias, path) ->
                    logger.info("Dev mode – plugin alias '@$alias' → $path")
                }
            }
            logger.info("Dev mode – Vue dev build: ${if (useDevVue) "enabled (vue.dev.js)" else "disabled (vue.js)"}")
        }
    }

    private fun readDevResourcesPath(): Path? {
        System.getProperty("vuetale.devResourcesPath")?.takeIf { it.isNotBlank() }
            ?.let { return Path.of(it) }
        props.getProperty("vuetale.devResourcesPath")?.takeIf { it.isNotBlank() }
            ?.let { return Path.of(it) }
        return null
    }

    /**
     * Collect all `vuetale.devResourcesPath.<alias>` entries from system properties
     * and the properties file (system properties take precedence per alias).
     */
    private fun readAliasDevResourcesPaths(): Map<String, Path> {
        val result = mutableMapOf<String, Path>()
        val prefix = "vuetale.devResourcesPath."

        // From properties file first (lower priority)
        props.stringPropertyNames()
            .filter { it.startsWith(prefix) }
            .forEach { key ->
                val alias = key.removePrefix(prefix).takeIf { it.isNotBlank() } ?: return@forEach
                val value = props.getProperty(key)?.takeIf { it.isNotBlank() } ?: return@forEach
                result[alias] = Path.of(value)
            }

        // System properties override (higher priority)
        System.getProperties().stringPropertyNames()
            .filter { it.startsWith(prefix) }
            .forEach { key ->
                val alias = key.removePrefix(prefix).takeIf { it.isNotBlank() } ?: return@forEach
                val value = System.getProperty(key)?.takeIf { it.isNotBlank() } ?: return@forEach
                result[alias] = Path.of(value)
            }

        return result
    }

    private fun readUseDevVue(): Boolean {
        System.getProperty("vuetale.devVue")?.takeIf { it.isNotBlank() }
            ?.let { return it.trim().lowercase() != "false" }
        props.getProperty("vuetale.devVue")?.takeIf { it.isNotBlank() }
            ?.let { return it.trim().lowercase() != "false" }
        return isDevMode
    }
}
