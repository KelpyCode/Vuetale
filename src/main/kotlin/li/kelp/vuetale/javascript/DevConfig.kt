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
 * Example `vuetale-dev.properties`:
 * ```
 * vuetale.devResourcesPath=C:/Users/you/Projects/Hytale/Vuetale/src/main/resources
 * vuetale.devVue=true
 * ```
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
     * Whether to load the Vue dev IIFE (`vue.dev.js`) instead of the production `vue.js`.
     * The dev build enables `__VUE_HMR_RUNTIME__` which powers partial component hot-reload.
     * Defaults to `true` when [isDevMode] is active; can be overridden via `vuetale.devVue`.
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
        useDevVue = readUseDevVue()
        if (isDevMode) {
            logger.info("Dev mode ENABLED – reading JS from filesystem: $devResourcesPath")
            logger.info("Dev mode – Vue dev build: ${if (useDevVue) "enabled (vue.dev.js)" else "disabled (vue.js)"}")
        }
    }

    private fun readDevResourcesPath(): Path? {
        // 1. JVM system property
        System.getProperty("vuetale.devResourcesPath")?.takeIf { it.isNotBlank() }
            ?.let { return Path.of(it) }

        // 2. vuetale-dev.properties in the working directory
        props.getProperty("vuetale.devResourcesPath")?.takeIf { it.isNotBlank() }
            ?.let { return Path.of(it) }

        return null
    }

    private fun readUseDevVue(): Boolean {
        // JVM system property overrides all
        System.getProperty("vuetale.devVue")?.takeIf { it.isNotBlank() }
            ?.let { return it.trim().lowercase() != "false" }

        // Properties file
        props.getProperty("vuetale.devVue")?.takeIf { it.isNotBlank() }
            ?.let { return it.trim().lowercase() != "false" }

        // Default: use dev Vue build whenever dev mode is active
        return isDevMode
    }
}


