package li.kelp.vuetale.tree

class RootElement() : ElementContainer("root") {
    override fun render(depth: Int): String {
        return children.joinToString("\n\n") { it.render(depth) }
    }

    override val elementType = "Root"
}