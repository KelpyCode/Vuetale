package li.kelp.vuetale.util

import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.GroupElement

object RenderUtil {
    fun indent(depth: Int): String {
        return "  ".repeat(depth)
    }

    fun simpleElementRender(element: Element, depth: Int): String {
        var render = indent(depth)
        val properties = element.renderProperties(depth + 1)

        var selector = "${element.elementType}"
        selector += " #${element.id}"

        render += "$selector {\n"

        if(!properties.isEmpty()) {
            render += properties

            render += "\n"
        }

        if(element is GroupElement) {
            val childrenRendered = element.children?.mapNotNull { it.render(depth + 1) }
            if(childrenRendered != null) {
                render += childrenRendered.joinToString("\n")
            }
        }

        render += indent(depth)
        render += "}\n"

        return render
    }
}