package li.kelp.vuetale.hytale

import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.codecs.simple.StringCodec

/**
 * Event payload sent from the Hytale client back to the server when a UI event fires.
 *
 * Fields
 * ------
 * - [routingKey] — Stable key of the form `"<rawElementId>__<bindingTypeName>"` (e.g.
 *   `"vt_abc123__Activating"`).  Written into
 *   [EventData][com.hypixel.hytale.server.core.ui.builder.EventData] by `VuetaleUIPage`
 *   when registering each binding, and used in `handleDataEvent` to look up the correct
 *   Vue callback.
 * - [value]      — Current value of the element that fired the event (empty string for
 *   activation events that carry no value).
 */
class VuetaleEventData {
    var routingKey: String = ""
    var value: String = ""

    companion object {
        private val STRING = StringCodec()

        @JvmField
        val CODEC: BuilderCodec<VuetaleEventData> =
            BuilderCodec.builder(VuetaleEventData::class.java) { VuetaleEventData() }
                .append(
                    KeyedCodec("RoutingKey", STRING),
                    { d, v: String? -> d.routingKey = v ?: "" },
                    { d -> d.routingKey }
                ).add()
                .append(
                    KeyedCodec("Value", STRING),
                    { d, v: String? -> d.value = v ?: "" },
                    { d -> d.value }
                ).add()
                .build()
    }
}


