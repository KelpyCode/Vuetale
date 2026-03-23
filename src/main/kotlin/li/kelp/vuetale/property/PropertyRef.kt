package li.kelp.vuetale.property

class PropertyRef(name: String, var value: String?): Property(name) {
    override fun render(): String? {
        if(value == null) {
            return null
        }

        return "$name: $value"
    }
}