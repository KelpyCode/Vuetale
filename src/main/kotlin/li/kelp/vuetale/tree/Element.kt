package li.kelp.vuetale.tree

abstract class Element(val tag: String) {
    companion object {
        val idElementMap = mutableMapOf<String, Element>()

        fun generateId(): String {
            // Randomly generate an ID, check if it already exists, and if so, generate a new one
            var id: String
            do {
                id = List(12) { ('a'..'z') + ('0'..'9') }.flatten().random().toString()
            } while (idElementMap.containsKey(id))
            return id
        }
    }

    abstract val elementType: String
    val id: String
    var parent: ElementContainer? = null

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