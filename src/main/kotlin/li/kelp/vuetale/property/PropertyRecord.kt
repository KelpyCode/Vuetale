package li.kelp.vuetale.property

class PropertyRecord(name: String, var map: MutableMap<String, Property> = mutableMapOf()) : Property(name) {

    override fun toString(): String {
        return "(Record) $name: ${render()}"
    }


    override fun render(): String? {
        if (map.isEmpty()) {
            return null
        }

        val renderedProperties = map.values.mapNotNull { it.render() }
        if (renderedProperties.isEmpty()) {
            return null
        }
        return "$name: (${renderedProperties.joinToString(", ")})"
    }
}