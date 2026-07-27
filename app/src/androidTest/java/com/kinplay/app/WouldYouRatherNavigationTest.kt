package com.kinplay.app

import android.os.SystemClock
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WouldYouRatherNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun seedItemOpensDedicatedPlayLaneAdvancesOnlyOnTapAndExitsBackToCategory() {
        waitForText("At the Dinner Table")
        compose.onNodeWithText("At the Dinner Table").performClick()

        waitForText("Would You Rather")
        compose.onNodeWithText("Would You Rather").performScrollTo().performClick()
        compose.onNodeWithText("Open").performScrollTo().performClick()

        compose.onNodeWithText("Pick a category").assertIsDisplayed()
        compose.onNodeWithText("Animals").assertIsDisplayed()

        val transitionStartedAt = SystemClock.elapsedRealtime()
        compose.onNodeWithText("Animals").performClick()
        compose.onNodeWithTag("wyr-prompt").assertIsDisplayed()
        val transitionElapsedMillis = SystemClock.elapsedRealtime() - transitionStartedAt
        assertTrue(
            "Prompt transition took ${transitionElapsedMillis}ms; expected less than 2000ms",
            transitionElapsedMillis < 2_000,
        )

        val firstPrompt = currentPromptText()
        compose.mainClock.advanceTimeBy(2_000)
        compose.waitForIdle()
        assertEquals("Prompt changed without a tap", firstPrompt, currentPromptText())

        compose.onNodeWithTag("wyr-prompt").performClick()
        compose.waitForIdle()
        assertNotEquals("Tap did not advance to a different prompt", firstPrompt, currentPromptText())

        compose.onNodeWithText("Exit").assertIsDisplayed().performClick()
        compose.onNodeWithText("Pick a category").assertDoesNotExist()
        compose.onNodeWithText("Would You Rather").assertExists()
        compose.onNodeWithText("Back home").assertExists()
        assertTrue(
            "Exit did not return to the originating category screen",
            compose.onAllNodesWithText("At the Dinner Table").fetchSemanticsNodes().isNotEmpty(),
        )
    }

    private fun waitForText(text: String) {
        compose.waitUntil(timeoutMillis = 5_000) {
            compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun currentPromptText(): String {
        val textNodes = compose.onAllNodesWithTag("wyr-prompt-text").fetchSemanticsNodes()
        assertEquals("Expected exactly one settled prompt", 1, textNodes.size)
        return textNodes.single().config[SemanticsProperties.Text].joinToString(separator = "") { it.text }
    }
}
