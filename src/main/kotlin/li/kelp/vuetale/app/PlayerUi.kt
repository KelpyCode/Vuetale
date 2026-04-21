package li.kelp.vuetale.app

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import li.kelp.vuetale.hytale.VuetaleUIHud
import li.kelp.vuetale.hytale.VuetaleUIPage
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

/**
 * High-level handle for all Vuetale UI belonging to a single player.
 *
 * Obtain via [PlayerUiManager.get] and call helpers like [openPage], [navigate], [closePage].
 * All methods that touch Hytale's page/HUD APIs dispatch to [CompletableFuture.runAsync] so
 * callers do not need to worry about threading.
 */
class PlayerUi internal constructor(
    val uuid: UUID,
    /** Stable string owner key passed to every [App] – defaults to the player UUID string. */
    val ownerId: String = uuid.toString(),
) {
    private val logger = Logger.getLogger("PlayerUi[$ownerId]")

    // Live references to the Hytale UI objects – set by PlayerUiManager helpers.
    internal var pageRef: Ref<EntityStore>? = null
    internal var pageStore: Store<EntityStore>? = null
    internal var playerRef: PlayerRef? = null

    /** Currently open page instance, if any. */
    var page: VuetaleUIPage? = null
        internal set(value) {
            field = value
            if (value != null) flushPendingPageData(value.app)
        }

    /** Currently active HUD instance, if any. */
    var hud: VuetaleUIHud? = null
        internal set(value) {
            field = value
            if (value != null) flushPendingHudData(value.app)
        }

    // Buffers for setData/setHudData calls that arrive before the page/hud app is created.
    private val pendingPageData: MutableMap<String, Any?> = LinkedHashMap()
    private val pendingHudData: MutableMap<String, Any?> = LinkedHashMap()

    private fun flushPendingPageData(app: App) {
        if (pendingPageData.isEmpty()) return
        pendingPageData.forEach { (k, v) -> app.setData(k, v) }
        pendingPageData.clear()
    }

    private fun flushPendingHudData(app: App) {
        if (pendingHudData.isEmpty()) return
        pendingHudData.forEach { (k, v) -> app.setData(k, v) }
        pendingHudData.clear()
    }

    // ── Page API ───────────────────────────────────────────────────────────

    /**
     * Open (or replace) the player's fullscreen page.
     *
     * @param componentPath  Module path of the Vue component to show, e.g. `"vt:@core/pages/Dashboard"`.
     * @param lifetime       When the player may dismiss the screen (default: [CustomPageLifetime.CanDismiss]).
     */
    fun openPage(
        componentPath: String,
        lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss,
    ): PlayerUi {
        val pRef = requirePlayerRef()
        val (ref, store, player) = requirePlayerContext()
        CompletableFuture.runAsync {
            val newPage = VuetaleUIPage(pRef, ownerId, AppType.Page, lifetime, componentPath)
            page = newPage
            player.pageManager.openCustomPage(ref, store, newPage)
        }
        return this
    }

    /**
     * Navigate the already-open page to a different component without unmounting/remounting.
     * If no page is mounted this is a no-op (consider [openPage] instead).
     */
    fun navigate(componentPath: String) {
        val currentPage = page ?: run {
            logger.warning("navigate() called but no page is currently open for $ownerId")
            return
        }
        currentPage.app.navigateTo(componentPath)
    }

    /**
     * Cleans up the Vue app for the current page.
     * Note: Hytale page dismissal is driven by the page's own `onDismiss` lifecycle.
     * Calling this from server code (e.g. to force-close) should be combined with
     * whatever server-side mechanism you use to navigate the player away from the page.
     */
    fun closePage() {
        page?.app?.let { app ->
            if (app.isMounted) app.unmount()
        }
        page = null
    }

    // ── HUD API ────────────────────────────────────────────────────────────

    /**
     * Show (or replace) the player's HUD.
     *
     * @param componentPath  Module path of the Vue component, e.g. `"vt:@core/huds/ActionBar"`.
     */
    fun openHud(componentPath: String): PlayerUi {
        val (ref, store, player) = requirePlayerContext()
        CompletableFuture.runAsync {
            val newHud = VuetaleUIHud(requirePlayerRef(), ownerId, componentPath)
            hud = newHud
            player.hudManager.setCustomHud(requirePlayerRef(), newHud)
        }
        return this
    }

    /** Navigate the active HUD to a different component without hiding it. */
    fun navigateHud(componentPath: String) {
        hud?.app?.navigateTo(componentPath)
            ?: logger.warning("navigateHud() called but no HUD is active for $ownerId")
    }

    /** Hide the current HUD, if any. */
    fun closeHud() {
        val h = hud ?: return
        val (ref, store, player) = requirePlayerContext()
        CompletableFuture.runAsync {
            h.hide()
            player.hudManager.resetHud(requirePlayerRef())
        }
        hud = null
    }

    // ── Data API ───────────────────────────────────────────────────────────

    /**
     * Push a reactive data value to the Vue side for the currently open page.
     * If the page is not yet open (e.g. called right after [openPage] before the async
     * completes), the value is buffered and flushed automatically once the app is ready.
     *
     * @param key   The string key used in `useData("key")` on the Vue side.
     * @param value Any JSON-serialisable JVM value (String, Number, Boolean, data class, null).
     */
    fun setData(key: String, value: Any?) {
        val app = page?.app
        if (app != null) {
            app.setData(key, value)
        } else {
            pendingPageData[key] = value
        }
    }

    /**
     * Push a reactive data value to the Vue side for the currently active HUD.
     * Buffered if the HUD is not yet active.
     */
    fun setHudData(key: String, value: Any?) {
        val app = hud?.app
        if (app != null) {
            app.setData(key, value)
        } else {
            pendingHudData[key] = value
        }
    }

    // ── Internal ───────────────────────────────────────────────────────────

    private fun requirePlayerRef(): PlayerRef =
        playerRef ?: error("PlayerUi[$ownerId] has no PlayerRef – was it registered via PlayerUiManager?")

    private data class PlayerContext(val ref: Ref<EntityStore>, val store: Store<EntityStore>, val player: Player)

    private fun requirePlayerContext(): PlayerContext {
        val ref = pageRef ?: error("PlayerUi[$ownerId] has no entity Ref")
        val store = pageStore ?: error("PlayerUi[$ownerId] has no entity Store")
        val player = store.getComponent(ref, Player.getComponentType())
            ?: error("PlayerUi[$ownerId]: Player component not found")
        return PlayerContext(ref, store, player)
    }
}

/**
 * Singleton manager for per-player [PlayerUi] handles.
 *
 * ### Typical usage (inside a command or event handler)
 * ```kotlin
 * val ui = PlayerUiManager.getOrCreate(playerRef.uuid, playerRef, ref, store)
 * ui.openPage("vt:@core/pages/Dashboard")
 * ```
 */
object PlayerUiManager {
    private val logger = Logger.getLogger("PlayerUiManager")
    private val playerUis: ConcurrentHashMap<UUID, PlayerUi> = ConcurrentHashMap()

    /**
     * Get the [PlayerUi] for [uuid], creating one if it does not exist.
     * Providing [playerRef], [ref] and [store] updates the stored Hytale context
     * so subsequent API calls can dispatch to Hytale correctly.
     */
    fun getOrCreate(
        uuid: UUID,
        playerRef: PlayerRef? = null,
        ref: Ref<EntityStore>? = null,
        store: Store<EntityStore>? = null,
    ): PlayerUi {
        val ui = playerUis.getOrPut(uuid) { PlayerUi(uuid) }
        if (playerRef != null) ui.playerRef = playerRef
        if (ref != null) ui.pageRef = ref
        if (store != null) ui.pageStore = store
        return ui
    }

    /** Get an existing [PlayerUi] without creating one. Returns `null` if the player is unknown. */
    fun get(uuid: UUID): PlayerUi? = playerUis[uuid]

    /** Remove a player's [PlayerUi] entry (call on logout / session end). */
    fun remove(uuid: UUID) {
        playerUis.remove(uuid)
    }

    // ── Convenience shortcuts ──────────────────────────────────────────────

    /**
     * Open a fullscreen page for the player, creating the [PlayerUi] if needed.
     *
     * ```kotlin
     * PlayerUiManager.openPage(playerRef, ref, store, "vt:@core/pages/Shop")
     * ```
     */
    fun openPage(
        playerRef: PlayerRef,
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        componentPath: String,
        lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss,
    ): PlayerUi {
        return getOrCreate(playerRef.uuid, playerRef, ref, store).openPage(componentPath, lifetime)
    }

    fun openPage(
        playerRef: PlayerRef,
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        module: String,
        page: String,
        lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss,
    ): PlayerUi {
        var componentPath = "@$module/pages/$page"
        if (!componentPath.endsWith(".vue.js")) {
            componentPath += ".vue.js"
        }

        return getOrCreate(playerRef.uuid, playerRef, ref, store).openPage(componentPath, lifetime)
    }

    /** Show a HUD for the player, creating the [PlayerUi] if needed. */
    fun openHud(
        playerRef: PlayerRef,
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        componentPath: String,
    ): PlayerUi {
        return getOrCreate(playerRef.uuid, playerRef, ref, store).openHud(componentPath)
    }

    fun openHud(
        playerRef: PlayerRef,
        ref: Ref<EntityStore>,
        store: Store<EntityStore>,
        module: String,
        hud: String,
    ) {
        var componentPath = "@$module/huds/$hud"
        if (!componentPath.endsWith(".vue.js")) {
            componentPath += ".vue.js"
        }

        getOrCreate(playerRef.uuid, playerRef, ref, store).openHud(componentPath)
    }
}