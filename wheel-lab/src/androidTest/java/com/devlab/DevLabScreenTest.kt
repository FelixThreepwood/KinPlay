package com.devlab

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class DevLabScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun labCanSwitchBetweenWheelPagesAndKeepsControlsVisible() {
        compose.setContent { DevLabApp() }

        compose.onNodeWithTag("dev-lab-page-animals").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-animal-reel-animals").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-spin-animals").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-demo-colors").performClick()
        compose.onNodeWithTag("dev-lab-page-colors").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-wheel-colors").assertIsDisplayed()
        compose.onNodeWithTag("dev-lab-next-colors").assertIsDisplayed()
    }
}
