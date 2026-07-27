package com.kinplay.app.lock

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChildHandoffLockTest {
    @Test
    fun holdProgressClampsAndOnlyCompletesAtThreeSeconds() {
        val initial = ChildHandoffLockState()
        val holding = initial.beginHold(nowMillis = 1_000)

        assertEquals(0f, holding.progress(nowMillis = 900))
        assertEquals(0.5f, holding.progress(nowMillis = 2_500))
        assertFalse(holding.shouldToggle(nowMillis = 3_999))
        assertTrue(holding.shouldToggle(nowMillis = 4_000))
        assertEquals(1f, holding.progress(nowMillis = 9_000))
    }

    @Test
    fun releasingEarlyCancelsWithoutChangingLockState() {
        val cancelled = ChildHandoffLockState(isLocked = false)
            .beginHold(nowMillis = 10)
            .cancelHold()

        assertFalse(cancelled.isLocked)
        assertEquals(null, cancelled.holdStartedAtMillis)
        assertEquals(0f, cancelled.progress(nowMillis = 5_000))
    }

    @Test
    fun completingAContinuousHoldTogglesAndResetsTheHold() {
        val locked = ChildHandoffLockState()
            .beginHold(nowMillis = 100)
            .completeHold(nowMillis = 3_100)

        assertTrue(locked.isLocked)
        assertEquals(null, locked.holdStartedAtMillis)
        assertFalse(
            locked.beginHold(nowMillis = 3_100).completeHold(nowMillis = 6_100).isLocked,
        )
    }

    @Test
    fun accessibleActivationRunsTheSameThreeSecondRuleAndCannotBeRestartedMidCountdown() {
        val countdown = ChildHandoffLockState()
            .beginAccessibleCountdown(nowMillis = 500)
            .beginAccessibleCountdown(nowMillis = 2_000)

        assertEquals(ChildHandoffActivation.ACCESSIBLE_COUNTDOWN, countdown.activation)
        assertEquals(500L, countdown.holdStartedAtMillis)
        assertFalse(countdown.shouldToggle(nowMillis = 3_499))
        assertTrue(countdown.completeHold(nowMillis = 3_500).isLocked)
    }

    @Test
    fun pointerCancellationCannotCancelAnAccessibleRecoveryCountdown() {
        val accessibleUnlock = ChildHandoffLockState(isLocked = true)
            .beginAccessibleCountdown(nowMillis = 100)
            .cancelPointerHold()

        assertEquals(ChildHandoffActivation.ACCESSIBLE_COUNTDOWN, accessibleUnlock.activation)
        assertFalse(accessibleUnlock.completeHold(nowMillis = 3_100).isLocked)
    }

    @Test
    fun activeLockGuardsBackExitAndGameControlsButNeverItsRecoveryControl() {
        val locked = ChildHandoffLockState(isLocked = true)

        assertFalse(locked.allows(InAppAction.BACK))
        assertFalse(locked.allows(InAppAction.EXIT))
        assertFalse(locked.allows(InAppAction.GAME_CONTROL))
        assertTrue(locked.allows(InAppAction.LOCK_CONTROL))
        assertTrue(ChildHandoffLockState(isLocked = false).allows(InAppAction.BACK))
    }
}
