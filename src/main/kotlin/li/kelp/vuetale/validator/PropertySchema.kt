package li.kelp.vuetale.validator

import li.kelp.vuetale.javascript.asKtBoolean
import li.kelp.vuetale.javascript.asKtInt
import li.kelp.vuetale.javascript.asKtDouble
import li.kelp.vuetale.javascript.asKtString
import li.kelp.vuetale.javascript.isNullOrUndefined
import li.kelp.vuetale.javascript.memberKeys
import li.kelp.vuetale.property.Property
import li.kelp.vuetale.property.PropertyArray
import li.kelp.vuetale.property.PropertyBoolean
import li.kelp.vuetale.property.PropertyEnum
import li.kelp.vuetale.property.PropertyNumber
import li.kelp.vuetale.property.PropertyRecord
import li.kelp.vuetale.property.PropertyString
import li.kelp.vuetale.tree.Element
import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.V8ValueString
import com.caoccao.javet.values.reference.V8ValueArray
import com.caoccao.javet.values.reference.V8ValueObject
import li.kelp.vuetale.property.PropertyColor


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

    fun execute(element: String, property: String, value: V8Value): Property? {
        val declaration = elements[element] ?: throw IllegalArgumentException("Element '$element' not found in schema.")
        return execute(declaration, property, value)
    }

    private fun executeInterface(
        declaration: SchemaInterfaceDeclaration,
        propertyName: String,
        value: V8Value
    ): PropertyRecord {
        val map = mutableMapOf<String, Property>()
        val obj = value as V8ValueObject
        for (subField in declaration.fields) {
            if (!obj.has(subField.name)) continue
            obj.get<V8Value>(subField.name).use { subValue ->
                execute(declaration, subField.name, subValue)?.let { map[subField.name] = it }
            }
        }
        return PropertyRecord(propertyName, map)
    }

    fun execute(declaration: SchemaDeclaration, property: String, value: V8Value): Property? {
        if (value.isNullOrUndefined) return null

        when (declaration) {
            is SchemaEnumDeclaration -> {
                val enumValue = value.asKtString()
                if (!declaration.values.contains(enumValue)) {
                    throw IllegalArgumentException(
                        "Invalid enum value '$enumValue' for enum '${declaration.name}'. Allowed values: ${
                            declaration.values.joinToString(", ")
                        }"
                    )
                }
                return PropertyEnum(property, enumValue)
            }

            else -> {
                val field = when (declaration) {
                    is SchemaElementDeclaration -> declaration.fields.find { it.name == property }
                    is SchemaInterfaceDeclaration -> declaration.fields.find { it.name == property }
                    else -> null
                }

                return when (field?.type) {
                    PropertyType.ColorString -> PropertyColor(property, value.asKtString())
                    PropertyType.Record -> PropertyRecord(property, mutableMapOf())
                    PropertyType.String -> PropertyString(property, value.asKtString())
                    PropertyType.Number -> PropertyNumber(property, value.asKtDouble())
                    PropertyType.Boolean -> PropertyBoolean(property, value.asKtBoolean())
                    PropertyType.Ref -> {
                        val ref = field.ref!!
                        if (enums[ref] != null) execute(enums[ref]!!, property, value)
                        else executeInterface(interfaces[ref]!!, property, value)
                    }

                    PropertyType.RefArray -> {
                        val ref = field.ref!!
                        val array = value as V8ValueArray
                        val list = (0 until array.length).map { i ->
                            array.get<V8ValueObject>(i).use { item ->
                                executeInterface(interfaces[ref]!!, property, item)
                            }
                        }.toMutableList<Property>()
                        PropertyArray(property, list)
                    }

                    PropertyType.RefOrString -> {
                        val ref = field.ref!!
                        if (value is V8ValueString) PropertyString(property, value.value)
                        else executeInterface(interfaces[ref]!!, property, value)
                    }

                    else -> throw IllegalArgumentException(
                        "Unsupported property type: ${field?.type} (property '$property' in '${declaration::class.simpleName}')"
                    )
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

fun Element.executeProperty(property: String, value: V8Value): Property? {
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
