package li.kelp.vuetale.property

class PropertyEnum(name: String, var value: String?) : Property(name) {
    override fun render(): String? {
        this.varRef ?: return renderAsVariable()
        
        if (value == null) {
            return null
        }

        return "$name: $value"
    }
}