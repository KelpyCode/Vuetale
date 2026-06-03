package li.kelp.vuetale

import li.kelp.vuetale.app.UiLifecycleState
import li.kelp.vuetale.app.UiLifecycleStateMachine
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SampleTest {
    private val logger = Logger.getLogger("SampleTest")

    @Test
    fun lifecycleAllowsExpectedTransitions() {
        val sm = UiLifecycleStateMachine("test", logger)

        assertTrue(sm.transition(UiLifecycleState.OPENING, "open"))
        assertTrue(sm.transition(UiLifecycleState.OPEN, "opened"))
        assertTrue(sm.transition(UiLifecycleState.CLOSING, "close"))
        assertTrue(sm.transition(UiLifecycleState.DESTROYED, "destroyed"))
        assertTrue(sm.transition(UiLifecycleState.CLOSED, "closed"))
        assertEquals(UiLifecycleState.CLOSED, sm.currentState())
    }

    @Test
    fun lifecycleRejectsIllegalTransitions() {
        val sm = UiLifecycleStateMachine("test", logger)

        assertTrue(!sm.transition(UiLifecycleState.OPEN, "illegal-direct-open"))
        assertEquals(UiLifecycleState.CLOSED, sm.currentState())

        assertTrue(sm.transition(UiLifecycleState.OPENING, "open"))
        assertTrue(!sm.transition(UiLifecycleState.CLOSED, "illegal-opening-to-closed"))
        assertEquals(UiLifecycleState.OPENING, sm.currentState())
    }

    @Test
    fun lifecycleStressSequenceIsStable() {
        val sm = UiLifecycleStateMachine("stress", logger)
        val pool = Executors.newFixedThreadPool(8)
        val start = CountDownLatch(1)
        val done = CountDownLatch(200)

        repeat(200) { i ->
            pool.submit {
                start.await(2, TimeUnit.SECONDS)
                if (i % 2 == 0) {
                    sm.transition(UiLifecycleState.OPENING, "stress-open-$i")
                    sm.transition(UiLifecycleState.OPEN, "stress-opened-$i")
                } else {
                    sm.transition(UiLifecycleState.CLOSING, "stress-close-$i")
                    sm.transition(UiLifecycleState.DESTROYED, "stress-destroy-$i")
                    sm.transition(UiLifecycleState.CLOSED, "stress-reset-$i")
                }
                done.countDown()
            }
        }

        start.countDown()
        assertTrue(done.await(5, TimeUnit.SECONDS), "Stress lifecycle operations timed out")
        pool.shutdownNow()

        val final = sm.currentState()
        assertTrue(
            final == UiLifecycleState.CLOSED ||
                    final == UiLifecycleState.OPENING ||
                    final == UiLifecycleState.OPEN ||
                    final == UiLifecycleState.CLOSING ||
                    final == UiLifecycleState.DESTROYED,
            "Unexpected final state: $final"
        )
    }
}
