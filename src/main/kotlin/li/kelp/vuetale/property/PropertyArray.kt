package li.kelp.vuetale.property

class PropertyArray(name: String, var list: MutableList<Property> = mutableListOf()) : Property(name) {
    override fun render(): String? {
        if (this.varRef != null) return renderAsVariable()

        if (list.isEmpty()) {
            return null
        }

        val renderedProperties = list.mapNotNull { it.render() }
        if (renderedProperties.isEmpty()) {
            return null
        }
        return "$name: (${renderedProperties.joinToString(", ")})"
    }
}