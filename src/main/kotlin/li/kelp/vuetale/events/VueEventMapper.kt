package li.kelp.vuetale.events

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType

/**
 * Maps Vue prop keys (e.g. `"onActivating"`) to their Hytale [CustomUIEventBindingType].
 *
 * Vue passes event handlers as `on<EventName>` props (camelCase). This object lower-cases
 * the suffix and looks it up in [map]. Returns `null` for any non-event prop.
 */
object VueEventMapper {

    private val map: Map<String, CustomUIEventBindingType> = mapOf(
        "activating"                    to CustomUIEventBindingType.Activating,
        "rightclicking"                 to CustomUIEventBindingType.RightClicking,
        "doubleclicking"                to CustomUIEventBindingType.DoubleClicking,
        "mouseentered"                  to CustomUIEventBindingType.MouseEntered,
        "mouseexited"                   to CustomUIEventBindingType.MouseExited,
        "valuechanged"                  to CustomUIEventBindingType.ValueChanged,
        "elementreordered"              to CustomUIEventBindingType.ElementReordered,
        "validating"                    to CustomUIEventBindingType.Validating,
        "dismissing"                    to CustomUIEventBindingType.Dismissing,
        "focusgained"                   to CustomUIEventBindingType.FocusGained,
        "focuslost"                     to CustomUIEventBindingType.FocusLost,
        "keydown"                       to CustomUIEventBindingType.KeyDown,
        "mousebuttonreleased"           to CustomUIEventBindingType.MouseButtonReleased,
        "slotclicking"                  to CustomUIEventBindingType.SlotClicking,
        "slotdoubleclicking"            to CustomUIEventBindingType.SlotDoubleClicking,
        "slotmouseentered"              to CustomUIEventBindingType.SlotMouseEntered,
        "slotmouseexited"               to CustomUIEventBindingType.SlotMouseExited,
        "dragcancelled"                 to CustomUIEventBindingType.DragCancelled,
        "dropped"                       to CustomUIEventBindingType.Dropped,
        "slotmousedragcompleted"        to CustomUIEventBindingType.SlotMouseDragCompleted,
        "slotmousedragexited"           to CustomUIEventBindingType.SlotMouseDragExited,
        "slotclickreleasewhiledragging" to CustomUIEventBindingType.SlotClickReleaseWhileDragging,
        "slotclickpresswhiledragging"   to CustomUIEventBindingType.SlotClickPressWhileDragging,
        "selectedtabchanged"            to CustomUIEventBindingType.SelectedTabChanged,
    )

    /**
     * Maps a Vue prop key like `"onActivating"` → [CustomUIEventBindingType.Activating].
     * Returns `null` if [vuePropKey] does not start with `"on"` or the suffix is not recognized.
     */
    fun map(vuePropKey: String): CustomUIEventBindingType? {
        if (!vuePropKey.startsWith("on")) return null
        val suffix = vuePropKey.removePrefix("on").lowercase()
        return map[suffix]
    }
}
