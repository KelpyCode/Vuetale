package li.kelp.vuetale.property

class PropertyBoolean(name: String, var value: Boolean?) : Property(name) {
    override fun toString(): String {
        return "(Boolean) $name: ${render()}"
    }

    override fun render(): String? {
        if (value == null) {
            return null
        }

        val valueString = if (value == true) "true" else "false"

        return "$name: $valueString"
    }
}