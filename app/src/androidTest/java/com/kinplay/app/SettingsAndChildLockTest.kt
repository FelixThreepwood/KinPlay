package com.kinplay.app

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.down
import androidx.compose.ui.test.up
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsAndChildLockTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun settingsDestinationChangesAndPersistsFinitePreferencesAcrossRecreation() {
        compose.onNodeWithText("Settings").performScrollTo().performClick()
        compose.onNodeWithTag("settings-screen").assertIsDisplayed()
        compose.onNodeWithTag("setting-launcher-icon-teal").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("setting-launcher-icon-sunshine").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("setting-timer-90_seconds").performScrollTo().performClick()
        compose.onNodeWithTag("setting-duration-20_minutes").performScrollTo().performClick()
        compose.onNodeWithTag("setting-theme-ocean").performScrollTo().performClick()

        compose.activityRule.scenario.recreate()

        compose.onNodeWithTag("setting-timer-90_seconds").performScrollTo().assertIsSelected()
        compose.onNodeWithTag("setting-duration-20_minutes").performScrollTo().assertIsSelected()
        compose.onNodeWithTag("setting-theme-ocean").performScrollTo().assertIsSelected()
        compose.onNodeWithText(
            "Current plan: 90 seconds rounds • 20 minutes activities • Ocean theme",
        ).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun shortTouchCannotActivateChildHandoffLock() {
        var controlTaps = 0
        compose.setContent {
            com.kinplay.app.ui.KinPlayTheme(com.kinplay.app.settings.AppColorTheme.FOREST) {
                com.kinplay.app.lock.ChildHandoffLockContainer {
                    Button(onClick = { controlTaps += 1 }) { Text("Game control") }
                }
            }
        }

        compose.onNodeWithTag("child-lock-control").assertIsDisplayed().performTouchInput {
            down(center)
            advanceEventTime(1_000)
            up()
        }
        compose.onNodeWithTag("child-lock-blocker").assertDoesNotExist()
        compose.onNodeWithText("Game control").performClick()
        assertEquals(1, controlTaps)
    }

    @Test
    fun semanticsActionShowsThreeSecondProgressAndSupportsLockAndRecovery() {
        compose.setContent {
            com.kinplay.app.ui.KinPlayTheme(com.kinplay.app.settings.AppColorTheme.FOREST) {
                com.kinplay.app.lock.ChildHandoffLockContainer {
                    Button(onClick = {}) { Text("Game control") }
                }
            }
        }
        compose.mainClock.autoAdvance = false

        compose.onNodeWithTag("child-lock-control")
            .performSemanticsAction(SemanticsActions.OnClick)
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(1_100)
        compose.onNodeWithTag("child-lock-progress").assertTextContains("1 of 3 seconds")
        compose.onNodeWithTag("child-lock-blocker").assertDoesNotExist()

        compose.mainClock.advanceTimeBy(2_000)
        compose.onNodeWithTag("child-lock-blocker").assertIsDisplayed()

        compose.onNodeWithTag("child-lock-control")
            .performSemanticsAction(SemanticsActions.OnClick)
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(3_100)
        compose.onNodeWithTag("child-lock-blocker").assertDoesNotExist()
        compose.onNodeWithText("Game control").performClick()
    }
}
