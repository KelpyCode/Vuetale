package li.kelp.vuetale.tree

import li.kelp.vuetale.property.Property
import li.kelp.vuetale.util.RenderUtil.indent
import li.kelp.vuetale.util.RenderUtil.simpleElementRender
import org.graalvm.polyglot.Value

abstract class Element(val tag: String) {
    @JvmField
    var _vnode: Value = Value.asValue(null) // Placeholder for the virtual node representation

    @JvmField
    var __vue_app__: Value = Value.asValue(null) // Placeholder for the virtual node representation

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
    }

    open fun render(depth: Int): String {
        return simpleElementRender(this, depth)
    }

    fun renderProperties(depth: Int): String {
        if(properties.isEmpty()) {
            return ""
        }
        return properties.mapNotNull { indent(depth) + it.value.render() }.joinToString(";\n") + ";\n"
    }

    abstract val elementType: String
    val id: String
    var parent: ElementContainer? = null

    var properties = mutableMapOf<String, Property>()

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