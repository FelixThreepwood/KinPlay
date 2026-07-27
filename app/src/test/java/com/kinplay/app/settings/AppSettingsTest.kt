package com.kinplay.app.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppSettingsTest {
    @Test
    fun defaultsRemainBackwardCompatibleWhenNoKeysExist() {
        assertEquals(
            AppSettings(
                gameTimer = GameTimer.ONE_MINUTE,
                activityDuration = ActivityDuration.TEN_MINUTES,
                colorTheme = AppColorTheme.FOREST,
                launcherIcon = LauncherIconVariant.TEAL,
            ),
            AppSettingsCodec.decode(emptyMap()),
        )
    }

    @Test
    fun codecRoundTripsEverySupportedChoice() {
        GameTimer.entries.forEach { timer ->
            ActivityDuration.entries.forEach { duration ->
                AppColorTheme.entries.forEach { theme ->
                    LauncherIconVariant.entries.forEach { launcherIcon ->
                        val settings = AppSettings(timer, duration, theme, launcherIcon)
                        assertEquals(settings, AppSettingsCodec.decode(AppSettingsCodec.encode(settings)))
                    }
                }
            }
        }
    }

    @Test
    fun unknownAndLegacyValuesFallBackWithoutCrashing() {
        val decoded = AppSettingsCodec.decode(
            mapOf(
                AppSettingsCodec.GAME_TIMER_KEY to "not-a-timer",
                AppSettingsCodec.ACTIVITY_DURATION_KEY to "999",
                AppSettingsCodec.COLOR_THEME_KEY to "removed-theme",
                AppSettingsCodec.LAUNCHER_ICON_KEY to "removed-icon",
                "unrelated_legacy_key" to "preserved-by-owner",
            ),
        )

        assertEquals(AppSettings.DEFAULT, decoded)
    }

    @Test
    fun repositoryPersistsChangesThroughAReplaceableKeyValueBoundary() {
        val storage = InMemorySettingsKeyValueStore()
        val repository = AppSettingsRepository(storage)
        val changed = AppSettings(
            gameTimer = GameTimer.NINETY_SECONDS,
            activityDuration = ActivityDuration.TWENTY_MINUTES,
            colorTheme = AppColorTheme.OCEAN,
            launcherIcon = LauncherIconVariant.SUNSHINE,
        )

        repository.save(changed)

        assertEquals(changed, AppSettingsRepository(storage).load())
        assertTrue(storage.values.keys.containsAll(AppSettingsCodec.knownKeys))
    }
}
