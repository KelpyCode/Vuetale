package li.kelp.vuetale.style

import li.kelp.vuetale.property.Property
import li.kelp.vuetale.property.PropertyMap
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyOrigin
import li.kelp.vuetale.property.PropertyRef
import org.graalvm.polyglot.Value

class UnsupportedSelectorException(selector: String, why: String = "") : Exception("Unsupported selector: $selector" + (if(why.isNotEmpty()) " ($why)" else ""))

object StyleRegistry {
    val styles = mutableMapOf<String, Value>()

    val classProperties = mutableMapOf<String, MutableList<Property>>()

    fun registerStyle(name: String, classes: Value) {
        StyleRegistry.styles[name] = classes

        classes.memberKeys.forEach { selectorStr ->
            val selectors = selectorStr.split(",").map { it.trim() }
            val selectors2 = selectors.map {
                val entries = it.split(" ")
                if(entries.size > 1) {
                    throw UnsupportedSelectorException(it, "Deep selectors are not supported yet")
                }

                if(!entries[0].startsWith(".")) {
                    throw UnsupportedSelectorException(it, "Only class selectors are supported yet")
                }

                val content = classes.getMember(selectorStr)
                Pair(entries[0].substring(1), content)
            }

            selectors2.forEach { pair ->
                    val selectorStr = pair.first
                    val properties = pair.second
                // Init list if not exists
                if(!classProperties.containsKey(selectorStr)) {
                    classProperties[selectorStr] = mutableListOf<Property>()
                }


                properties.memberKeys.forEach { propertyName ->
                    val entries = styleToProperty(propertyName, properties.getMember(propertyName))
                    classProperties[selectorStr]?.addAll(entries)
                }

                mergeMapProperties(classProperties[selectorStr]!!)
            }

            println("STYLE:  $selectorStr: ${classes.getMember(selectorStr)}")
        }
    }

    fun mergeMapProperties(properties: MutableList<Property>) {
        // Merge properties with the same name, keeping the one with the highest origin, merge all sub properties if both are maps
        val grouped = properties.groupBy { it.name }

        val merged = grouped.map { (_, group) ->
            if (group.all { it is PropertyMap }) {
                // All entries are maps: merge their sub-properties, highest origin wins per sub-key
                val highestOrigin = group.maxOf { it.origin }
                val mergedMap = mutableMapOf<String, Property>()
                for (prop in group) {
                    val mapProp = prop as PropertyMap
                    for ((subKey, subProp) in mapProp.map) {
                        val existing = mergedMap[subKey]
                        if (existing == null || subProp.origin >= existing.origin) {
                            mergedMap[subKey] = subProp
                        }
                    }
                }
                PropertyMap(group.first().name, mergedMap).also { it.origin = highestOrigin }
            } else {
                // Not all maps: keep the entry with the highest origin
                group.maxByOrNull { it.origin }!!
            }
        }

        properties.clear()
        properties.addAll(merged)
    }

    fun styleToProperty(key: String, value: Value): List<Property> {



        class MapBuilder() {
            var map = PropertyMap("", mutableMapOf())

            fun name(name: String) {
                map.name = name
            }

            fun entry(key: String, value: Property) {
                map.map[key] = value
            }
        }

        fun map(mb: MapBuilder.() -> Unit) {

        }

        map {
            name("Anchor")
            entry("left", PropertyNumber("left", value.asString().toInt()))

        }

        return when(key) {
            "color" -> listOf(PropertyRef("Color", value.asString()))
            "anchorLeft" -> listOf(PropertyMap("Anchor", mutableMapOf("left" to PropertyNumber("left", value.asString().toInt()))))
            "anchorRight" -> listOf(PropertyMap("Anchor", mutableMapOf("right" to PropertyNumber("right", value.asString().toInt()))))
            else -> listOf()
        }
    }

    fun getPropertiesForClass(className: String): List<Property> {
        val entries = classProperties[className] ?: emptyList()

        return entries.map { it.clone().apply { origin = PropertyOrigin.Class } }
    }
}