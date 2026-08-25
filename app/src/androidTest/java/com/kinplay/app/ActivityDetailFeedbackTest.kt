package com.kinplay.app

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.feedback.FeedbackStore
import com.kinplay.app.feedback.FeedbackOverlay
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.settings.AppSettings
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityDetailFeedbackTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val item = KinPlayItem(
        id = "feedback_detail_item",
        type = "activity",
        status = "active",
        title = "Feedback Detail Item",
        summary = "A detail feedback regression fixture.",
        modes = listOf("quick_play"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 5,
        energyLevel = "calm",
        childHandoffLockEligible = true,
    )

    @Composable
    private fun DetailUnderTest() {
        val context = LocalContext.current.applicationContext
        KinPlayTheme(AppColorTheme.FOREST) {
            Box(Modifier.fillMaxSize()) {
                ActivityDetailScreen(
                    item = item,
                    itemId = item.id,
                    isFavorite = false,
                    onToggleFavorite = {},
                    onMarkPlayed = {},
                    settings = AppSettings.DEFAULT,
                    navController = rememberNavController(),
                )
                FeedbackOverlay(
                    context = context,
                    screen = "detail/${item.id}",
                    contentId = item.id,
                    contentTitle = item.title,
                )
            }
        }
    }

    @Test
    fun unlockedDetailFeedbackSavesCurrentItemIdAndTitle() {
        compose.runOnIdle { FeedbackStore(compose.activity.applicationContext).save(emptyList()) }
        compose.setContent { DetailUnderTest() }

        compose.onNodeWithTag("feedback-control").assertIsDisplayed().performClick()
        compose.onNodeWithTag("feedback-comment-field")
            .performTextInput("Detail context retained")
        compose.onNodeWithTag("feedback-comment-field").assertTextContains("Detail context retained")
        compose.onNodeWithText("Save note").performScrollTo().performClick()
        compose.onNodeWithTag("feedback-control").performClick()
        compose.onNodeWithText("Unsent: 1", substring = true).performScrollTo().assertIsDisplayed()

        compose.runOnIdle {
            val saved = FeedbackStore(compose.activity.applicationContext).load().single()
            assertEquals("detail/${item.id}", saved.screen)
            assertEquals(item.id, saved.contentId)
            assertEquals(item.title, saved.contentTitle)
        }
    }

    @Test
    fun detailFeedbackIsHostProvidedAndDetailDoesNotOwnTheRemovedLock() {
        compose.setContent { DetailUnderTest() }
        compose.onNodeWithTag("feedback-control").assertIsDisplayed()
        compose.onNodeWithTag("child-lock-control").assertDoesNotExist()
    }
}
