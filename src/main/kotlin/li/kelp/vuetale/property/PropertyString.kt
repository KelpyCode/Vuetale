package li.kelp.vuetale.property

class PropertyString(name: String, var value: String?) : Property(name) {

    fun escapeValue(): String? {
        this.varRef ?: return renderAsVariable()

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