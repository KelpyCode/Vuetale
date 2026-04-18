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
import java.util.concurrent.CompletableFuture
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
    lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss,
    /** Initial component to render, e.g. `"@core/pages/Dashboard"`. May be changed later via [App.navigateTo]. */
    componentPath: String? = null,
) : InteractiveCustomUIPage<VuetaleEventData>(playerRef, lifetime, VuetaleEventData.CODEC) {

    private val logger = Logger.getLogger("VuetaleUIPage[$appOwner-$appType]")

    /**
     * Set to false in [onDismiss] so any in-flight async sendUpdate dispatched from
     * [App.onDirty] is silently dropped rather than calling into a dismissed page.
     */
    @Volatile
    private var isActive = true

    /** The Vuetale app that owns the element tree for this page. */
    val app: App = run {
        val existingApp = AppManager.getApp(AppManager.getAppId(appOwner, appType))

        if (existingApp != null) {
            // Silence the dirty callback so the V8 tick doesn't fire onDirty while
            // we're in the middle of re-opening the page (deadlock risk, see below).
            existingApp.onDirty = null
            existingApp.isDirty = false

            // If a new component path was requested, navigate the live Vue app to it
            // instead of tearing down and recreating everything.
            if (componentPath != null && componentPath != existingApp.componentPath) {
                existingApp.navigateTo(componentPath)
            }

            existingApp
        } else {
            // No existing app – create a fresh one.
            // Note: we intentionally do NOT call removeApp here; if somehow a stale
            // entry existed it would have been caught by the branch above.
            AppManager.createApp(appOwner, appType, componentPath)
        }
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
        //    This lambda runs on the vuetale-v8 thread.
        //
        //    IMPORTANT: sendUpdate() must NEVER be called directly on the V8 thread.
        //    If sendUpdate() blocks (acquiring a Hytale player/page lock) and at the
        //    same time a second openCustomPage call on the game thread is waiting for
        //    the V8 thread via evalScript(), the two threads deadlock permanently.
        //    Dispatching via CompletableFuture.runAsync() keeps the V8 tick non-blocking.
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

            if (structuralChange || hasElementStructural) {
                // ── Full re-render ─────────────────────────────────────────
                // Targeted remove/insert is avoided because stale selectors
                // (e.g. after ErrorBoundary transitions) cause "element not found"
                // client disconnects.  A full clear+re-render is always safe.
                val cmdBuilder = UICommandBuilder()
                    .clear("#App")
                    .appendInline("#App", app.root.render(0))

                val evtBuilder = UIEventBuilder()
                registerEventBindings(evtBuilder)
                sendUpdateAsync(cmdBuilder, evtBuilder, false)
            } else if (dirtyIds.isNotEmpty()) {
                // ── Incremental property update ────────────────────────────
                val cmdBuilder = UICommandBuilder()
                var needsFallback = false

                for (rawId in dirtyIds) {
                    val element = Element.idElementMap[rawId]
                    if (element == null) {
                        continue
                    }
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
                    val cmdBuilder2 = UICommandBuilder()
                        .clear("#App")
                        .appendInline("#App", app.root.render(0))
                    val evtBuilder = UIEventBuilder()
                    registerEventBindings(evtBuilder)
                    sendUpdateAsync(cmdBuilder2, evtBuilder, false)
                } else {
                    sendUpdateAsync(cmdBuilder)
                }
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Dispatch a [sendUpdate] call asynchronously on the common ForkJoin pool.
     *
     * The [App.onDirty] callback fires on the `vuetale-v8` thread.  Calling [sendUpdate]
     * directly from that thread risks a deadlock: if [sendUpdate] acquires a Hytale
     * player/page lock and the game thread is simultaneously waiting for the V8 thread
     * via [JSEngine.evalScript] (e.g. during a second [build] call), both threads stall
     * forever.  Dispatching here keeps the V8 tick non-blocking.
     */
    private fun sendUpdateAsync(cmdBuilder: UICommandBuilder, evtBuilder: UIEventBuilder, lockInterface: Boolean) {
        CompletableFuture.runAsync {
            if (isActive) runCatching { sendUpdate(cmdBuilder, evtBuilder, lockInterface) }
        }
    }

    private fun sendUpdateAsync(cmdBuilder: UICommandBuilder) {
        CompletableFuture.runAsync {
            if (isActive) runCatching { sendUpdate(cmdBuilder, false) }
        }
    }

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
        // Prevent any in-flight async sendUpdate from reaching a dismissed page.
        isActive = false
        // Detach the dirty callback first to avoid any stray update after unmount
        app.onDirty = null

        // Only remove this specific App instance.  If the user opened the page a
        // second time before dismissing the first, the constructor will have already
        // replaced this app in AppManager with a new one.  Calling removeApp by
        // owner+type would then unmount the *new* app, killing the second session.
        if (AppManager.getApp(app.getId()) === app) {
            AppManager.removeApp(app.owner, app.type)
        }

        super.onDismiss(ref, store)
    }
}

