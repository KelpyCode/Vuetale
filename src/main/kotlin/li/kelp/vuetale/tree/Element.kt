package li.kelp.vuetale.tree

import li.kelp.vuetale.app.App
import li.kelp.vuetale.property.Property
import li.kelp.vuetale.util.RenderUtil.indent
import li.kelp.vuetale.util.RenderUtil.simpleElementRender
import li.kelp.vuetale.validator.canHaveProperty

abstract class Element(var tag: String) {

    companion object {
        val idElementMap = mutableMapOf<String, Element>()

        fun generateId(): String {
            // Randomly generate an ID, check if it already exists, and if so, generate a new one
            var id: String
            do {
                val chars = ('a'..'z') + ('0'..'9')
                id = List(12) { chars.random() }.joinToString("")
            } while (idElementMap.containsKey(id))
            return "vt_" + id
        }

        val supportedProperties = setOf<String>()

        fun findElementClassByTag(tag: String): Class<out Element>? {
            return elementTagMap[tag.lowercase().replace("-", "")]
        }
    }

    open fun render(depth: Int): String {
        return simpleElementRender(this, depth)
    }

    fun renderProperties(depth: Int): String {
        if (properties.isEmpty()) {
            return ""
        }
        return properties.mapNotNull { indent(depth) + it.value.render() }.joinToString(";\n") + ";\n"
    }

    val id: String
    var app: App? = null
    var customId: String? = null
    var parent: ElementContainer? = null
    var properties: MutableMap<String, Property> = mutableMapOf()

    var varFrom: String? = null
    var varName: String? = null

    fun setPropertySafe(name: String, property: Property) {
        if (this.canHaveProperty(name)) {
            properties[name] = property
        } else {
            throw IllegalArgumentException("Property '$name' is not supported for element type '$tag'.")
        }
    }

    init {
        id = generateId()
        idElementMap[id] = this
    }

    fun detachFromParent() {
        parent?.let {
            it.removeChild(this)
        }
    }

    fun appendTo(newParent: ElementContainer) {
        detachFromParent()
        newParent.appendChild(this)
        parent = newParent
    }

    fun prependTo(newParent: ElementContainer) {
        detachFromParent()
        newParent.prependChild(this)
        parent = newParent


    }

    fun insertTo(newParent: ElementContainer, index: Int) {
        detachFromParent()
        newParent.insertChild(this, index)
        parent = newParent
    }

    fun remove() {
        detachFromParent()
        idElementMap.remove(id)
    }
}