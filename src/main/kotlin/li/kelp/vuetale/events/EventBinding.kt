package li.kelp.vuetale.events

import com.caoccao.javet.values.reference.V8ValueFunction
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType

/**
 * Represents one Vue event handler registered on a Kotlin element
 * ([li.kelp.vuetale.tree.Element]).
 *
 * @param elementSelector CSS-style selector used by Hytale's UIEventBuilder, e.g. `"#vt_abc123"`.
 * @param bindingType     Hytale event type, e.g. [CustomUIEventBindingType.Activating].
 * @param callback        The live V8 function to invoke when the event fires.
 *                        Must be closed when the element is removed to avoid V8 memory leaks.
 * @param routingKey      Stable string `"<elementId>__<bindingTypeName>"` used to correlate
 *                        the [VuetaleEventData.routingKey] received in [handleDataEvent].
 */
data class EventBinding(
    val elementSelector: String,
    val bindingType: CustomUIEventBindingType,
    val callback: V8ValueFunction,
    val routingKey: String
)

