package li.kelp.vuetale.property

import li.kelp.vuetale.tree.Element

abstract class Property(val name: String) {

    open fun isValid(): Boolean {
        return true
    }

    abstract fun render(): String?
}