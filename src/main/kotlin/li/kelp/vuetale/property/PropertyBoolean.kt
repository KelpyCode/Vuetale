package li.kelp.vuetale.property

class PropertyBoolean(name: String, var value: Boolean?) : Property(name) {
    override fun render(): String? {
        this.varRef ?: return renderAsVariable()

        if (value == null) {
            return null
        }

        val valueString = if (value == true) "True" else "False"

        return "$name: $valueString"
    }
}