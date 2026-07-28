package com.kinplay.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
class ContentCardHierarchyTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

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
    fun collapsedCardShowsLeftSummaryAndRightAlignedParticipantDurationAndAge() {
        setCard(width = 420.dp)

        compose.onNodeWithText(item.title).assertIsDisplayed()
        compose.onNodeWithText(item.summary).assertIsDisplayed()
        compose.onNodeWithText("Works 1:1 or with a group").assertIsDisplayed()
        compose.onNodeWithText("9 min").assertIsDisplayed()
        compose.onNodeWithText("Ages 3–8").assertIsDisplayed()
        compose.onNodeWithText("Needs: paper clues").assertIsDisplayed()
        compose.onNodeWithText("Setup: Hide three paper clues where everyone can reach them.").assertIsDisplayed()
        compose.onNodeWithText("Open").assertDoesNotExist()

        val title = compose.onNodeWithText(item.title, useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val summary = compose.onNodeWithText(item.summary, useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val participant = compose.onNodeWithText("Works 1:1 or with a group", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val duration = compose.onNodeWithText("9 min", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val age = compose.onNodeWithText("Ages 3–8", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        assertTrue("Title and summary must share a left edge", abs(title.left - summary.left) <= 1f)
        assertTrue("Participant descriptor must trail the primary labels", participant.left > title.left)
        assertTrue("Trailing descriptors must share a right edge", abs(participant.right - duration.right) <= 1f)
        assertTrue("Trailing descriptors must share a right edge", abs(duration.right - age.right) <= 1f)
    }

    @Test
    fun expansionAndFavoriteStarPreserveSummaryMaterialsSetupAndOpenAction() {
        setCard(width = 420.dp, favorite = true)

        compose.onNodeWithText("★ ${item.title}").assertIsDisplayed().performClick()
        compose.onAllNodesWithText(item.summary).assertCountEquals(1)
        compose.onNodeWithText("Needs: paper clues").assertIsDisplayed()
        compose.onNodeWithText("Setup: Hide three paper clues where everyone can reach them.").assertIsDisplayed()
        compose.onNodeWithText("Open").assertIsDisplayed()
        compose.onNodeWithText(item.energyLevel).assertIsDisplayed()

        compose.onNodeWithText("★ ${item.title}").performClick()
        compose.onNodeWithText("Open").assertDoesNotExist()
        compose.onNodeWithText("★ ${item.title}").assertIsDisplayed()
    }

    @Test
    fun narrowCardStacksOnlyAsNeededWithoutClippingOrLosingTrailingAlignment() {
        setCard(width = 280.dp)

        val required = listOf(
            item.title,
            item.summary,
            "Needs: paper clues",
            "Setup: Hide three paper clues where everyone can reach them.",
            "Works 1:1 or with a group",
            "9 min",
            "Ages 3–8",
        )
        required.forEach { compose.onNodeWithText(it).assertIsDisplayed() }

        val summary = compose.onNodeWithText(item.summary, useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val participant = compose.onNodeWithText("Works 1:1 or with a group", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val duration = compose.onNodeWithText("9 min", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val age = compose.onNodeWithText("Ages 3–8", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        assertTrue("Stacked descriptors must not obscure primary copy", participant.top >= summary.bottom)
        assertTrue("Stacked trailing descriptors must stay right aligned", abs(participant.right - duration.right) <= 1f)
        assertTrue("Stacked trailing descriptors must stay right aligned", abs(duration.right - age.right) <= 1f)
    }

    @Test
    fun largeFontScaleUsesAccessibleStackedFallbackAndKeepsAllCollapsedContent() {
        setCard(width = 420.dp, fontScale = 2f)

        val required = listOf(
            item.title,
            item.summary,
            "Needs: paper clues",
            "Setup: Hide three paper clues where everyone can reach them.",
            "Works 1:1 or with a group",
            "9 min",
            "Ages 3–8",
        )
        required.forEach { compose.onNodeWithText(it).assertIsDisplayed() }

        val summary = compose.onNodeWithText(item.summary, useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val participant = compose.onNodeWithText("Works 1:1 or with a group", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        assertTrue("Large text fallback must not overlap primary and trailing content", participant.top >= summary.bottom)
    }

    private fun setCard(
        width: androidx.compose.ui.unit.Dp,
        favorite: Boolean = false,
        fontScale: Float = 1f,
    ) {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale)) {
                KinPlayTheme(AppColorTheme.FOREST) {
                    Box(Modifier.width(width)) {
                        ContentCard(
                            item = item,
                            favoriteIds = if (favorite) setOf(item.id) else emptySet(),
                            navController = rememberNavController(),
                        )
                    }
                }
            }
        }
    }
}
