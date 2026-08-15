package com.devlab

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.devlab.feedback.FeedbackStore
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class DevLabFeedbackScreenTest {
    @get:Rule
    val compose = createAndroidComposeRule<DevLabActivity>()

    @Before
    fun clearStoredFeedback() {
        FeedbackStore(compose.activity.applicationContext).save(emptyList())
    }

    @After
    fun clearStoredFeedbackAfterTest() {
        FeedbackStore(compose.activity.applicationContext).save(emptyList())
    }

    @Test
    fun feedbackCapturesTheActiveDevLabDemoAndSavesLocally() {
        compose.onNodeWithTag("feedback-control").assertIsDisplayed().performClick()
        compose.onNodeWithText("Quick comment").performTextInput("The reel needs a clearer stop state.")
        compose.onNodeWithText("Save note").performClick()
        compose.waitForIdle()

        compose.onNodeWithText("Saved locally. 1 unsent.").assertIsDisplayed()
        compose.onNodeWithText("dev_lab/animals").assertIsDisplayed()
        compose.runOnIdle {
            val saved = FeedbackStore(compose.activity.applicationContext).load().single()
            assertEquals("dev_lab/animals", saved.screen)
            assertEquals("animals", saved.contentId)
            assertEquals("Animal moves", saved.contentTitle)
        }
    }
}
