package li.kelp.vuetale.javascript

/**
 * Simple global debug flag for Vuetale. Volatile to ensure visibility across threads.
 */
object DebugConfig {
    @Volatile
    var enabled: Boolean = false

    fun toggle(): Boolean {
        enabled = !enabled
        return enabled
    }
}

