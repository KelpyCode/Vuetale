package li.kelp.vuetale.validator

import li.kelp.vuetale.tree.Element

object PropertyValidator {
    val supportedPropertyPerType: MutableMap<Class<out Element>, List<String>> = mutableMapOf()

    fun Element.canHavePropertyOld(property: String): Boolean {
        val supportedProperties = supportedPropertyPerType[this.javaClass] ?: return false
        return supportedProperties.contains(property)
    }

    fun Element.getSupportedProperties(): List<String> {
        return supportedPropertyPerType[this.javaClass] ?: emptyList()
    }

    fun getElementTypeByName(name: String): Class<out Element>? {
        return supportedPropertyPerType.keys.firstOrNull { it.name == name }
    }

    fun registerProperties(element: Class<out Element>, properties: List<String>) {
        supportedPropertyPerType[element] = properties
    }
}