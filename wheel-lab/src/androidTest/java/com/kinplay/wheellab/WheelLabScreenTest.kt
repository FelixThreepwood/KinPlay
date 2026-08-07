package com.kinplay.wheellab

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class WheelLabScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun labCanSwitchBetweenWheelPagesAndKeepsControlsVisible() {
        compose.setContent { WheelLabApp() }

        compose.onNodeWithTag("wheel-lab-page-animals").assertIsDisplayed()
        compose.onNodeWithTag("wheel-lab-spin-animals").assertIsDisplayed()
        compose.onNodeWithTag("wheel-lab-demo-colors").performClick()
        compose.onNodeWithTag("wheel-lab-page-colors").assertIsDisplayed()
        compose.onNodeWithTag("wheel-lab-wheel-colors").assertIsDisplayed()
        compose.onNodeWithTag("wheel-lab-next-colors").assertIsDisplayed()
    }
}
