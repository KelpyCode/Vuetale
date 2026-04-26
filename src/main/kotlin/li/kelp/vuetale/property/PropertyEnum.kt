package li.kelp.vuetale.property

class PropertyEnum(name: String, var value: String?) : Property(name) {
    override fun toString(): String {
        return "(Enum) $name: ${render()}"
    }

    override fun render(): String? {
        if (value == null) {
            return null
        }

        return "$name: $value"
    }
}