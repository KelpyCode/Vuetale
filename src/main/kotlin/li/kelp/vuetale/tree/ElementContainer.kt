package li.kelp.vuetale.tree

abstract class ElementContainer(tag: String) : Element(tag) {
    var children: MutableList<Element> = mutableListOf()


    fun removeChild(child: Element) {
        children.remove(child)
        child.parent = null
    }

    fun removeChild(id: String) {
        val child = children.find { it.id == id }
        if (child != null) {
            removeChild(child)
        }
    }

    fun removeChild(index: Int) {
        if (index in children.indices) {
            val child = children[index]
            removeChild(child)
        }
    }

    fun clearChildren() {
        for (child in children) {
            child.parent = null
        }
        children.clear()
    }

    fun appendChild(child: Element) {
        val oldParent = child.parent
        if (oldParent is ElementContainer) {
            if (oldParent == this && children.contains(child)) {
                // Already a child of this container, no action needed
                return
            }
            oldParent.children.remove(child)
        }
        children.add(child)
        child.parent = this
    }

    fun prependChild(child: Element) {
        child.appendTo(this)
        children.remove(child)
        children.add(0, child)
    }

    fun insertChild(child: Element, index: Int) {
        child.appendTo(this)
        children.remove(child)
        children.add(index, child)
    }
}