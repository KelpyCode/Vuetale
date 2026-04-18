package li.kelp.vuetale.javascript

import java.util.logging.Logger

/**
 * Registry mapping short module aliases (e.g. `"core"`) to the [ClassLoader] that owns
 * the corresponding JAR's resources.
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
 * ModuleRegistry.registerModule("core", MyPlugin::class.java)
 * ```
 *
 * The alias must match the `name` field in that mod's `vuetale-plugin.json` / `manifest.json`.
 */
object ModuleRegistry {
    private val logger = Logger.getLogger("ModuleRegistry")

    data class ModuleEntry(
        val alias: String,
        val classLoader: ClassLoader
    )

    private val modules: MutableMap<String, ModuleEntry> = mutableMapOf()

    /**
     * Register a module alias pointing at the JAR that contains [anchorClass].
     * The alias must be lowercase, matching the mod's `manifest.json` `name` field.
     *
     * Safe to call multiple times with the same alias – the last registration wins
     * (useful during hot-reload scenarios).
     */
    fun registerModule(alias: String, anchorClass: Class<*>) {
        val loader = anchorClass.classLoader
            ?: ClassLoader.getSystemClassLoader()
        modules[alias] = ModuleEntry(alias, loader)
        logger.info("ModuleRegistry: registered alias '@$alias' via ${anchorClass.name}")
    }

    /** Kotlin convenience overload. */
    inline fun <reified T> registerModule(alias: String) = registerModule(alias, T::class.java)

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

