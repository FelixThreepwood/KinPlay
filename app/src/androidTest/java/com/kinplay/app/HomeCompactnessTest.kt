package com.kinplay.app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeCompactnessTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun compactPhoneShowsIdentityOneLinePurposeAndAllSixCategoriesWithoutAnInstructionGap() {
        setHome(width = 320, height = 640)

        val identity = compose.onNodeWithText("KinPlay", useUnmergedTree = true).assertIsDisplayed()
        val descriptor = assertDescriptorIsSingleLineAndFits()

        val identityBounds = identity.fetchSemanticsNode().boundsInRoot
        val descriptorBounds = descriptor.fetchSemanticsNode().boundsInRoot
        assertTrue("Purpose must sit beside identity", descriptorBounds.left >= identityBounds.right)

        openCategoryDrawer()
        val drawerBounds = compose.onNodeWithTag("home-activity-themes-drawer").fetchSemanticsNode().boundsInRoot
        val drawerToggleBounds = compose.onNodeWithTag("home-activity-themes-toggle").fetchSemanticsNode().boundsInRoot
        val firstCategory = compose.onNodeWithTag("home-category-quiet_games").performScrollTo().assertIsDisplayed()
        val firstBounds = firstCategory.fetchSemanticsNode().boundsInRoot
        val density = compose.activity.resources.displayMetrics.density
        assertTrue(
            "Category drawer has an unnecessary vertical gap above its toggle",
            drawerBounds.top - identityBounds.bottom <= 52f * density,
        )
        assertTrue(
            "Category grid has an unnecessary gap inside its drawer",
            firstBounds.top - drawerToggleBounds.bottom <= 12f * density,
        )
        assertAllCategoriesFitViewport()
        compose.onNodeWithText("What fits now").assertDoesNotExist()
    }

    @Test
    fun representativeWideHomeKeepsEveryCategoryInsideItsTouchTarget() {
        assertResponsiveHome(width = 600, fontScale = 1f)
    }

    @Test
    fun largeFontHomeKeepsEveryCategoryInsideItsTouchTarget() {
        assertResponsiveHome(width = 320, fontScale = 1.5f)
    }

    private fun assertResponsiveHome(width: Int, fontScale: Float) {
        setHome(width = width, height = 640, fontScale = fontScale)
        openCategoryDrawer()

        assertDescriptorIsSingleLineAndFits()
        QuickCategory.defaultGrid.forEach { category ->
            val cardBounds = compose.onNodeWithTag("home-category-${category.id}")
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertHeightIsAtLeast(48.dp)
                .assertWidthIsAtLeast(48.dp)
                .fetchSemanticsNode().boundsInRoot
            val labelNode = compose.onNodeWithText(category.label, useUnmergedTree = true).assertIsDisplayed()
            val cueNode = compose.onNodeWithText(category.placeCue, substring = true, useUnmergedTree = true).assertIsDisplayed()
            assertTextHasNoVisualOverflow(labelNode, "${category.label} label")
            assertTextHasNoVisualOverflow(cueNode, "${category.label} cue")
            val labelBounds = labelNode.fetchSemanticsNode().boundsInRoot
            val cueBounds = cueNode.fetchSemanticsNode().boundsInRoot
            assertTrue("${category.label} label is clipped above its target", labelBounds.top >= cardBounds.top)
            assertTrue("${category.label} label overlaps its cue", labelBounds.bottom <= cueBounds.top)
            assertTrue("${category.label} cue is clipped below its target", cueBounds.bottom <= cardBounds.bottom)
        }
        assertAllCategoriesFitViewport()
    }

    @Test
    fun everyVisibleHomeActionHasAtLeastA48DpTouchTarget() {
        setHome(width = 320, height = 640)
        openCategoryDrawer()

        QuickCategory.defaultGrid.forEach { category ->
            compose.onNodeWithTag("home-category-${category.id}")
                .assertHasClickAction()
                .assertHeightIsAtLeast(48.dp)
                .assertWidthIsAtLeast(48.dp)
        }
        listOf("random_game", "all_games_and_activities").forEach { action ->
            compose.onNodeWithTag("home-action-$action")
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertHeightIsAtLeast(48.dp)
                .assertWidthIsAtLeast(48.dp)
        }
    }

    private fun assertAllCategoriesFitViewport() {
        val viewport = compose.onNodeWithTag("home-viewport").fetchSemanticsNode().boundsInRoot
        QuickCategory.defaultGrid.forEach { category ->
            val bounds = compose.onNodeWithTag("home-category-${category.id}")
                .performScrollTo()
                .assertIsDisplayed()
                .fetchSemanticsNode().boundsInRoot
            assertTrue("${category.label} starts outside the viewport", bounds.left >= viewport.left)
            assertTrue("${category.label} ends outside the viewport", bounds.right <= viewport.right)
            assertTrue("${category.label} is below the first screen", bounds.bottom <= viewport.bottom)
        }
    }

    private fun assertDescriptorIsSingleLineAndFits() =
        compose.onNodeWithText(HOME_DESCRIPTOR, useUnmergedTree = true).assertIsDisplayed().also { descriptor ->
            val layouts = textLayouts(descriptor)
            val layout = layouts.single()
            assertEquals("Home purpose must stay on one line", 1, layout.lineCount)
            assertFalse(
                "Home purpose must not be clipped or ellipsized: size=${layout.size}, lines=${layout.lineCount}, overflow=${layout.hasVisualOverflow}",
                layout.hasVisualOverflow,
            )
        }

    private fun assertTextHasNoVisualOverflow(node: SemanticsNodeInteraction, description: String) {
        val layout = textLayouts(node).single()
        assertFalse("$description must not be clipped or ellipsized", layout.hasVisualOverflow)
    }

    private fun textLayouts(node: SemanticsNodeInteraction): List<TextLayoutResult> =
        mutableListOf<TextLayoutResult>().also { layouts ->
            node.performSemanticsAction(SemanticsActions.GetTextLayoutResult) { action -> action(layouts) }
        }

    private fun openCategoryDrawer() {
        compose.onNodeWithTag("home-activity-themes-toggle").performClick()
    }

    private fun setHome(width: Int, height: Int, fontScale: Float = 1f) {
        compose.setContent {
            val deviceDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(deviceDensity.density, fontScale),
            ) {
                Box(Modifier.size(width.dp, height.dp).testTag("home-viewport")) {
                    KinPlayTheme(AppColorTheme.FOREST) {
                        HomeScreen(
                            contentPack = ContentPack(),
                            favoriteIds = emptySet(),
                            recentIds = emptyList(),
                            navController = rememberNavController(),
                        )
                    }
                }
            }
        }
    }
}
