package li.kelp.vuetale.property

class PropertyNumber(name: String, var value: Number?): Property(name) {
    override fun render(): String? {
        if(value == null) {
            return null
        }

        return "$name: $value"
    }
}