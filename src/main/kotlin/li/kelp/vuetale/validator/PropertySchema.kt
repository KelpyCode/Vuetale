package li.kelp.vuetale.validator

import li.kelp.vuetale.property.Property
import li.kelp.vuetale.property.PropertyArray
import li.kelp.vuetale.property.PropertyBoolean
import li.kelp.vuetale.property.PropertyEnum
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyRecord
import li.kelp.vuetale.property.PropertyString
import li.kelp.vuetale.tree.Element
import org.graalvm.polyglot.Value


enum class PropertyType {
    Ref,
    RefArray,
    RefOrString,
    String,
    ColorString,
    Number,
    Boolean,
    Record,
    Array
}

data class SchemaField(
    val name: String,
    val type: PropertyType = PropertyType.Ref,
    var ref: String? = null,
)

abstract class SchemaDeclaration {}
abstract class SchemaValue {}
data class SchemaSingleValueRef<T>(val value: String) : SchemaValue()
data class SchemaArrayValueRef<T>(val value: String) : SchemaValue()


data class SchemaInterfaceDeclaration(
    val name: String,
    val fields: List<SchemaField>
) : SchemaDeclaration()

data class SchemaEnumDeclaration(
    val name: String,
    val values: List<String>
) : SchemaDeclaration()

data class SchemaElementDeclaration(
    val name: String,
    val fields: List<SchemaField>
) : SchemaDeclaration() {

}

class Schema {
    val enums = mutableMapOf<String, SchemaEnumDeclaration>()
    val interfaces = mutableMapOf<String, SchemaInterfaceDeclaration>()
    val elements = mutableMapOf<String, SchemaElementDeclaration>()

    fun enum(name: String, values: List<String>) {
        enums[name] = SchemaEnumDeclaration(name, values)
    }

    fun type(name: String, fields: List<SchemaField>) {
        interfaces[name] = SchemaInterfaceDeclaration(name, fields)
    }

    fun element(name: String, fields: List<SchemaField>) {
        elements[name] = SchemaElementDeclaration(name, fields)
    }

    fun execute(element: String, property: String, value: Value): Property? {
        val declaration = elements[element] ?: throw IllegalArgumentException("Element '$element' not found in schema.")
        return execute(declaration, property, value)
    }

    private fun executeInterface(declaration: SchemaInterfaceDeclaration, propertyName: String, value: Value): PropertyRecord {
        val map = mutableMapOf<String, Property>()
        for (subField in declaration.fields) {
            if (!value.hasMember(subField.name)) continue
            val subValue = value.getMember(subField.name)
            execute(declaration, subField.name, subValue)?.let { map[subField.name] = it }
        }
        return PropertyRecord(propertyName, map)
    }

    fun execute(declaration: SchemaDeclaration, property: String, value: Value): Property? {
        if (value.isNull) return null

        when (declaration) {
            is SchemaEnumDeclaration -> {
                val enumValue = value.asString()
                if (!declaration.values.contains(enumValue)) {
                    throw IllegalArgumentException(
                        "Invalid enum value '$enumValue' for enum '${declaration.name}'. Allowed values: ${
                            declaration.values.joinToString(
                                ", "
                            )
                        }"
                    )
                }
                return PropertyEnum(property, enumValue)
            }

            else -> {
                var field = when (declaration) {
                    is SchemaElementDeclaration -> declaration.fields.find { it.name == property }
                    is SchemaInterfaceDeclaration -> declaration.fields.find { it.name == property }
                    else -> null
                }

                when (field?.type) {
                    PropertyType.ColorString -> return PropertyString(property, value.asString())
                    PropertyType.Record -> return PropertyRecord(property, value.asProxyObject())
                    PropertyType.String -> return PropertyString(property, value.asString())
                    PropertyType.Number -> return PropertyNumber(property, value.asInt())
                    PropertyType.Boolean -> return PropertyBoolean(property, value.asBoolean())
                    PropertyType.Ref -> {
                        val ref = field.ref!!

                        if (enums[ref] != null) {
                            return execute(enums[ref]!!, property, value)
                        } else {
                            return executeInterface(interfaces[ref]!!, property, value)
                        }
                    }

                    PropertyType.RefArray -> {
                        val ref = field.ref!!

                        val list = mutableListOf<Property>()
                        val size = value.arraySize
                        for (i in 0 until size) {
                            val item = value.getArrayElement(i)
                            list.add(executeInterface(interfaces[ref]!!, property, item))
                        }

                        return PropertyArray(property, list)
                    }

                    PropertyType.RefOrString -> {
                        val ref = field.ref!!

                        if (value.isString) {
                            return PropertyString(property, value.asString())
                        } else {
                            return executeInterface(interfaces[ref]!!, property, value)
                        }
                    }

                    else -> throw IllegalArgumentException("Unsupported property type: ${field?.type} (Setting property '$property' in declaration '${declaration::class.simpleName}')")
                }
            }
        }

        throw IllegalArgumentException("Unsupported declaration type: ${declaration::class.simpleName}")
    }

}

fun Element.canHaveProperty(property: String): Boolean {

    val declaration = propertySchema!!.elements[this.tag] ?: return false
    return when (declaration) {
        is SchemaElementDeclaration -> declaration.fields.any { it.name == property }
        is SchemaInterfaceDeclaration -> declaration.fields.any { it.name == property }
        else -> false
    }
}

fun Element.executeProperty(property: String, value: Value): Property? {
    val declaration =
        propertySchema!!.elements[this.tag]
            ?: throw IllegalArgumentException("Element '${this.tag}' not found in schema.")
    return propertySchema!!.execute(declaration, property, value)
}


fun buildSchema(builder: Schema.() -> Unit): Schema {
    val schema = Schema()
    builder(schema)
    return schema
}
