package li.kelp.vuetale.util

object StringUtil {
    fun String.fromPascalCaseToKebabCase(): String {
        return this.split("(?=[A-Z])".toRegex()).joinToString("-") { it.lowercase() }
    }

    fun String.fromKebabCaseToPascalCase(): String {
        return this.split("-").joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
    }

    fun String.fromPascalCaseToCamelCase(): String {
        if (this.isEmpty()) {
            return this
        }
        return this[0].lowercase() + this.substring(1)
    }

    fun String.capitalize(): String {
        if (this.isEmpty()) {
            return this
        }
        return this[0].uppercase() + this.substring(1)
    }

}