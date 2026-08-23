package com.devlab

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class DevLabScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun labShowsOnlyAnimalMovesAndKeepsItsControlsVisible() {
        compose.setContent { DevLabApp() }

        compose.onNodeWithTag("dev-lab-page-animals").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-demo-animals").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-animal-reel-animals").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-spin-animals").assertIsDisplayed()
        compose.onAllNodesWithTag("dev-lab-demo-colors").assertCountEquals(0)
        compose.onAllNodesWithTag("dev-lab-demo-long_labels").assertCountEquals(0)
    }
}
