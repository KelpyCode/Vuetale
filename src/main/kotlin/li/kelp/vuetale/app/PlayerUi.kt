package li.kelp.vuetale.app

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.protocol.packets.interface_.Page
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import li.kelp.vuetale.helpers.toWorld
import li.kelp.vuetale.hytale.VuetaleUIHud
import li.kelp.vuetale.hytale.VuetaleUIPage
import li.kelp.vuetale.javascript.JSEngine
import java.time.Instant
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
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
    private val lifecycle = UiLifecycleStateMachine("player=$uuid", logger)
    private val lifecycleLock = Any()

    private data class PendingPageOpen(
        val componentPath: String,
        val lifetime: CustomPageLifetime,
    )

    private data class PageOpenSnapshot(
        val ref: Ref<EntityStore>,
        val store: Store<EntityStore>,
    )

    @Volatile
    private var pendingPageOpen: PendingPageOpen? = null

    @Volatile
    private var closeBarrier: CompletableFuture<Unit>? = null

    private val preOpenRequiredBindings = listOf(
        "ComponentPath",
        "PlayerBinding",
        "PageContextBinding",
        "StoreBinding",
        "LifecycleState"
    )

    private val postInitRequiredBindings = listOf(
        "JSRuntimeContext",
        "UIControllerBinding",
        "MessageBridge",
        "PageContextBinding"
    )

    private val optionalBindings = listOf(
        "VueRoot",
        "RuntimeBindings",
        "MessageChannelBinding"
    )

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

    @Volatile
    private var desiredHudComponentPath: String? = null

    // Buffers for setData/setHudData calls that arrive before the page/hud app is created.
    private val dataLock = Any()
    private val pendingPageData: MutableMap<String, Any?> = LinkedHashMap()
    private val pendingHudData: MutableMap<String, Any?> = LinkedHashMap()
    private val hudDataCache: MutableMap<String, Any?> = LinkedHashMap()

    private fun flushPendingPageData(app: App) {
        val pendingEntries = synchronized(dataLock) {
            if (pendingPageData.isEmpty()) return
            pendingPageData.entries.map { it.key to it.value }.also { pendingPageData.clear() }
        }
        pendingEntries.forEach { (k, v) -> app.setData(k, v) }
    }

    private fun flushPendingHudData(app: App) {
        val pendingEntries = synchronized(dataLock) {
            if (pendingHudData.isEmpty()) return
            pendingHudData.entries.map { it.key to it.value }.also { pendingHudData.clear() }
        }
        pendingEntries.forEach { (k, v) -> app.setData(k, v) }
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
        synchronized(lifecycleLock) {
            inferDesiredHudComponentPath("open-page-request")
            pendingPageOpen = PendingPageOpen(componentPath, lifetime)
            logUi("Opening UI requested", uiId = pageUiId(), extra = " component=$componentPath")

            when (lifecycle.currentState()) {
                UiLifecycleState.CLOSED, UiLifecycleState.DESTROYED -> startPendingOpenLocked("open-request")
                UiLifecycleState.OPEN -> {
                    if (lifecycle.transition(UiLifecycleState.CLOSING, "switch-open-request")) {
                        startCloseAsyncLocked(requestPageNone = true, reason = "switch-open-request")
                    }
                }

                UiLifecycleState.OPENING, UiLifecycleState.CLOSING -> {
                    logUi("Open queued while lifecycle busy", uiId = pageUiId())
                }
            }
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
        synchronized(lifecycleLock) {
            when (lifecycle.currentState()) {
                UiLifecycleState.CLOSED, UiLifecycleState.DESTROYED -> {
                    logUi("Close ignored; already closed", uiId = pageUiId())
                    return
                }

                UiLifecycleState.CLOSING -> {
                    logUi("Close ignored; already closing", uiId = pageUiId())
                    return
                }

                UiLifecycleState.OPEN, UiLifecycleState.OPENING -> {
                    if (lifecycle.transition(UiLifecycleState.CLOSING, "explicit-close")) {
                        startCloseAsyncLocked(requestPageNone = true, reason = "explicit-close")
                    }
                }
            }
        }
    }

    internal fun onPageDismissed(dismissedPage: VuetaleUIPage) {
        synchronized(lifecycleLock) {
            if (page !== dismissedPage && page != null) {
                logUi("Ignoring stale dismiss callback", uiId = dismissedPage.app.getId())
                return
            }

            if (page === dismissedPage) page = null

            when (lifecycle.currentState()) {
                UiLifecycleState.OPEN -> {
                    if (lifecycle.transition(UiLifecycleState.CLOSING, "external-dismiss")) {
                        startCloseAsyncLocked(requestPageNone = false, reason = "external-dismiss")
                    }
                }

                UiLifecycleState.OPENING -> {
                    if (lifecycle.transition(UiLifecycleState.CLOSING, "external-dismiss-during-open")) {
                        startCloseAsyncLocked(requestPageNone = false, reason = "external-dismiss-during-open")
                    }
                }

                else -> {}
            }

            logUi("UI dismissed by Hytale", uiId = pageUiId())
        }
    }

    // ── HUD API ────────────────────────────────────────────────────────────

    /**
     * Show (or replace) the player's HUD.
     *
     * @param componentPath  Module path of the Vue component, e.g. `"vt:@core/huds/ActionBar"`.
     */
    fun openHud(componentPath: String): PlayerUi {
        desiredHudComponentPath = componentPath
        ensureHudVisible("open-hud-request")
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
        desiredHudComponentPath = null

        val pRef = playerRef
        val world = pRef?.worldUuid?.toWorld()
        if (pRef != null && world != null) {
            world.execute {
                runCatching {
                    h.hide()
                    val reference = pRef.reference ?: return@runCatching
                    reference.store.getComponent(reference, Player.getComponentType())?.let { player ->
                        player.hudManager.resetHud(pRef)
                    }
                }.onFailure { t ->
                    logger.severe("closeHud async failed for $ownerId: ${t.javaClass.simpleName}: ${t.message}\n${t.stackTraceToString()}")
                }
            }
        } else {
            runCatching { h.hide() }
                .onFailure { t -> logger.warning("closeHud hide fallback failed for $ownerId: ${t.javaClass.simpleName}: ${t.message}") }
        }

        hud = null
        synchronized(dataLock) {
            pendingHudData.clear()
            hudDataCache.clear()
        }
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
        val app = synchronized(dataLock) {
            val currentApp = page?.app
            if (currentApp == null) {
                pendingPageData[key] = value
            }
            currentApp
        }
        app?.setData(key, value)
    }

    /**
     * Push a reactive data value to the Vue side for the currently active HUD.
     * Buffered if the HUD is not yet active.
     */
    fun setHudData(key: String, value: Any?) {
        val app = synchronized(dataLock) {
            hudDataCache[key] = value
            if (desiredHudComponentPath == null) {
                inferDesiredHudComponentPath("set-hud-data")
            }
            val currentApp = hud?.app
            if (currentApp == null) {
                pendingHudData[key] = value
            }
            currentApp
        }
        app?.setData(key, value)
    }

    // ── Internal ───────────────────────────────────────────────────────────

    private fun requirePlayerRef(): PlayerRef =
        playerRef ?: error("PlayerUi[$ownerId] has no PlayerRef – was it registered via PlayerUiManager?")

    private data class PlayerContext(val ref: Ref<EntityStore>, val store: Store<EntityStore>, val player: Player)

    private fun requirePlayerContext(): PlayerContext {
        val threadName = Thread.currentThread().name
        if (!threadName.contains("WorldThread")) {
            error("PlayerUi[$ownerId] requirePlayerContext() must run on WorldThread, actual thread=$threadName")
        }
        val ref = pageRef ?: error("PlayerUi[$ownerId] has no entity Ref")
        val store = pageStore ?: error("PlayerUi[$ownerId] has no entity Store")
        val player = store.getComponent(ref, Player.getComponentType())
            ?: error("PlayerUi[$ownerId]: Player component not found")
        return PlayerContext(ref, store, player)
    }

    private fun startPendingOpenLocked(reason: String) {
        if (!isWorldThread()) {
            dispatchPendingOpenToWorldThread(reason)
            return
        }

        closeBarrier?.let { barrier ->
            if (!barrier.isDone) {
                logUi("Open deferred until close barrier completes", uiId = pageUiId(), extra = " reason=$reason")
                return
            }
        }

        val request = pendingPageOpen ?: return
        val state = lifecycle.currentState()
        if (state != UiLifecycleState.CLOSED && state != UiLifecycleState.DESTROYED) {
            return
        }

        // Phase 1 (WorldThread): capture all Hytale thread-affined references before
        // any async work starts. Never call Store.getComponent() from ForkJoin threads.
        val snapshot = try {
            val ctx = requirePlayerContext()
            PageOpenSnapshot(ctx.ref, ctx.store)
        } catch (t: Throwable) {
            logger.severe("[UI] Pre-open world-thread snapshot failed uiId=${pageUiId()} player=$uuid state=${lifecycle.currentState()} thread=${Thread.currentThread().name} ts=${Instant.now()} error=${t.javaClass.simpleName}:${t.message}")
            return
        }

        pendingPageOpen = null
        if (!lifecycle.transition(UiLifecycleState.OPENING, reason)) {
            pendingPageOpen = request
            return
        }

        CompletableFuture.runAsync {
            val uiId = pageUiId()
            val preOpenMissing = validatePreOpenBindings(request.componentPath)
            if (preOpenMissing.isNotEmpty()) {
                logger.severe(
                    "[UI] Pre-open validation failed uiId=$uiId player=$uuid state=${lifecycle.currentState()} thread=${Thread.currentThread().name} ts=${Instant.now()} missing=${preOpenMissing.joinToString(",")}" +
                            " required=${preOpenRequiredBindings.joinToString(",")}" +
                            " optional=${optionalBindings.joinToString(",")}" +
                            " component=${request.componentPath}"
                )
                synchronized(lifecycleLock) {
                    lifecycle.transition(UiLifecycleState.CLOSING, "pre-open-validation-failed")
                    lifecycle.transition(UiLifecycleState.DESTROYED, "pre-open-validation-failed")
                    lifecycle.transition(UiLifecycleState.CLOSED, "pre-open-validation-failed-reset")
                    if (pendingPageOpen != null) {
                        startPendingOpenLocked("retry-after-pre-open-validation-failure")
                    }
                }
                return@runAsync
            }

            var postInitMissing: List<String> = emptyList()
            val openResult = runCatching {
                val pRef = requirePlayerRef()

                val newPage = VuetaleUIPage(pRef, ownerId, AppType.Page, request.lifetime, request.componentPath)
                page = newPage

                // Phase 3 (WorldThread): attach page using thread-affined Hytale APIs.
                attachPageOnWorldThread(snapshot, newPage)

                postInitMissing = validatePostInitBindings(newPage, snapshot)
                if (postInitMissing.isNotEmpty()) {
                    throw IllegalStateException("Post-init validation failed: ${postInitMissing.joinToString(",")}")
                }

                logUi("UI Opened", uiId = uiId)
            }

            synchronized(lifecycleLock) {
                if (openResult.isSuccess) {
                    lifecycle.transition(UiLifecycleState.OPEN, "open-complete")
                    ensureHudVisible("page-open-complete")
                } else {
                    if (postInitMissing.isNotEmpty()) {
                        logger.severe(
                            "[UI] Post-init validation failed uiId=$uiId player=$uuid state=${lifecycle.currentState()} thread=${Thread.currentThread().name} ts=${Instant.now()} missing=${postInitMissing.joinToString(",")}" +
                                    " required=${postInitRequiredBindings.joinToString(",")}" +
                                    " optional=${optionalBindings.joinToString(",")}" +
                                    " component=${request.componentPath}"
                        )
                    }

                    if (lifecycle.currentState() == UiLifecycleState.OPENING) {
                        lifecycle.transition(UiLifecycleState.CLOSING, if (postInitMissing.isNotEmpty()) "post-init-validation-failed" else "open-failed")
                    }
                    startCloseAsyncLocked(requestPageNone = true, reason = if (postInitMissing.isNotEmpty()) "post-init-validation-failed" else "open-failed")
                }

                if (pendingPageOpen != null) {
                    when (lifecycle.currentState()) {
                        UiLifecycleState.OPEN -> {
                            if (lifecycle.transition(UiLifecycleState.CLOSING, "queued-switch-after-open")) {
                                startCloseAsyncLocked(requestPageNone = true, reason = "queued-switch-after-open")
                            }
                        }

                        UiLifecycleState.CLOSED, UiLifecycleState.DESTROYED -> startPendingOpenLocked("queued-open")
                        else -> {}
                    }
                }
            }

            openResult.onFailure { t ->
                logger.severe("[UI] Open failed player=$uuid uiId=$uiId state=${lifecycle.currentState()} thread=${Thread.currentThread().name} ts=${Instant.now()} error=${t.javaClass.simpleName}:${t.message}\n${t.stackTraceToString()}")
            }
        }
    }

    private fun attachPageOnWorldThread(snapshot: PageOpenSnapshot, pageToOpen: VuetaleUIPage) {
        synchronized(lifecycleLock) {
            if (lifecycle.currentState() != UiLifecycleState.OPENING) {
                error("PlayerUi[$ownerId]: attach aborted because lifecycle state is ${lifecycle.currentState()} (expected OPENING)")
            }
        }

        val world = playerRef?.worldUuid?.toWorld()
            ?: error("PlayerUi[$ownerId]: world is unavailable for page attachment")

        val attachFuture = CompletableFuture<Unit>()

        world.execute {
            runCatching {
                synchronized(lifecycleLock) {
                    if (lifecycle.currentState() != UiLifecycleState.OPENING) {
                        error("PlayerUi[$ownerId]: attach aborted on WorldThread because lifecycle state is ${lifecycle.currentState()} (expected OPENING)")
                    }
                }

                val player = snapshot.store.getComponent(snapshot.ref, Player.getComponentType())
                    ?: error("PlayerUi[$ownerId]: Player component missing during page attach")
                player.pageManager.openCustomPage(snapshot.ref, snapshot.store, pageToOpen)
            }.onSuccess {
                attachFuture.complete(Unit)
            }.onFailure {
                attachFuture.completeExceptionally(it)
            }
        }

        // Wait only on the async worker, never on WorldThread.
        attachFuture.get(5, TimeUnit.SECONDS)
    }

    private fun startCloseAsyncLocked(requestPageNone: Boolean, reason: String) {
        val currentPage = page
        val currentApp = currentPage?.app
        page = null

        val barrier = closeBarrier?.takeIf { !it.isDone } ?: (currentApp?.destroyAsync(reason)
            ?: CompletableFuture.completedFuture(Unit)).also { closeBarrier = it }

        logUi("Starting Shutdown", uiId = currentApp?.getId(), extra = " reason=$reason")

        CompletableFuture.runAsync {
            runCatching {
                if (requestPageNone) {
                    requestHytalePageClose()
                }
                barrier.get(10, TimeUnit.SECONDS)
            }.onFailure { t ->
                logger.severe("[UI] Shutdown failed player=$uuid uiId=${currentApp?.getId()} state=${lifecycle.currentState()} thread=${Thread.currentThread().name} ts=${Instant.now()} error=${t.javaClass.simpleName}:${t.message}\n${t.stackTraceToString()}")
            }

            synchronized(lifecycleLock) {
                if (barrier.isCompletedExceptionally) {
                    // Keep CLOSING state on teardown failure to prevent overlapping reopen.
                    logUi("Close barrier failed; keeping state in CLOSING", uiId = currentApp?.getId(), extra = " reason=$reason")
                    return@synchronized
                }

                closeBarrier = null

                if (lifecycle.currentState() == UiLifecycleState.CLOSING) {
                    lifecycle.transition(UiLifecycleState.DESTROYED, "close-complete")
                }
                if (lifecycle.currentState() == UiLifecycleState.DESTROYED) {
                    lifecycle.transition(UiLifecycleState.CLOSED, "close-reset")
                }

                logUi("Shutdown Complete", uiId = currentApp?.getId(), extra = " reason=$reason")

                ensureHudVisible("page-close-complete")

                if (pendingPageOpen != null) {
                    startPendingOpenLocked("pending-open-after-close")
                }
            }
        }
    }

    private fun requestHytalePageClose() {
        val pRef = playerRef ?: return
        pRef.worldUuid?.toWorld()?.execute {
            val reference = pRef.reference ?: return@execute
            reference.store.getComponent(reference, Player.getComponentType())?.let { player ->
                player.pageManager.setPage(reference, reference.store, Page.None)
            }
        }
    }

    private fun validatePreOpenBindings(componentPath: String): List<String> {
        val missing = mutableListOf<String>()
        if (componentPath.isBlank()) missing.add("ComponentPath")
        if (playerRef == null) missing.add("PlayerBinding")
        if (pageRef == null) missing.add("PageContextBinding")
        if (pageStore == null) missing.add("StoreBinding")
        if (lifecycle.currentState() != UiLifecycleState.OPENING) missing.add("LifecycleState")

        return missing
    }

    private fun validatePostInitBindings(openedPage: VuetaleUIPage, snapshot: PageOpenSnapshot): List<String> {
        val missing = mutableListOf<String>()

        val engineAlive = JSEngine.getExistingInstance()?.isAlive == true
        if (!engineAlive) missing.add("JSRuntimeContext")

        val bridgeReady = runCatching {
            val engine = JSEngine.getExistingInstance() ?: return@runCatching false
            if (!engine.isAlive) return@runCatching false
            engine.bridge
            true
        }.getOrDefault(false)
        if (!bridgeReady) missing.add("MessageBridge")

        if (page !== openedPage || pageRef == null || pageStore == null) {
            missing.add("PageContextBinding")
        }

        // UIControllerBinding is verified from the world-thread snapshot acquired
        // before async execution started.
        if (pageRef !== snapshot.ref || pageStore !== snapshot.store) {
            missing.add("UIControllerBinding")
        }

        return missing
    }

    private fun logUi(event: String, uiId: String? = null, extra: String = "") {
        logger.info(
            "[UI] $event player=$uuid uiId=${uiId ?: "-"} state=${lifecycle.currentState()} thread=${Thread.currentThread().name} ts=${Instant.now()}$extra"
        )
    }

    private fun isWorldThread(): Boolean = Thread.currentThread().name.contains("WorldThread")

    private fun dispatchPendingOpenToWorldThread(reason: String) {
        val world = playerRef?.worldUuid?.toWorld()
        if (world == null) {
            logUi("Cannot dispatch pending open to WorldThread (world unavailable)", uiId = pageUiId(), extra = " reason=$reason")
            return
        }

        logUi("Dispatching pending open to WorldThread", uiId = pageUiId(), extra = " reason=$reason")
        world.execute {
            synchronized(lifecycleLock) {
                startPendingOpenLocked("world-dispatch:$reason")
            }
        }
    }

    private fun ensureHudVisible(reason: String) {
        val componentPath = inferDesiredHudComponentPath(reason) ?: run {
            logger.warning("ensureHudVisible skipped for $ownerId: desiredHudComponentPath is null (reason=$reason)")
            return
        }
        val pRef = playerRef ?: run {
            logger.warning("ensureHudVisible skipped for $ownerId: playerRef is null (reason=$reason)")
            return
        }
        val world = pRef.worldUuid?.toWorld() ?: run {
            logger.warning("ensureHudVisible skipped for $ownerId: world is unavailable (reason=$reason)")
            return
        }

        logUi("Reasserting HUD", uiId = "$ownerId-${AppType.Hud}", extra = " reason=$reason")

        world.execute {
            runCatching {
                val reference = pageRef ?: pRef.reference ?: run {
                    logger.warning("ensureHudVisible world-step skipped for $ownerId: no entity ref available (reason=$reason)")
                    return@runCatching
                }
                val store = pageStore ?: reference.store
                val player = store.getComponent(reference, Player.getComponentType()) ?: run {
                    logger.warning("ensureHudVisible world-step skipped for $ownerId: Player component missing (reason=$reason)")
                    return@runCatching
                }

                val targetHud = synchronized(dataLock) {
                    val existingHud = hud
                    if (existingHud != null) {
                        if (normalizeComponentPath(componentPath) != existingHud.app.componentPath) {
                            existingHud.app.navigateTo(componentPath)
                        }
                        existingHud
                    } else {
                        VuetaleUIHud(pRef, ownerId, componentPath).also { hud = it }
                    }
                }

                player.hudManager.addCustomHud(pRef, targetHud)
                reapplyHudDataCache(targetHud.app)
                logger.info("HUD reasserted for $ownerId (reason=$reason, component=$componentPath)")
            }.onFailure { t ->
                logger.warning("HUD reassert failed for $ownerId (reason=$reason): ${t.javaClass.simpleName}: ${t.message}")
            }
        }
    }

    private fun reapplyHudDataCache(app: App) {
        val cachedEntries = synchronized(dataLock) {
            hudDataCache.entries.map { it.key to it.value }
        }
        cachedEntries.forEach { (key, value) -> app.setData(key, value) }
    }

    private fun inferDesiredHudComponentPath(reason: String): String? {
        desiredHudComponentPath?.let { return it }

        val candidate = synchronized(dataLock) {
            hud?.app?.componentPath
        } ?: AppManager.getApp(AppManager.getAppId(ownerId, AppType.Hud))?.componentPath

        if (!candidate.isNullOrBlank()) {
            desiredHudComponentPath = candidate
            logger.info("Inferred desired HUD component for $ownerId: $candidate (reason=$reason)")
        }

        return desiredHudComponentPath
    }

    private fun normalizeComponentPath(path: String): String = path.removePrefix("vt:")

    private fun pageUiId(): String = "$ownerId-${AppType.Page}"
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

    /**
     * Remove a player's [PlayerUi] entry, unmounting any active UI.
     * Call on logout / session end.
     */
    fun remove(uuid: UUID) {
        playerUis.remove(uuid)?.let { ui ->
            ui.closePage()
            ui.closeHud()
        }
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
    ): PlayerUi {
        var componentPath = "@$module/huds/$hud"
        if (!componentPath.endsWith(".vue.js")) {
            componentPath += ".vue.js"
        }

        return getOrCreate(playerRef.uuid, playerRef, ref, store).openHud(componentPath)
    }
}