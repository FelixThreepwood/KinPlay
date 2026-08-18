package com.kinplay.app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContentCardHierarchyTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val item = KinPlayItem(
        id = "card_hierarchy_fixture",
        type = "activity",
        status = "active",
        title = "Family Treasure Hunt",
        summary = "Take turns finding objects that match playful clues.",
        modes = listOf("pick_a_game"),
        minAge = 3,
        maxAge = 8,
        durationMinutes = 9,
        energyLevel = "medium",
        materials = listOf("paper clues"),
        setupSteps = listOf("Hide three paper clues where everyone can reach them."),
        participantSuitability = ParticipantSuitability.BOTH,
    )

    @Test
    fun levelOneCardIsMetadataLightTitleNavigableAndFavoriteIndependent() {
        var favoriteClicks = 0
        setCard(width = 420.dp, levelOne = true) { favoriteClicks += 1 }

        compose.onNodeWithText(item.title).assertIsDisplayed()
        compose.onNodeWithText(item.summary).assertIsDisplayed()
        compose.onNodeWithText("9 min").assertDoesNotExist()
        compose.onNodeWithText("Ages 3+").assertDoesNotExist()
        compose.onNodeWithText("Needs: paper clues").assertDoesNotExist()
        compose.onNodeWithText("Open").assertDoesNotExist()

        compose.onNodeWithTag("favorite-toggle-${item.id}").assertIsDisplayed().performClick()
        assertEquals(1, favoriteClicks)
    }

    @Test
    fun regularCardShowsExpandedPreviewAndOpenAction() {
        setCard(width = 420.dp)

        compose.onNodeWithText(item.title).assertIsDisplayed()
        compose.onNodeWithText(item.summary).assertIsDisplayed()
        compose.onNodeWithText("Works 1:1 or with a group").assertIsDisplayed()
        compose.onNodeWithText("9 min").assertIsDisplayed()
        compose.onNodeWithText("Ages 3+").assertIsDisplayed()
        compose.onNodeWithText(item.energyLevel).assertIsDisplayed()
        compose.onNodeWithText("Open").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun narrowLevelOneCardKeepsTitleAndSummaryWithoutMetadataOverflow() {
        setCard(width = 280.dp, levelOne = true)

        compose.onNodeWithText(item.title).assertIsDisplayed()
        compose.onNodeWithText(item.summary).assertIsDisplayed()
        compose.onNodeWithText("Needs: paper clues").assertDoesNotExist()
        compose.onNodeWithText("Setup: Hide three paper clues where everyone can reach them.")
            .assertDoesNotExist()
    }

    @Test
    fun largeFontScaleLevelOneCardKeepsItsCollapsedContract() {
        setCard(width = 420.dp, levelOne = true, fontScale = 2f)

        compose.onNodeWithText(item.title).assertIsDisplayed()
        compose.onNodeWithText(item.summary).assertIsDisplayed()
        compose.onNodeWithText("Open").assertDoesNotExist()
    }

    private fun setCard(
        width: androidx.compose.ui.unit.Dp,
        levelOne: Boolean = false,
        fontScale: Float = 1f,
        onToggleFavorite: () -> Unit = {},
    ) {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale)) {
                KinPlayTheme(AppColorTheme.FOREST) {
                    Box(Modifier.width(width)) {
                        ContentCard(
                            item = item,
                            favoriteIds = emptySet(),
                            navController = rememberNavController(),
                            onToggleFavorite = onToggleFavorite,
                            levelOne = levelOne,
                        )
                    }
                }
            }
        }
    }
}
