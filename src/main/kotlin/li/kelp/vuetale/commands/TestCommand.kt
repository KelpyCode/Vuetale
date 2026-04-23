package li.kelp.vuetale.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.UUIDComponent
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import li.kelp.vuetale.app.AppType
import li.kelp.vuetale.app.PlayerUiManager
import li.kelp.vuetale.hytale.VuetaleUIPage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.Nonnull


class TestCommand : AbstractPlayerCommand("vuetale", "Super test command!") {
    protected override fun execute(
        @Nonnull commandContext: CommandContext,
        @Nonnull store: Store<EntityStore?>,
        @Nonnull ref: Ref<EntityStore?>,
        @Nonnull playerRef: PlayerRef,
        @Nonnull world: World
    ) {
        val player: Player? = store.getComponent(ref, Player.getComponentType()) // also a component

//        CompletableFuture.runAsync {
//            player?.pageManager?.openCustomPage(
//                ref,
//                store,
//                VuetaleUIPage(playerRef, playerRef.uuid.toString(), AppType.Page)
//            )
//        }

        val ui = PlayerUiManager.openPage(
            playerRef,
            ref as Ref<EntityStore>,
            store as Store<EntityStore>,
            "core",
            "TestPage"
        )

        class Abc(val a: String, val b: Int)

        ui.setData("test", "Hello this is a test!")
        ui.setData("test2", Abc("abc", 123))

    }
}