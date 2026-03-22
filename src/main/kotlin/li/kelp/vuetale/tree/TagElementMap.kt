package li.kelp.vuetale.tree

import kotlin.reflect.KClass


val TagElementMap: Map<String, KClass<out Element>> = mapOf(
    "div" to Element::class,
    "group" to Element::class
)
