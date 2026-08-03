package com.kinplay.app.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionConfigurationTest {
    @Test
    fun defaultsIncludeThreeRoundsAndTheExistingGlobalDuration() {
        val settings = AppSettingsCodec.decode(emptyMap())

        assertEquals(SessionRounds.THREE, settings.defaultRounds)
        assertEquals(ActivityDuration.TEN_MINUTES, settings.activityDuration)
        assertEquals(
            SessionConfiguration(ActivityDuration.TEN_MINUTES, SessionRounds.THREE),
            settings.sessionDefaults(),
        )
    }

    @Test
    fun codecPersistsDefaultRoundsAndNextSessionOverrides() {
        val settings = AppSettings(
            gameTimer = GameTimer.NINETY_SECONDS,
            activityDuration = ActivityDuration.TWENTY_MINUTES,
            defaultRounds = SessionRounds.SEVEN,
            colorTheme = AppColorTheme.OCEAN,
            launcherIcon = LauncherIconVariant.SUNSHINE,
            nextSessionOverrides = mapOf(
                "family_charades_animals" to SessionConfigurationOverride(
                    duration = ActivityDuration.FIVE_MINUTES,
                    rounds = SessionRounds.FIVE,
                ),
            ),
        )

        assertEquals(settings, AppSettingsCodec.decode(AppSettingsCodec.encode(settings)))
    }

    @Test
    fun invalidRoundValuesFallBackAndMalformedOverridesAreIgnored() {
        val decoded = AppSettingsCodec.decode(
            mapOf(
                AppSettingsCodec.DEFAULT_ROUNDS_KEY to "0",
                AppSettingsCodec.SESSION_OVERRIDES_KEY to "{\"family_charades_animals\":{\"rounds\":\"99\"}}",
            ),
        )

        assertEquals(SessionRounds.THREE, decoded.defaultRounds)
        assertTrue(decoded.nextSessionOverrides.isEmpty())
    }

    @Test
    fun overrideResolutionIsScopedAndDoesNotMutateGlobalDefaults() {
        val storage = InMemorySettingsKeyValueStore()
        val repository = AppSettingsRepository(storage)
        val defaults = repository.load().sessionDefaults()

        repository.saveNextSessionOverride(
            "family_charades_animals",
            SessionConfigurationOverride(duration = ActivityDuration.TWENTY_MINUTES, rounds = SessionRounds.FIVE),
        )

        assertEquals(
            SessionConfiguration(ActivityDuration.TWENTY_MINUTES, SessionRounds.FIVE),
            repository.peekNextSessionConfiguration("family_charades_animals"),
        )
        assertEquals(defaults, repository.load().sessionDefaults())
        assertEquals(defaults, repository.peekNextSessionConfiguration("another_game"))
    }

    @Test
    fun nextSessionOverrideSurvivesRepositoryRecreationThenIsConsumedOnce() {
        val storage = InMemorySettingsKeyValueStore()
        AppSettingsRepository(storage).saveNextSessionOverride(
            "family_charades_animals",
            SessionConfigurationOverride(rounds = SessionRounds.SEVEN),
        )

        val recreatedRepository = AppSettingsRepository(storage)
        assertEquals(
            SessionConfiguration(ActivityDuration.TEN_MINUTES, SessionRounds.SEVEN),
            recreatedRepository.consumeNextSessionConfiguration("family_charades_animals"),
        )
        assertNull(recreatedRepository.load().nextSessionOverrides["family_charades_animals"])
        assertEquals(
            recreatedRepository.load().sessionDefaults(),
            recreatedRepository.peekNextSessionConfiguration("family_charades_animals"),
        )
    }
}
