package li.kelp.vuetale.events

import com.caoccao.javet.values.reference.V8ValueFunction
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import li.kelp.vuetale.app.App
import li.kelp.vuetale.tree.Element

/**
 * Holds all live Vue event bindings for a single [App].
 *
 * Lifecycle:
 * - [registerEvent] is called from [li.kelp.vuetale.javascript.VueBridge.patchProp] whenever
 *   Vue sets an `on*` prop (e.g. `onActivating`).
 * - [unregisterEvents] is called when an element is removed via [li.kelp.vuetale.javascript.VueBridge.remove].
 * - [getAllBindings] / [findByRoutingKey] are queried by [li.kelp.vuetale.hytale.VuetaleUIPage]
 *   during [build] and [handleDataEvent].
 */
class EventRegistry(val app: App) {

    // elementId (raw, no #) -> (bindingType.name -> EventBinding)
    private val bindings: MutableMap<String, MutableMap<String, EventBinding>> = mutableMapOf()

    // routingKey -> EventBinding  (secondary index for O(1) handleDataEvent lookup)
    private val routingIndex: MutableMap<String, EventBinding> = mutableMapOf()

    // ── Registration ───────────────────────────────────────────────────────

    /**
     * Register (or replace) a handler for [element] + [bindingType].
     * The previous [V8ValueFunction] for the same slot, if any, is closed.
     */
    fun registerEvent(
        element: Element,
        bindingType: CustomUIEventBindingType,
        callback: V8ValueFunction
    ) {
        val rawId = element.getId()
        val selector = "#$rawId"
        val routingKey = "${rawId}__${bindingType.name}"

        val byElement = bindings.getOrPut(rawId) { mutableMapOf() }

        // Close the old V8 handle to avoid leaks
        byElement[bindingType.name]?.let { old ->
            routingIndex.remove(old.routingKey)
            runCatching { old.callback.close() }
        }

        val binding = EventBinding(
            elementSelector = selector,
            bindingType = bindingType,
            callback = callback,
            routingKey = routingKey
        )
        byElement[bindingType.name] = binding
        routingIndex[routingKey] = binding
    }

    /**
     * Remove and close all bindings registered for [rawElementId] (no `#` prefix).
     * Called when an element is removed from the Vue tree.
     */
    fun unregisterEvents(rawElementId: String) {
        bindings.remove(rawElementId)?.values?.forEach { binding ->
            routingIndex.remove(binding.routingKey)
            runCatching { binding.callback.close() }
        }
    }

    // ── Queries ────────────────────────────────────────────────────────────

    /** Returns every currently registered [EventBinding] across all elements. */
    fun getAllBindings(): List<EventBinding> = bindings.values.flatMap { it.values }

    /**
     * Look up the binding that matches a [routingKey] received from the client.
     * The routing key has the format `"<rawElementId>__<bindingTypeName>"`.
     */
    fun findByRoutingKey(routingKey: String): EventBinding? = routingIndex[routingKey]

    // ── Cleanup ────────────────────────────────────────────────────────────

    /** Close all V8 callbacks (called when the App is destroyed). */
    fun closeAll() {
        bindings.values.flatMap { it.values }.forEach { runCatching { it.callback.close() } }
        bindings.clear()
        routingIndex.clear()
    }
}