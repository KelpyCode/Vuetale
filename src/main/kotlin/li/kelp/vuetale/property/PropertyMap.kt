package li.kelp.vuetale.property

class PropertyMap(name: String, var map: Map<String, Property> = mapOf()): Property(name) {
    override fun render(): String? {
        if(map.isEmpty()) {
            return null
        }

        val renderedProperties = map.values.mapNotNull { it.render() }
        if(renderedProperties.isEmpty()) {
            return null
        }
        return "$name: (${renderedProperties.joinToString(", ")})"
    }
}