package com.kinplay.app

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsActions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.down
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.up
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.lock.ChildHandoffLockContainer
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChildHandoffLockAndroidTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun shortTouchCannotActivateChildHandoffLock() {
        var controlTaps = 0
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ChildHandoffLockContainer {
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
            KinPlayTheme(AppColorTheme.FOREST) {
                ChildHandoffLockContainer {
                    Button(onClick = {}) { Text("Game control") }
                }
            }
        }
        compose.mainClock.autoAdvance = false

        compose.onNodeWithTag("child-lock-control")
            .performSemanticsAction(SemanticsActions.OnClick)
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(1_100)
        compose.onNodeWithTag("child-lock-progress", useUnmergedTree = true).assertTextContains("1 of 3 seconds", substring = true)
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
