package com.kinplay.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
    val compose = createAndroidComposeRule<MainActivity>()

    private val safetyItem = KinPlayItem(
        id = "indoor_pillow_marco_polo",
        type = "activity",
        status = "active",
        title = "Pillow Marco Polo: Eyes-Open Islands",
        summary = "A safe eyes-open call-and-response walk using pillows only as island boundary markers.",
        modes = listOf("quick_play", "pick_a_game"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 6,
        energyLevel = "medium",
        materials = listOf("two or three firm pillows"),
        safetyTags = listOf("parent_supervision", "movement", "sibling_friendly"),
        setupSteps = listOf(
            "Adult supervises and clears a flat room away from stairs, furniture edges, cords, pets, fragile objects, and other hazards.",
            "Place two or three firm pillows flat as stationary island or boundary markers with wide walking space between them.",
        ),
        playSteps = listOf("Everyone’s eyes remain open and all players walk only."),
        parentNotes = "Pillows are boundary/island markers only: never throw them, cover a face, pile them, or jump on them. Keep one arm of space between players.",
    )

    @Test
    fun retainedWarningsAreRenderedOnCollapsedExpandedAndDetailsSurfaces() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ContentCard(
                    item = safetyItem,
                    favoriteIds = emptySet(),
                    navController = rememberNavController(),
                )
            }
        }

        compose.onNodeWithText("Needs: two or three firm pillows")
            .assertIsDisplayed()
        compose.onNodeWithText("Setup: Adult supervises and clears a flat room away from stairs", substring = true)
            .assertIsDisplayed()
        compose.onNodeWithText(safetyItem.summary).assertIsDisplayed()

        compose.onNodeWithText(safetyItem.title).performClick()
        compose.onNodeWithText(safetyItem.summary).assertIsDisplayed()
        compose.onNodeWithText("Open").assertIsDisplayed()

        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                ActivityDetailScreen(
                    item = safetyItem,
                    itemId = safetyItem.id,
                    isFavorite = false,
                    onToggleFavorite = {},
                    onMarkPlayed = {},
                    settings = AppSettings.DEFAULT,
                    navController = rememberNavController(),
                )
            }
        }

        compose.onNodeWithText(safetyItem.setupSteps.first(), substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        compose.onNodeWithText(safetyItem.parentNotes)
            .performScrollTo()
            .assertIsDisplayed()
        compose.onNodeWithText("Safety tags: Parent supervision, Movement, Sibling friendly")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
