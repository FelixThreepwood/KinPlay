package com.kinplay.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.ActivityDuration
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.settings.SessionConfiguration
import com.kinplay.app.settings.SessionRounds
import com.kinplay.app.session.TimedSession
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimedSessionScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun sessionShowsProgressAndCompletesAfterEachRoundIsFinished() {
        val item = KinPlayItem(
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
        val session = TimedSession(
            gameId = item.id,
            configuration = SessionConfiguration(ActivityDuration.FIVE_MINUTES, SessionRounds.THREE),
        )

        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                TimedSessionScreen(
                    item = item,
                    session = session,
                    onExit = {},
                )
            }
        }

        compose.onNodeWithTag("timed-session-round").assertTextContains("Round 1 of 3")
        compose.onNodeWithTag("timed-session-timer").assertTextContains("05:00")
        compose.onNodeWithTag("timed-session-finish-round").assertIsDisplayed()

        repeat(3) {
            compose.onNodeWithTag("timed-session-finish-round").performClick()
            compose.waitForIdle()
        }

        compose.onNodeWithText("Session complete").performScrollTo().assertIsDisplayed()
    }
}
