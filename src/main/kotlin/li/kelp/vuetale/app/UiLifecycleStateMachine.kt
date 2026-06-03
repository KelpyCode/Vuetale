package li.kelp.vuetale.app

import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Logger

enum class UiLifecycleState {
    CLOSED,
    OPENING,
    OPEN,
    CLOSING,
    DESTROYED
}

/**
 * Thread-safe lifecycle state gate for player UI sessions.
 *
 * This validates legal transitions and logs illegal ones with thread context,
 * but never blocks waiting on external work.
 */
class UiLifecycleStateMachine(
    private val label: String,
    private val logger: Logger,
    initialState: UiLifecycleState = UiLifecycleState.CLOSED,
) {
    private val stateRef = AtomicReference(initialState)

    fun currentState(): UiLifecycleState = stateRef.get()

    fun transition(next: UiLifecycleState, reason: String): Boolean {
        while (true) {
            val current = stateRef.get()
            if (!isLegalTransition(current, next)) {
                logger.warning("[UI] Illegal lifecycle transition label=$label from=$current to=$next reason=$reason thread=${Thread.currentThread().name}")
                return false
            }
            if (stateRef.compareAndSet(current, next)) {
                logger.info("[UI] Lifecycle transition label=$label from=$current to=$next reason=$reason thread=${Thread.currentThread().name}")
                return true
            }
        }
    }

    private fun isLegalTransition(from: UiLifecycleState, to: UiLifecycleState): Boolean {
        if (from == to) return false
        return when (from) {
            UiLifecycleState.CLOSED -> to == UiLifecycleState.OPENING
            UiLifecycleState.OPENING -> to == UiLifecycleState.OPEN || to == UiLifecycleState.CLOSING || to == UiLifecycleState.DESTROYED
            UiLifecycleState.OPEN -> to == UiLifecycleState.CLOSING
            UiLifecycleState.CLOSING -> to == UiLifecycleState.DESTROYED
            UiLifecycleState.DESTROYED -> to == UiLifecycleState.CLOSED || to == UiLifecycleState.OPENING
        }
    }
}
