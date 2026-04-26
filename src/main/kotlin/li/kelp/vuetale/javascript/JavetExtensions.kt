package li.kelp.vuetale.javascript

import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.*
import com.caoccao.javet.values.reference.V8ValueArray
import com.caoccao.javet.values.reference.V8ValueObject

/** Returns `true` if this value is JavaScript `null` or `undefined`. */
val V8Value.isNullOrUndefined: Boolean
    get() = this is V8ValueNull || this is V8ValueUndefined

/**
 * Convert any JS primitive to a Kotlin [String], mirroring GraalVM's `Value.asString()`.
 * Throws for non-primitive types; callers should guard with type checks first.
 */
fun V8Value.asKtString(): String = when (this) {
    is V8ValueString -> value
    is V8ValueInteger -> value.toString()
    is V8ValueDouble -> value.toString()
    is V8ValueBoolean -> value.toString()
    is V8ValueLong -> value.toString()
    else -> throw IllegalArgumentException("Cannot convert ${javaClass.simpleName} to String")
}

/**
 * Convert any JS number-like value to a Kotlin [Int], mirroring GraalVM's `Value.asInt()`.
 */
fun V8Value.asKtInt(): Int = when (this) {
    is V8ValueInteger -> value
    is V8ValueDouble -> value.toInt()
    is V8ValueLong -> value.toInt()
    is V8ValueString -> value.toInt()
    else -> throw IllegalArgumentException("Cannot convert ${javaClass.simpleName} to Int")
}

/**
 * Convert any JS number-like value to a Kotlin [Double], preserving decimal precision.
 */
fun V8Value.asKtDouble(): Double = when (this) {
    is V8ValueInteger -> value.toDouble()
    is V8ValueDouble -> value
    is V8ValueLong -> value.toDouble()
    is V8ValueString -> value.toDouble()
    else -> throw IllegalArgumentException("Cannot convert ${javaClass.simpleName} to Double")
}

/**
 * Convert any JS boolean-like value to a Kotlin [Boolean].
 */
fun V8Value.asKtBoolean(): Boolean = when (this) {
    is V8ValueBoolean -> value
    else -> throw IllegalArgumentException("Cannot convert ${javaClass.simpleName} to Boolean")
}

/**
 * Returns the own enumerable property names of a JS object as a plain [List<String>].
 * Mirrors GraalVM's `Value.memberKeys`.
 * All temporary [V8Value] objects are closed inside this call.
 */
fun V8ValueObject.memberKeys(): List<String> {
    val result = mutableListOf<String>()
    getOwnPropertyNames().use { names ->
        val len = names.length
        for (i in 0 until len) {
            names.get<V8ValueString>(i).use { nameVal ->
                result.add(nameVal.value)
            }
        }
    }
    return result
}

/**
 * Safely get a member from this object, returning `null` if the property is
 * absent or is `undefined`.
 */
fun <T : V8Value> V8ValueObject.getMemberOrNull(key: String): T? {
    val v = get<V8Value>(key)
    return if (v == null || v is V8ValueUndefined || v is V8ValueNull) {
        v?.close()
        null
    } else {
        @Suppress("UNCHECKED_CAST")
        v as? T
    }
}


