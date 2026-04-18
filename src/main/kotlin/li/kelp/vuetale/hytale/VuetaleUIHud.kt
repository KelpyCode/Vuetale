package li.kelp.vuetale.hytale

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import li.kelp.vuetale.app.App
import li.kelp.vuetale.app.AppManager
import li.kelp.vuetale.app.AppType
import li.kelp.vuetale.tree.Element
import li.kelp.vuetale.property.*
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger

/**
 * Hytale HUD backed by a Vue application.
 *
 * ### Lifecycle
 * 1. Construct – creates (or re-creates) an [App] for the given [appOwner] + [AppType.Hud].
 * 2. `build()` – Hytale calls this when the HUD is first shown via [show].
 *    - Mounts the Vue app so the element tree is synchronously populated.
 *    - Appends the static root layout via `uiCommandBuilder.append(rootUiPath)`.
 *    - Injects the rendered element tree into `#App` via `appendInline`.
 *    - Sets [App.onDirty] so subsequent Vue re-renders push incremental updates.
 * 3. `hide()` – cleans up the [App] and removes it from [AppManager].
 *
 * ### Thread safety
 * The [App.onDirty] callback fires on the **`vuetale-v8`** daemon thread.
 * `update()` is dispatched asynchronously via [CompletableFuture.runAsync] to avoid
 * deadlocks with the game thread.
 *
 * @param playerRef  The player this HUD belongs to.
 * @param appOwner   Stable identifier for the Vue app (typically the player name).
 */
class VuetaleUIHud(
    playerRef: PlayerRef,
    appOwner: String,
    /** Initial component to render, e.g. `"@core/huds/MyHud"`. */
    componentPath: String? = null,
) : CustomUIHud(playerRef) {

    private val logger = Logger.getLogger("VuetaleUIHud[$appOwner]")

    @Volatile
    private var isActive = true

    /** The Vuetale app that owns the element tree for this HUD. */
    val app: App = run {
        val existingApp = AppManager.getApp(AppManager.getAppId(appOwner, AppType.Hud))

        if (existingApp != null) {
            existingApp.onDirty = null
            existingApp.isDirty = false

            if (componentPath != null && componentPath != existingApp.componentPath) {
                existingApp.navigateTo(componentPath)
            }

            existingApp
        } else {
            AppManager.createApp(appOwner, AppType.Hud, componentPath)
        }
    }

    // ── build ──────────────────────────────────────────────────────────────

    override fun build(uiCommandBuilder: UICommandBuilder) {
        // 1. Mount Vue – synchronously populates the Kotlin element tree
        app.mount()

        // 2. Load the static root layout (defines group #App {})
        uiCommandBuilder.append("Common.ui")
        uiCommandBuilder.append("Pages/HytaleRoot.ui")

        // 3. Inject the entire rendered tree into #App
        val rendered = app.root.render(0)
        uiCommandBuilder.appendInline("#App", rendered)

        // Clear stale mount-time tracking (same reasoning as VuetaleUIPage)
        app.hasStructuralChanges = false
        app.dirtyElementIds.clear()
        app.removedElementSelectors.clear()
        app.insertedElements.clear()
        app.isDirty = false

        // 4. Wire up the dirty → incremental-update pipeline.
        //    Runs on the vuetale-v8 thread; dispatch update() asynchronously.
        app.onDirty = {
            val structuralChange = app.hasStructuralChanges
            val dirtyIds = app.dirtyElementIds.toSet()
            val removedSelectors = app.removedElementSelectors.toList()
            val insertedElements = app.insertedElements.toList()

            app.hasStructuralChanges = false
            app.dirtyElementIds.clear()
            app.removedElementSelectors.clear()
            app.insertedElements.clear()

            val hasElementStructural = removedSelectors.isNotEmpty() || insertedElements.isNotEmpty()

            if (structuralChange || hasElementStructural) {
                // ── Full re-render ─────────────────────────────────────────
                val cmdBuilder = UICommandBuilder()
                    .clear("#App")
                    .appendInline("#App", app.root.render(0))

                sendUpdateAsync(cmdBuilder, false)
            } else if (dirtyIds.isNotEmpty()) {
                // ── Incremental property update ────────────────────────────
                val cmdBuilder = UICommandBuilder()
                var needsFallback = false

                for (rawId in dirtyIds) {
                    val element = Element.idElementMap[rawId]
                    if (element == null) continue
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
                    val fallback = UICommandBuilder()
                        .clear("#App")
                        .appendInline("#App", app.root.render(0))
                    sendUpdateAsync(fallback, false)
                } else {
                    sendUpdateAsync(cmdBuilder, false)
                }
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Dispatch an [update] call asynchronously on the common ForkJoin pool to
     * keep the V8 tick non-blocking.
     */
    private fun sendUpdateAsync(cmdBuilder: UICommandBuilder, lockInterface: Boolean) {
        CompletableFuture.runAsync {
            if (isActive) runCatching { update(lockInterface, cmdBuilder) }
        }
    }

    /**
     * Emit a single `UICommandBuilder.set` command for [prop] on [elementSelector].
     *
     * Returns `true` if handled, `false` if unsupported (caller should fall back
     * to a full re-render).
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
                val v = prop.value
                if (v != null) builder.set(path, v) else builder.setNull(path)
                true
            }

            is PropertyRecord -> {
                prop.map.values.all { subProp ->
                    emitPropertySet(builder, "$elementSelector.${prop.name}", subProp)
                }
            }

            else -> false
        }
    }

    // ── hide ───────────────────────────────────────────────────────────────

    /**
     * Stop the HUD, silence the dirty callback, and remove the app from [AppManager].
     * Call this when the HUD should no longer be active for the player.
     */
    fun hide() {
        isActive = false
        app.onDirty = null

        if (AppManager.getApp(app.getId()) === app) {
            AppManager.removeApp(app.owner, app.type)
        }
    }
}

