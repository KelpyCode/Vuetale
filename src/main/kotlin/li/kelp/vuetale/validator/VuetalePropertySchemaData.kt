package li.kelp.vuetale.validator

import com.caoccao.javet.values.V8Value
import com.caoccao.javet.values.primitive.V8ValueBoolean
import com.caoccao.javet.values.primitive.V8ValueString
import li.kelp.vuetale.javascript.asKtBoolean
import li.kelp.vuetale.javascript.asKtDouble
import li.kelp.vuetale.javascript.asKtInt
import li.kelp.vuetale.javascript.asKtString
import li.kelp.vuetale.javascript.isNullOrUndefined
import li.kelp.vuetale.tree.Element

// ── Types ──────────────────────────────────────────────────────────────────────

enum class VuetalePropertyType {
    Boolean,
    String,
    Number,
}

data class VuetalePropertyDeclaration(
    val name: String,
    val type: VuetalePropertyType,
)

// ── Schema ─────────────────────────────────────────────────────────────────────

class VuetaleSchema {
    val properties = mutableMapOf<String, VuetalePropertyDeclaration>()

    fun property(name: String, type: VuetalePropertyType) {
        properties[name] = VuetalePropertyDeclaration(name, type)
    }

    /** Returns true when [name] is a registered Vuetale property (starts with Vt). */
    fun isVuetaleProperty(name: String): Boolean = properties.containsKey(name)

    /**
     * Parse a raw V8 value into the appropriate Kotlin type for this property.
     * Returns `null` when [value] is null/undefined (meaning "remove" / reset to default).
     */
    fun parse(name: String, value: V8Value): Any? {
        if (value.isNullOrUndefined) return null
        val decl = properties[name] ?: return null
        return when (decl.type) {
            VuetalePropertyType.Boolean -> when (value) {
                is V8ValueBoolean -> value.value
                is V8ValueString -> value.value.trim().lowercase() == "true"
                else -> value.asKtBoolean()
            }

            VuetalePropertyType.Number -> value.asKtDouble()
            else -> {}
        }
    }
}

fun buildVuetaleSchema(builder: VuetaleSchema.() -> Unit): VuetaleSchema {
    val schema = VuetaleSchema()
    builder(schema)
    return schema
}

// ── Global schema instance ─────────────────────────────────────────────────────

val vtPropertySchema: VuetaleSchema = buildVuetaleSchema {
    // When true, the element (and its subtree) will not be marked dirty and
    // therefore will not trigger incremental UI updates while the flag is set.
    // Useful for elements whose props are updated frequently server-side but
    // whose visual state should be controlled exclusively by client input
    // (e.g. a focused TextField whose Value is bound two-way).
    property("VtSkipUpdate", VuetalePropertyType.Boolean)
}

// ── Element helpers ────────────────────────────────────────────────────────────

/**
 * Returns `true` when this element **or any of its ancestors** has `VtSkipUpdate` set to `true`.
 *
 * Checking the parent chain is necessary because Vue's fallthrough-attr mechanism applies
 * component-level props (like `vt-skip-update`) to the component's *root* element, which is
 * often a wrapper Group rather than the inner element whose property is actually changing.
 * Walking upward means `<Common.TextField vt-skip-update="true" />` correctly suppresses
 * dirty-marking on the inner `<TextField>` even though the flag lives on the outer Group.
 */
fun Element.isVtSkipUpdate(): Boolean {
    var current: Element? = this
    while (current != null) {
        if (current.vuetaleProperties["VtSkipUpdate"] == true) return true
        current = current.parent
    }
    return false
}





