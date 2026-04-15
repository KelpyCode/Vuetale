package li.kelp.vuetale.tree

import li.kelp.vuetale.util.RenderUtil

class RootElement() : ElementContainer("root") {
    override fun render(depth: Int): String {
        return /*RenderUtil.renderImports(app!!) +*/ children.joinToString("\n\n") { it.render(depth) }
    }
}