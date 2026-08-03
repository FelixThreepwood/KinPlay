package com.kinplay.app.session

import com.kinplay.app.settings.ActivityDuration
import com.kinplay.app.settings.SessionConfiguration
import com.kinplay.app.settings.SessionRounds
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimedSessionProgressTest {
    private val configuration = SessionConfiguration(
        duration = ActivityDuration.FIVE_MINUTES,
        rounds = SessionRounds.THREE,
    )

    @Test
    fun tickingKeepsTheCurrentRoundUntilItsTimerExpires() {
        val initial = TimedSessionProgress.initial(configuration)

        val afterTick = initial.tick(configuration)

        assertEquals(1, afterTick.round)
        assertEquals(299, afterTick.remainingSeconds)
        assertTrue(afterTick.isActive)
    }

    @Test
    fun expiringACompletedRoundStartsTheNextRoundWithAResetTimer() {
        val finalSecond = TimedSessionProgress(round = 1, remainingSeconds = 1)

        val nextRound = finalSecond.tick(configuration)

        assertEquals(2, nextRound.round)
        assertEquals(300, nextRound.remainingSeconds)
        assertTrue(nextRound.isActive)
    }

    @Test
    fun finishingTheFinalRoundProducesACompletedSession() {
        val finalRound = TimedSessionProgress(round = 3, remainingSeconds = 1)

        val completed = finalRound.tick(configuration)

        assertEquals(3, completed.round)
        assertEquals(0, completed.remainingSeconds)
        assertTrue(completed.isComplete)
    }

    @Test
    fun completingARoundCanBeUsedAsAnInteractiveSkipWithoutChangingConfiguration() {
        val current = TimedSessionProgress(round = 2, remainingSeconds = 47)

        val nextRound = current.completeRound(configuration)

        assertEquals(3, nextRound.round)
        assertEquals(300, nextRound.remainingSeconds)
        assertTrue(nextRound.isActive)
    }
}
