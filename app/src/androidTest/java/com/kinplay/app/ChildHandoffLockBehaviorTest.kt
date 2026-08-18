package com.kinplay.app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.compose.ui.test.longClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.lock.CHILD_HANDOFF_HOLD_MILLIS
import com.kinplay.app.lock.ChildHandoffLockContainer
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChildHandoffLockBehaviorTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun lockedSurfaceKeepsGuidanceHiddenUntilBlockedTapThenRemovesIt() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ChildHandoffLockContainer {
                    Column {
                        Text("Visible game content")
                        Button(onClick = {}) { Text("Game action") }
                    }
                }
            }
        }

        compose.onNodeWithTag("child-lock-control").performTouchInput {
            longClick(durationMillis = CHILD_HANDOFF_HOLD_MILLIS + 250L)
        }
        compose.waitForIdle()

        assertTrue(compose.onAllNodesWithText("Visible game content").fetchSemanticsNodes().isEmpty())
        assertTrue(compose.onAllNodesWithText("Hold key for 3 seconds to unlock").fetchSemanticsNodes().isEmpty())

        compose.onNodeWithTag("child-lock-blocker").performTouchInput { click() }
        compose.onNodeWithText("Hold key for 3 seconds to unlock").assertIsDisplayed()

        compose.waitUntil(timeoutMillis = 4_000L) {
            compose.onAllNodesWithText("Hold key for 3 seconds to unlock").fetchSemanticsNodes().isEmpty()
        }
    }

    @Test
    fun lockedControlRemainsAvailableForAccessibleRecovery() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ChildHandoffLockContainer {
                    Text("Visible game content")
                }
            }
        }

        compose.onNodeWithTag("child-lock-control").performTouchInput {
            longClick(durationMillis = CHILD_HANDOFF_HOLD_MILLIS + 250L)
        }
        compose.waitForIdle()

        compose.onNodeWithTag("child-lock-control")
            .assertIsDisplayed()
            .performClick()
    }
}
