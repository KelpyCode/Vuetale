package li.kelp.vuetale.util

import li.kelp.vuetale.app.App
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.tree.GroupElement

object RenderUtil {
    fun indent(depth: Int): String {
        return "  ".repeat(depth)
    }

    fun renderImports(app: App): String {
        var render = ""

        app.dependencies.forEach {
            render += "\$${it.value.name}: \"${it.value.origin}\";\n"
        }

        return render
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

        if (element is GroupElement) {
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
        var selector = if (element.app != null && element.varFrom != null && element.varName != null) {
            val dep = element.app!!.getDependencyName(element.varFrom!!)
            "\$$dep.@${element.varName}"
        } else element.tag


        selector += " #${element.customId ?: element.id}"
        return selector
    }
}