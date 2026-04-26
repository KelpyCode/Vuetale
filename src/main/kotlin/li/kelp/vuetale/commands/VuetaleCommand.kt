package li.kelp.vuetale.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import li.kelp.vuetale.app.PlayerUiManager
import java.util.logging.Logger
import javax.annotation.Nonnull


class VuetaleCommand : AbstractPlayerCommand("vuetale", "Super test command!") {

    val logger = Logger.getLogger("VuetaleCommand")

    fun testPropagated() {
        logger.warning("This is a test log from VuetaleCommand.testPropagated()!")
    }

    fun testWithArgsAndReturn(x: Int, y: String): String {
        logger.warning("This is a test log from VuetaleCommand.testWithArgsAndReturn() with args: $x, $y!")
        return "Received args: $x and $y"
    }

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
        ui.setData("testFn", { testPropagated() })
        ui.setData("testFn2", this::testWithArgsAndReturn)

    }
}