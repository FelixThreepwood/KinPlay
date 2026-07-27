package com.kinplay.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.feedback.FeedbackStore
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
    val compose = createAndroidComposeRule<MainActivity>()

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
    )

    @Composable
    private fun DetailUnderTest() {
        KinPlayTheme(AppColorTheme.FOREST) {
            ActivityDetailScreen(
                item = item,
                itemId = item.id,
                isFavorite = false,
                onToggleFavorite = {},
                onMarkPlayed = {},
                settings = AppSettings.DEFAULT,
                navController = rememberNavController(),
            )
        }
    }

    @Test
    fun unlockedDetailFeedbackSavesCurrentItemIdAndTitle() {
        compose.runOnIdle { FeedbackStore(compose.activity.applicationContext).save(emptyList()) }
        compose.setContent { DetailUnderTest() }

        compose.onNodeWithTag("feedback-control").assertIsDisplayed().performClick()
        compose.onNodeWithText("Quick comment").performTextInput("Detail context retained")
        compose.onNodeWithText("Save note").performClick()

        compose.runOnIdle {
            val saved = FeedbackStore(compose.activity.applicationContext).load().single()
            assertEquals("detail/${item.id}", saved.screen)
            assertEquals(item.id, saved.contentId)
            assertEquals(item.title, saved.contentTitle)
        }
    }

    @Test
    fun detailFeedbackDisappearsOnlyWhileLockIsActiveAndReturnsAfterAccessibleUnlock() {
        compose.setContent { DetailUnderTest() }
        compose.mainClock.autoAdvance = false
        compose.onNodeWithTag("feedback-control").assertIsDisplayed()

        compose.onNodeWithTag("child-lock-control")
            .performSemanticsAction(SemanticsActions.OnClick)
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(3_100)
        compose.onNodeWithTag("child-lock-blocker").assertIsDisplayed()
        compose.onNodeWithTag("feedback-control").assertDoesNotExist()

        compose.onNodeWithTag("child-lock-control")
            .performSemanticsAction(SemanticsActions.OnClick)
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(3_100)
        compose.onNodeWithTag("child-lock-blocker").assertDoesNotExist()
        compose.onNodeWithTag("feedback-control").assertIsDisplayed()
    }
}
