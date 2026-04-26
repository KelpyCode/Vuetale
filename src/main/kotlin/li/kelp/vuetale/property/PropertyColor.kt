package li.kelp.vuetale.property

class PropertyColor(name: String, var value: String?) : Property(name) {
    override fun toString(): String {
        return "(Color) $name: ${render()}"
    }

    override fun render(): String? {
        if (value == null) {
            return null
        }

        return "$name: $value"
    }
}