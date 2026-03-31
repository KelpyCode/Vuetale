package li.kelp.vuetale.property

enum class PropertyOrigin {
    Default,
    Class,
    Style,
    Attribute
}

abstract class Property(var name: String, var origin: PropertyOrigin = PropertyOrigin.Default) {
    var varRef: String? = null

    open fun isValid(): Boolean {
        return true
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
            is PropertyMap -> PropertyMap(name, map.mapValues { it.value.clone() } as MutableMap<String, Property>)
            else -> throw IllegalStateException("Unknown property type: ${this::class}")
        }
    }
}