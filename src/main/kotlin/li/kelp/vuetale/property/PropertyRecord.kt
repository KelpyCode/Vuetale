package li.kelp.vuetale.property

class PropertyRecord(name: String, var map: MutableMap<String, Property> = mutableMapOf()) : Property(name) {
    override fun render(): String? {
        if (this.varRef != null) return renderAsVariable()

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