package li.kelp.vuetale.events

import li.kelp.vuetale.app.App
import li.kelp.vuetale.tree.Element

typealias ElementEvents = MutableMap<String, () -> Unit>

class EventRegistry(val app: App) {
    val elementEvents: MutableMap<String, ElementEvents> = mutableMapOf()

    fun registerElementEvent(element: Element) {

    }
}

