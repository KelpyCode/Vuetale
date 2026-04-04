package li.kelp.vuetale.property

enum class PropertyType {
    String,
    Number,
    Boolean,
    Enum,
    Map
}

enum class PropertyOrigin {
    Default,
    Class,
    Style,
    Attribute
}

abstract class Property(var name: String, var origin: PropertyOrigin = PropertyOrigin.Default) {
    var varRef: String? = null
    var parent: PropertyRecord? = null

    open fun isValid(): Boolean {
        return true
    }

    fun getPropertyPath(): String {
        val paths = mutableListOf<String>()
        var current: Property? = this
        while (current != null) {
            paths.add(current.name)
            current = current.parent
        }
        return paths.asReversed().joinToString(".")
    }

    abstract fun render(): String?

    fun renderAsVariable(): String {
        return "@$name"
    }

    open fun clone(): Property {
        return when (this) {
            is PropertyString -> PropertyString(name, value)
            is PropertyNumber -> PropertyNumber(name, value)
            is PropertyBoolean -> PropertyBoolean(name, value)
            is PropertyEnum -> PropertyEnum(name, value)
            is PropertyRecord -> PropertyRecord(
                name,
                map.mapValues { it.value.clone() } as MutableMap<String, Property>)

            else -> throw IllegalStateException("Unknown property type: ${this::class}")
        }
    }
}