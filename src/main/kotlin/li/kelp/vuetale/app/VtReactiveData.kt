package li.kelp.vuetale.app

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Base class for a server-side reactive data object that automatically syncs its
 * properties to a Vue app's data store whenever they are written.
 *
 * ### Kotlin usage (property delegation)
 * ```kotlin
 * class PlayerData(app: App) : VtReactiveData(app) {
 *     var health: Int by prop("playerHealth", 20)
 *     var name: String by prop("playerName", "Unknown")
 * }
 *
 * // Updating the property automatically pushes to Vue
 * playerData.health = 15   // Vue's `useData("playerHealth")` updates reactively
 * ```
 *
 * ### Java usage (manual set)
 * ```java
 * VtReactiveData data = new VtReactiveData(app);
 * data.set("playerHealth", 15);
 * ```
 *
 * ### PlayerUi shortcut
 * You can also attach a [VtReactiveData] to a [PlayerUi] and call [PlayerUi.setData] directly
 * without subclassing:
 * ```kotlin
 * playerUi.setData("playerHealth", player.health)
 * ```
 *
 * @param app The [App] instance whose Vue data store will receive the updates.
 */
open class VtReactiveData(private val app: App) {

    /**
     * Push a single key→value pair to the Vue data store.
     * Thread-safe – dispatches to the V8 thread internally.
     */
    fun set(key: String, value: Any?) = app.setData(key, value)

    /**
     * Push every entry of [map] to the Vue data store.
     * Convenience for batch updates.
     */
    fun setAll(map: Map<String, Any?>) = map.forEach { (k, v) -> set(k, v) }

    /**
     * Create a Kotlin property delegate that:
     * - Stores the current value locally (avoids unnecessary V8 round-trips for reads).
     * - Calls [set] on every write, keeping Vue's reactive ref in sync.
     *
     * @param key          The key used in `useData("key")` on the Vue side.
     * @param initialValue The default value (also pushed to Vue immediately so the ref
     *                     is never `undefined` on first render).
     */
    fun <T> prop(key: String, initialValue: T): ReadWriteProperty<VtReactiveData, T> =
        ReactiveProperty(key, initialValue)

    private inner class ReactiveProperty<T>(
        private val key: String,
        initialValue: T,
    ) : ReadWriteProperty<VtReactiveData, T> {

        private var backing: T = initialValue

        // Push the initial value so Vue never receives `undefined` on first render.
        init {
            set(key, initialValue)
        }

        override fun getValue(thisRef: VtReactiveData, property: KProperty<*>): T = backing

        override fun setValue(thisRef: VtReactiveData, property: KProperty<*>, value: T) {
            if (backing == value) return   // skip no-op writes
            backing = value
            set(key, value)
        }
    }
}

