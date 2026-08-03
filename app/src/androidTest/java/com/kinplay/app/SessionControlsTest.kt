package com.kinplay.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.AppSettings
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.settings.SessionConfigurationOverride
import com.kinplay.app.session.TimedSession
import com.kinplay.app.settings.resolveNextSessionConfiguration
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionControlsTest {
    @get:Rule
    val compose = createComposeRule()

    private val eligibleItem = KinPlayItem(
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
    fun eligibleDetailsPageShowsAppliedValuesAndPersistsNextSessionChoices() {
        var settings by mutableStateOf(AppSettings.DEFAULT)
        var started: TimedSession? = null
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ActivityDetailScreen(
                    item = eligibleItem,
                    isFavorite = false,
                    onToggleFavorite = {},
                    onMarkPlayed = {},
                    settings = settings,
                    navController = rememberNavController(),
                    onSaveSessionOverride = { gameId, override ->
                        settings = settings.copy(
                            nextSessionOverrides = settings.nextSessionOverrides +
                                (gameId to requireNotNull(override)),
                        )
                    },
                    onStartSession = { gameId ->
                        TimedSession(gameId, settings.resolveNextSessionConfiguration(gameId)).also { started = it }
                    },
                )
            }
        }

        compose.onNodeWithTag("session-configuration").assertIsDisplayed()
        compose.onNodeWithTag("session-applied").assertTextContains("10 minutes").assertTextContains("3 rounds")
        compose.onNodeWithTag("session-duration-20_minutes").performClick()
        compose.onNodeWithTag("session-rounds-7").performClick()
        compose.onNodeWithTag("session-applied").assertTextContains("20 minutes").assertTextContains("7 rounds")
        compose.onNodeWithTag("session-start-button").performClick()

        assertEquals(eligibleItem.id, started?.gameId)
        assertEquals("20_minutes", started?.configuration?.duration?.wireValue)
        assertEquals("7", started?.configuration?.rounds?.wireValue)
    }
}
