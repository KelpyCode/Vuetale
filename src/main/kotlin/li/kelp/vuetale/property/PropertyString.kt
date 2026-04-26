package li.kelp.vuetale.property

class PropertyString(name: String, var value: String?) : Property(name) {
    override fun toString(): String {
        return "(String) $name: ${render()}"
    }

    fun escapeValue(): String? {

        if (value == null) {
            return null
        }

        return value!!.replace("\"", "\\\"")
    }

    override fun render(): String? {
        if (value == null) {
            return null
        }

        return "$name: \"${escapeValue()}\""
    }
}