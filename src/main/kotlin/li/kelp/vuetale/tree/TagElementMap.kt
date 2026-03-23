package li.kelp.vuetale.tree

import java.util.Locale
import java.util.Locale.getDefault
import kotlin.reflect.KClass


val TagElementMap: Map<String, KClass<out Element>> = mapOf(
    "div" to GroupElement::class,
    "group" to GroupElement::class,

    "label" to LabelElement::class,
    "p" to LabelElement::class,
    "span" to LabelElement::class,
)

fun getElementClassForTag(tag: String): KClass<out Element>? {
    return TagElementMap[tag.lowercase()]
}