package com.kinplay.app.wyr

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WouldYouRatherScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun categorySurfaceShowsExactlyFourOrderedOneTapChoices() {
        var selected: String? = null
        compose.setContent {
            MaterialTheme {
                WouldYouRatherPlayScreen(
                    categories = categories(),
                    selectedCategory = null,
                    prompt = null,
                    onSelectCategory = { selected = it.id },
                    onAdvance = {},
                    onExit = {},
                )
            }
        }

        compose.onAllNodesWithTag("wyr-category-choice").assertCountEquals(4)
        listOf("Cute & Silly", "Animals", "Gross", "Super Gross").forEach {
            compose.onNodeWithText(it).assertIsDisplayed()
        }
        compose.onNodeWithText("Animals").performClick()
        assertEquals("animals", selected)
    }

    @Test
    fun promptSurfaceHasPersistentExitAndAdvancesByTappingThePrompt() {
        var advances = 0
        var exits = 0
        val category = categories()[2]
        compose.setContent {
            MaterialTheme {
                WouldYouRatherPlayScreen(
                    categories = categories(),
                    selectedCategory = category,
                    prompt = category.prompts.first(),
                    onSelectCategory = {},
                    onAdvance = { advances += 1 },
                    onExit = { exits += 1 },
                )
            }
        }

        compose.onNodeWithTag("wyr-prompt").assertIsDisplayed().performClick()
        assertEquals(1, advances)
        compose.onNodeWithText("Exit").assertIsDisplayed().performClick()
        assertEquals(1, exits)
    }

    private fun categories() = listOf(
        "cute_silly" to "Cute & Silly",
        "animals" to "Animals",
        "gross" to "Gross",
        "super_gross" to "Super Gross",
    ).mapIndexed { index, (id, title) ->
        WouldYouRatherCategory(
            id = id,
            title = title,
            order = index + 1,
            prompts = listOf(
                WouldYouRatherPrompt(
                    id = "wyr_${id}_001",
                    text = "Would you rather choose the first thing or choose the second thing?",
                    status = WouldYouRatherPromptStatus.APPROVED,
                ),
            ),
        )
    }
}