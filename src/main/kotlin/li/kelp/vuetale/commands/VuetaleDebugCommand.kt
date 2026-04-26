package li.kelp.vuetale.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import li.kelp.vuetale.javascript.DebugConfig
import java.util.logging.Logger
import javax.annotation.Nonnull


class VuetaleDebugCommand : AbstractPlayerCommand("vuetaledebug", "Toggle Vuetale debug logging") {

    val logger = Logger.getLogger("VuetaleDebugCommand")

    protected override fun execute(
        @Nonnull commandContext: CommandContext,
        @Nonnull store: Store<EntityStore?>,
        @Nonnull ref: Ref<EntityStore?>,
        @Nonnull playerRef: PlayerRef,
        @Nonnull world: World
    ) {
        // Simple toggle-only command to avoid depending on command argument APIs.
        val newState = DebugConfig.toggle()
        logger.info("Vuetale debug mode = $newState")
    }
}


