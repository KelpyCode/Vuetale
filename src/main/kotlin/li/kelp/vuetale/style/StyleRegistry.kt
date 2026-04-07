package li.kelp.vuetale.style

import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.reference.V8ValueObject
import li.kelp.vuetale.javascript.asKtString
import li.kelp.vuetale.javascript.memberKeys
import li.kelp.vuetale.property.Property
import li.kelp.vuetale.property.PropertyEnum
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyOrigin
import li.kelp.vuetale.property.PropertyRecord

class UnsupportedSelectorException(selector: String, why: String = "") :
    Exception("Unsupported selector: $selector" + (if (why.isNotEmpty()) " ($why)" else ""))

object StyleRegistry {
    // Raw style values are not stored long-term as V8Values; all processing
    // happens eagerly inside registerStyle so no V8 references leak.
    val classProperties = mutableMapOf<String, MutableList<Property>>()

    fun registerStyle(name: String, classes: V8ValueObject) {
        classes.memberKeys().forEach { selectorStr ->
            val selectors = selectorStr.split(",").map { it.trim() }
            val selectors2 = selectors.map {
                val entries = it.split(" ")
                if (entries.size > 1) {
                    throw UnsupportedSelectorException(it, "Deep selectors are not supported yet")
                }
                if (!entries[0].startsWith(".")) {
                    throw UnsupportedSelectorException(it, "Only class selectors are supported yet")
                }
                classes.get<V8ValueObject>(selectorStr).use { content ->
                    Pair(entries[0].substring(1), content.memberKeys().associateWith { propKey ->
                        content.get<V8Value>(propKey).use { v -> v.asKtString() }
                    })
                }
            }

            selectors2.forEach { (selectorName, properties) ->
                if (!classProperties.containsKey(selectorName)) {
                    classProperties[selectorName] = mutableListOf()
                }
                properties.forEach { (propName, propValue) ->
                    val entries = styleToProperty(propName, propValue)
                    classProperties[selectorName]?.addAll(entries)
                }
                mergeMapProperties(classProperties[selectorName]!!)
            }

            println("STYLE:  $selectorStr")
        }
    }

    fun mergeMapProperties(properties: MutableList<Property>) {
        val grouped = properties.groupBy { it.name }
        val merged = grouped.map { (_, group) ->
            if (group.all { it is PropertyRecord }) {
                val highestOrigin = group.maxOf { it.origin }
                val mergedMap = mutableMapOf<String, Property>()
                for (prop in group) {
                    val mapProp = prop as PropertyRecord
                    for ((subKey, subProp) in mapProp.map) {
                        val existing = mergedMap[subKey]
                        if (existing == null || subProp.origin >= existing.origin) {
                            mergedMap[subKey] = subProp
                        }
                    }
                }
                PropertyRecord(group.first().name, mergedMap).also { it.origin = highestOrigin }
            } else {
                group.maxByOrNull { it.origin }!!
            }
        }
        properties.clear()
        properties.addAll(merged)
    }

    /** Convert a single CSS-style property name/value pair to [Property] instances. */
    fun styleToProperty(key: String, value: String): List<Property> {
        return when (key) {
            "color" -> listOf(PropertyEnum("Color", value))
            "anchorLeft" -> listOf(
                PropertyRecord("Anchor", mutableMapOf("left" to PropertyNumber("left", value.toInt())))
            )
            "anchorRight" -> listOf(
                PropertyRecord("Anchor", mutableMapOf("right" to PropertyNumber("right", value.toInt())))
            )
            else -> listOf()
        }
    }

    fun getPropertiesForClass(className: String): List<Property> {
        return (classProperties[className] ?: emptyList())
            .map { it.clone().apply { origin = PropertyOrigin.Class } }
    }
}