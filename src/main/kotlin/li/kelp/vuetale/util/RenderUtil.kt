package li.kelp.vuetale.util

import li.kelp.vuetale.app.App
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.ElementContainer

object RenderUtil {
    fun indent(depth: Int): String {
        return "  ".repeat(depth)
    }

    fun simpleElementRender(element: Element, depth: Int): String {
        var render = indent(depth)

        var selector = renderSelector(element)

        render += "$selector {\n"

        val properties = element.renderProperties(depth + 1)
        if (!properties.isEmpty()) {
            render += properties

            render += "\n"
        }

        if (element is ElementContainer) {
            val childrenRendered = element.children?.mapNotNull { it.render(depth + 1) }
            if (childrenRendered != null) {
                render += childrenRendered.joinToString("\n")
            }
        }

        render += indent(depth)
        render += "}\n"

        return render
    }

    private fun renderSelector(element: Element): String {
        var selector = element.tag


        selector += " #${element.getId()}"
        return selector
    }
}