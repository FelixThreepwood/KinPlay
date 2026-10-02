package com.kinplay.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.settings.AppSettings
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SafetyWarningPresentationTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val activityItem = KinPlayItem(
        id = "quiet_color_hunt",
        type = "activity",
        status = "active",
        title = "I Spy",
        summary = "Play I Spy with ready-made color and shape clues.",
        modes = listOf("quick_play", "pick_a_game"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 5,
        energyLevel = "calm",
        safetyTags = listOf("parent_supervision", "movement", "sibling_friendly"),
        setupSteps = listOf("Choose one object everyone can see, then use the clues below."),
        playSteps = listOf("The adult starts: I spy with my little eye something blue."),
        parentNotes = "Take turns giving color and shape clues.",
    )

    @Test
    fun cardsShowActivityCopyWithoutClassificationLabels() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ContentCard(
                    item = activityItem,
                    favoriteIds = emptySet(),
                    navController = rememberNavController(),
                )
            }
        }

        compose.onNodeWithText(activityItem.title).assertIsDisplayed()
        compose.onNodeWithText(activityItem.summary).assertIsDisplayed()
        compose.onNodeWithText("Parent supervision").assertDoesNotExist()
        compose.onNodeWithText("Open").assertIsDisplayed()
    }

    @Test
    fun detailsShowPlainInstructionsWithoutClassificationLabels() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ActivityDetailScreen(
                    item = activityItem,
                    itemId = activityItem.id,
                    isFavorite = false,
                    onToggleFavorite = {},
                    onMarkPlayed = {},
                    settings = AppSettings.DEFAULT,
                    navController = rememberNavController(),
                )
            }
        }

        compose.onNodeWithText(activityItem.setupSteps.first(), substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        compose.onNodeWithText(activityItem.parentNotes)
            .performScrollTo()
            .assertIsDisplayed()
        compose.onNodeWithText("Safety tags: Parent supervision, Movement, Sibling friendly")
            .assertDoesNotExist()
        compose.onNodeWithText("parent_supervision").assertDoesNotExist()
    }
}
