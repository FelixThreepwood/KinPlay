package com.kinplay.app

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeVocabularyTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeShowsExactShortcutLabelsInTheEstablishedActionSection() {
        setHome()

        compose.onNodeWithText("More ways to start").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Random game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("All games and activities").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Pick For Me").assertDoesNotExist()
        compose.onNodeWithText("Browse All Games & Activities").assertDoesNotExist()
    }

    @Test
    fun shortcutAccessibilityClickLabelsMatchVisibleLabels() {
        setHome()

        assertClickLabel("Random game")
        assertClickLabel("All games and activities")
    }

    private fun setHome() {
        compose.setContent {
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

    private fun assertClickLabel(visibleLabel: String) {
        val node = compose.onNodeWithText(visibleLabel).performScrollTo()
        node.assertIsDisplayed()
        val clickAction = node.fetchSemanticsNode().config[SemanticsActions.OnClick]
        assertEquals(visibleLabel, clickAction.label)
    }
}