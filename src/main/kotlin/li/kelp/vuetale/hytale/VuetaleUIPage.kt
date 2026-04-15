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
import li.kelp.vuetale.events.EventBinding
import li.kelp.vuetale.javascript.JSEngine
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

        // 5. Wire up the dirty → incremental-update pipeline.
        //    This lambda runs on the vuetale-v8 thread; wrap with world.execute{} if needed.
        app.onDirty = {
            val cmdBuilder = UICommandBuilder()
                .clear("#App")
                .appendInline("#App", app.root.render(0))
            val evtBuilder = UIEventBuilder()
            registerEventBindings(evtBuilder)
            sendUpdate(cmdBuilder, evtBuilder, false)
        }
    }

    /**
     * Iterate all live [EventBinding]s and add them to [uiEventBuilder].
     *
     * The [EventData] encodes two keys the client will capture and send back:
     * - `"routingKey"` – a static discriminator (`"<rawId>__<typeName>"`) used to route to
     *   the correct Vue callback in [handleDataEvent].
     * - `"value"` – the element's current `.Value` property (blank for activation events).
     */
    private fun registerEventBindings(uiEventBuilder: UIEventBuilder) {
        for (binding in app.eventRegistry.getAllBindings()) {
            uiEventBuilder.addEventBinding(
                binding.bindingType,
                binding.elementSelector,
                EventData.of("RoutingKey", binding.routingKey)
                    .append("Value", "${binding.elementSelector}.Value"),
                false  // locksInterface: don't lock other UI interaction
            )
        }
    }

    // ── handleDataEvent ────────────────────────────────────────────────────

    override fun handleDataEvent(
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        data: VuetaleEventData
    ) {
        super.handleDataEvent(ref, store, data)

        val binding = app.eventRegistry.findByRoutingKey(data.routingKey)
        if (binding != null) {
            // Invoke the Vue event handler on the V8 thread
            JSEngine.instance.runOnV8Thread {
                runCatching {
                    // Pass the element value as the first argument so Vue handlers can receive it
                    binding.callback.callVoid(null, data.value)
                }.onFailure {
                    logger.warning("Error invoking callback for '${data.routingKey}': ${it.message}")
                }
            }
        } else {
            logger.warning("No binding found for routingKey='${data.routingKey}'")
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




