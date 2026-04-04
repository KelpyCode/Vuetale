package li.kelp.vuetale.style

import li.kelp.vuetale.property.Property
import li.kelp.vuetale.property.PropertyRecord
import li.kelp.vuetale.property.PropertyNumber

object StyleHandler {
    val styleGenerator: MutableMap<String, (v: String) -> Property> = mutableMapOf()

    init {

        class MapBuilder() {
            var map = PropertyRecord("", mutableMapOf())

            fun name(name: String) {
                map.name = name
            }

            fun entry(key: String, value: (key: String, value: String) -> Property) {
                // map.map[key] = value
            }
        }

        fun map(mb: MapBuilder.() -> Unit) {
            val instance = MapBuilder()
            mb(instance)
        }

        // ------------------

        map {
            name("Anchor")
            entry("Left") { key, value -> PropertyNumber(key, value.toInt()) }
            entry("Right") { key, value -> PropertyNumber(key, value.toInt()) }
            entry("Top") { key, value -> PropertyNumber(key, value.toInt()) }
            entry("Down") { key, value -> PropertyNumber(key, value.toInt()) }
        }
    }


}