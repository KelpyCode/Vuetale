package li.kelp.vuetale.property

import li.kelp.vuetale.tree.Element

abstract class Property(val element: Element) {
    abstract val name: String

    abstract fun getValue(): String

    open fun isValid(): Boolean {
        return true
    }
}