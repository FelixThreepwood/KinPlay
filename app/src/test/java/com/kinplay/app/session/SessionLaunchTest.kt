package com.kinplay.app.session

import com.kinplay.app.KinPlayItem
import com.kinplay.app.settings.ActivityDuration
import com.kinplay.app.settings.AppSettingsRepository
import com.kinplay.app.settings.InMemorySettingsKeyValueStore
import com.kinplay.app.settings.SessionConfiguration
import com.kinplay.app.settings.SessionRounds
import com.kinplay.app.settings.sessionDefaults
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionLaunchTest {
    private val eligibleActivity = KinPlayItem(
        id = "family_charades_animals",
        type = "activity",
        status = "active",
        title = "Charades",
        summary = "Act out animal clues.",
        modes = listOf("pick_a_game"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 10,
        energyLevel = "medium",
    )

    @Test
    fun onlyActivePickAGameActivitiesAreEligibleForTimedSessions() {
        assertTrue(eligibleActivity.isTimedSessionEligible())
        assertFalse(eligibleActivity.copy(status = "draft").isTimedSessionEligible())
        assertFalse(eligibleActivity.copy(type = "prompt").isTimedSessionEligible())
        assertFalse(eligibleActivity.copy(modes = listOf("calm_down")).isTimedSessionEligible())
    }

    @Test
    fun startTimedSessionUsesTheOneShotOverrideAndLeavesGlobalDefaultsUnchanged() {
        val repository = AppSettingsRepository(InMemorySettingsKeyValueStore())
        val globalDefaults = repository.load().sessionDefaults()
        repository.saveNextSessionOverride(
            eligibleActivity.id,
            com.kinplay.app.settings.SessionConfigurationOverride(
                duration = ActivityDuration.TWENTY_MINUTES,
                rounds = SessionRounds.SEVEN,
            ),
        )

        val session = startTimedSession(eligibleActivity.id, repository)

        assertEquals(
            TimedSession(
                gameId = eligibleActivity.id,
                configuration = SessionConfiguration(ActivityDuration.TWENTY_MINUTES, SessionRounds.SEVEN),
            ),
            session,
        )
        assertEquals(globalDefaults, repository.load().sessionDefaults())
        assertEquals(globalDefaults, repository.peekNextSessionConfiguration(eligibleActivity.id))
    }

    @Test
    fun startTimedSessionUsesGlobalDefaultsWhenNoOverrideExists() {
        val repository = AppSettingsRepository(InMemorySettingsKeyValueStore())
        val session = startTimedSession(eligibleActivity.id, repository)

        assertEquals(
            TimedSession(eligibleActivity.id, repository.load().sessionDefaults()),
            session,
        )
    }
}
