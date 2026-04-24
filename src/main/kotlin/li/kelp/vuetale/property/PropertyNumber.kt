package li.kelp.vuetale.property

/**
 * Property that represents numeric values with smart formatting:
 * - Whole numbers are displayed without decimals (e.g., "42", not "42.0")
 * - Decimal numbers preserve precision with trailing zeros removed (e.g., "1.5", "3.14159")
 */
class PropertyNumber(name: String, var value: Number?) : Property(name) {
    override fun render(): String? {
        if (value == null) {
            return null
        }

        val doubleValue = value!!.toDouble()
        val formattedValue = if (doubleValue == doubleValue.toLong().toDouble()) {
            // It's a whole number, display without decimals
            doubleValue.toLong().toString()
        } else {
            // It's a decimal, format with appropriate precision and remove trailing zeros
            val formatted = String.format("%.15f", doubleValue)
                .trimEnd('0')
                .trimEnd('.')
            formatted
        }

        return "$name: $formattedValue"
    }
}