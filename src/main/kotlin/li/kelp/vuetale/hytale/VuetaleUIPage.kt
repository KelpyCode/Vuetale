package li.kelp.vuetale.hytale

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.ui.builder.EventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import li.kelp.vuetale.app.App
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.app.AppType
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import li.kelp.vuetale.javascript.JSEngine
import li.kelp.vuetale.property.*
import li.kelp.vuetale.tree.Element
import java.util.logging.Logger

/**
 * Hytale UI page backed by a Vue application.
 *
 * ### Lifecycle
 * 1. Construct – creates (or re-creates) an [App] for the given [appOwner] + [appType].
 * 2. `build()` – Hytale calls this when the screen opens.
 *    - Mounts the Vue app so the element tree is synchronously populated.
 *    - Appends the static root layout via `uiCommandBuilder.append(rootUiPath)`.
 *    - Injects the rendered element tree into `#App` via `appendInline`.
 *    - Registers all collected event bindings with `uiEventBuilder`.
 *    - Sets [App.onDirty] so subsequent Vue re-renders push incremental updates.
 * 3. `handleDataEvent()` – Hytale calls this when a registered UI event fires.
 *    - Looks up the Vue callback by [VuetaleEventData.routingKey].
 *    - Invokes the callback on the V8 thread via [JSEngine].
 * 4. `onDismiss()` – cleans up the [App] and removes it from [AppManager].
 *
 * ### Thread safety
 * The [App.onDirty] callback fires on the **`vuetale-v8`** daemon thread.  If Hytale's
 * `sendUpdate()` must be called from the game/server thread, wrap the call inside
 * `world.execute { … }` (or equivalent) before assigning [App.onDirty].
 *
 * @param playerRef   The player this page belongs to.
 * @param appOwner    Stable identifier for the Vue app (typically the player name).
 * @param appType     [AppType.Page] or [AppType.Hud].
 * @param lifetime    When the player may dismiss the screen.
 * @param rootUiPath  Path to the static `.ui` file that contains the `#App` container.
 *                    Resolved by Hytale's asset system (adjust to match your mod's pack layout).
 */
class VuetaleUIPage(
    playerRef: PlayerRef,
    appOwner: String,
    appType: AppType = AppType.Page,
    lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss
) : InteractiveCustomUIPage<VuetaleEventData>(playerRef, lifetime, VuetaleEventData.CODEC) {

    private val logger = Logger.getLogger("VuetaleUIPage[$appOwner-$appType]")

    /** The Vuetale app that owns the element tree for this page. */
    val app: App = run {
        // Defensively remove a stale app (e.g. from a previous session)
        AppManager.removeApp(appOwner, appType)
        AppManager.createApp(appOwner, appType)
    }

    // ── build ──────────────────────────────────────────────────────────────

    override fun build(
        ref: Ref<EntityStore>,
        uiCommandBuilder: UICommandBuilder,
        uiEventBuilder: UIEventBuilder,
        store: Store<EntityStore>
    ) {
        // 1. Mount Vue – synchronously populates the Kotlin element tree
        app.mount()

        // 2. Load the static root layout (defines group #App {})
        uiCommandBuilder.append("Common.ui")
        uiCommandBuilder.append("Pages/HytaleRoot.ui")

        // 3. Inject the entire rendered tree into #App
        val rendered = app.root.render(0) // strip newlines to avoid Hytale parser issues
        uiCommandBuilder.appendInline("#App", rendered)

        // 4. Register all Vue event bindings collected during mount
        registerEventBindings(uiEventBuilder)

        // Clear stale mount-time tracking: all inserts/patches during mount are already
        // covered by the appendInline above.  If we leave these lists populated, the first
        // onDirty invocation would re-process mount-time data and emit bogus structural
        // commands referencing element IDs that may no longer exist in the client's UI.
        app.hasStructuralChanges = false
        app.dirtyElementIds.clear()
        app.removedElementSelectors.clear()
        app.insertedElements.clear()
        app.isDirty = false

        // 5. Wire up the dirty → incremental-update pipeline.
        //    This lambda runs on the vuetale-v8 thread
        app.onDirty = {
            val structuralChange = app.hasStructuralChanges
            val dirtyIds = app.dirtyElementIds.toSet()
            val removedSelectors = app.removedElementSelectors.toList()
            val insertedElements = app.insertedElements.toList()

            // Reset tracking state before building the update so any mutations that
            // fire during sendUpdate are captured for the *next* batch.
            app.hasStructuralChanges = false
            app.dirtyElementIds.clear()
            app.removedElementSelectors.clear()
            app.insertedElements.clear()

            val hasElementStructural = removedSelectors.isNotEmpty() || insertedElements.isNotEmpty()

            if (structuralChange) {
                // ── Full re-render ─────────────────────────────────────────
                // Required when a property was removed (set to null/undefined in Vue),
                // which cannot be expressed as a targeted set command.
                val cmdBuilder = UICommandBuilder()
                    .clear("#App")
                    .appendInline("#App", app.root.render(0))
                val evtBuilder = UIEventBuilder()
                registerEventBindings(evtBuilder)
                sendUpdate(cmdBuilder, evtBuilder, false)
            } else if (hasElementStructural) {
                // ── Targeted element insert / remove ───────────────────────
                // Elements were added or removed (e.g. v-if toggled).  Emit
                // targeted remove/appendInline commands so unrelated elements
                // (like a focused textbox) are not destroyed and re-created.
                val cmdBuilder = UICommandBuilder()
                for (selector in removedSelectors) {
                    cmdBuilder.remove(selector)
                }

                // Vue inserts children into their parent *before* the parent is
                // inserted into the grandparent, so insertedElements is ordered
                // children-first.  Emitting appendInline in that order would
                // reference parent IDs that don't yet exist in the client DOM.
                //
                // Fix: only emit appendInline for *root* insertions – elements
                // whose parentSelector is NOT itself a newly-inserted element.
                // GroupElement.render() already includes all descendants
                // recursively, so a single root appendInline delivers the entire
                // subtree; all deeper entries in insertedElements are redundant.
                val insertedSelectors = insertedElements
                    .map { it.child.buildUniqueSelector() }
                    .toHashSet()
                for (ins in insertedElements) {
                    if (ins.parentSelector !in insertedSelectors) {
                        cmdBuilder.appendInline(ins.parentSelector, ins.child.render(0))
                    }
                }

                // Re-register events because newly inserted elements may have bindings.
                val evtBuilder = UIEventBuilder()
                registerEventBindings(evtBuilder)
                sendUpdate(cmdBuilder, evtBuilder, false)
            } else if (dirtyIds.isNotEmpty()) {
                // ── Incremental property update ────────────────────────────
                // Only scalar/record properties changed – no elements were
                // added or removed.  Emit targeted set commands so the client
                // never destroys the focused TextField.
                val cmdBuilder = UICommandBuilder()
                var needsFallback = false

                for (rawId in dirtyIds) {
                    val element = Element.idElementMap[rawId]
                    if (element == null) {
                        // Element was removed between markDirty and onDirty – safe to skip.
                        continue
                    }
                    // Use the unique parent-chain selector so elements with custom IDs
                    // (e.g. #Title, #Content) are targeted unambiguously even when the
                    // same component is mounted multiple times on one page.
                    val selector = element.buildUniqueSelector()
                    for (prop in element.properties.values) {
                        if (!emitPropertySet(cmdBuilder, selector, prop)) {
                            needsFallback = true
                            break
                        }
                    }
                    if (needsFallback) break
                }

                if (needsFallback) {
                    // An unsupported property type was encountered – fall back to full re-render.
                    val cmdBuilder2 = UICommandBuilder()
                        .clear("#App")
                        .appendInline("#App", app.root.render(0))
                    val evtBuilder = UIEventBuilder()
                    registerEventBindings(evtBuilder)
                    sendUpdate(cmdBuilder2, evtBuilder, false)
                } else {
                    // No event re-registration needed: element IDs and routing keys are stable.
                    sendUpdate(cmdBuilder, false)
                }
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private fun registerEventBindings(uiEventBuilder: UIEventBuilder) {
        for (binding in app.eventRegistry.getAllBindings()) {
            // Only ValueChanged carries a Value payload – all other event types
            // (Activating, RightClicking, …) have no Value property on the element
            // and Hytale crashes with "Could not gather property value" if we request it.
            val eventData = EventData.of("RoutingKey", binding.routingKey).let {
                if (binding.bindingType == CustomUIEventBindingType.ValueChanged)
                    it.append("@Value", "${binding.elementSelector}.Value")
                else it
            }
            uiEventBuilder.addEventBinding(
                binding.bindingType,
                binding.elementSelector,
                eventData,
                false
            )
        }
    }

    /**
     * Emit a single `UICommandBuilder.set` command for [prop] on [elementSelector].
     *
     * Returns `true` if the property was handled, `false` if the type is unsupported
     * and the caller should fall back to a full re-render.
     *
     * [PropertyRecord] is handled recursively using dotted-path selectors
     * (e.g. `#id.Anchor.Left`).
     */
    private fun emitPropertySet(
        builder: UICommandBuilder,
        elementSelector: String,
        prop: Property
    ): Boolean {
        val path = "$elementSelector.${prop.name}"
        return when (prop) {
            is PropertyString -> {
                val v = prop.value
                if (v != null) builder.set(path, v) else builder.setNull(path)
                true
            }

            is PropertyNumber -> {
                val v = prop.value
                when {
                    v == null -> builder.setNull(path)
                    v is Int -> builder.set(path, v)
                    v is Float -> builder.set(path, v)
                    v is Double -> builder.set(path, v)
                    else -> builder.set(path, v.toDouble())
                }
                true
            }

            is PropertyBoolean -> {
                val v = prop.value
                if (v != null) builder.set(path, v) else builder.setNull(path)
                true
            }

            is PropertyEnum -> {
                // Enum values are plain identifier strings (e.g. "Center", "Top")
                val v = prop.value
                if (v != null) builder.set(path, v) else builder.setNull(path)
                true
            }

            is PropertyRecord -> {
                // Recurse: emit one set command per sub-property with a dotted path
                prop.map.values.all { subProp ->
                    emitPropertySet(builder, "$elementSelector.${prop.name}", subProp)
                }
            }

            else -> false  // unknown type – trigger fallback
        }
    }

    // ── handleDataEvent ────────────────────────────────────────────────────

    override fun handleDataEvent(
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        data: VuetaleEventData
    ) {
        super.handleDataEvent(ref, store, data)

        // Capture only plain values here – never capture the V8ValueFunction itself outside
        // the V8 thread, because a concurrent hot-reload may close the old runtime and clear
        // the event registry between this line and the task actually executing on the V8 thread.
        val routingKey = data.routingKey
        val value = data.value

        JSEngine.instance.runOnV8Thread {
            // Re-fetch the binding *inside* the V8 task so we always use the live reference.
            // If a hot-reload fired in the meantime, forceReset() will have cleared the
            // registry and this returns null – skipping callVoid before touching the closed runtime.
            val liveBinding = app.eventRegistry.findByRoutingKey(routingKey)
            if (liveBinding == null) {
                logger.warning("No binding found for routingKey='$routingKey' (may be stale after hot-reload)")
                return@runOnV8Thread
            }
            runCatching {
                liveBinding.callback.callVoid(null, value)
            }.onFailure {
                logger.warning("Error invoking callback for '$routingKey': ${it.message}")
            }
        }

        // Acknowledge the event so Hytale does not consider the page stale
        sendUpdate()
    }

    // ── onDismiss ──────────────────────────────────────────────────────────

    override fun onDismiss(ref: Ref<EntityStore>, store: Store<EntityStore>) {
        // Detach the dirty callback first to avoid any stray update after unmount
        app.onDirty = null
        AppManager.removeApp(app.owner, app.type)
        super.onDismiss(ref, store)
    }
}

