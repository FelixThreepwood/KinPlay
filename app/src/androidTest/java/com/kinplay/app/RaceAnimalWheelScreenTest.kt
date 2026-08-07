package com.kinplay.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Rule
import org.junit.Test

class RaceAnimalWheelScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun productionWheelExposesSelectionAndSpinControls() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                RaceAnimalWheel()
            }
        }

        compose.onNodeWithTag("race-animal-wheel-card").assertIsDisplayed()
        compose.onNodeWithTag("race-animal-wheel").assertIsDisplayed()
        compose.onNodeWithTag("race-animal-selected").assertIsDisplayed()
        compose.onNodeWithTag("race-animal-spin-button").assertIsDisplayed()
        compose.onNodeWithTag("race-animal-next-button").assertIsDisplayed()
        compose.onNodeWithTag("race-animal-instruction").assertIsDisplayed()
    }

    @Test
    fun lockedProductionWheelDisablesBothActions() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                RaceAnimalWheel(enabled = false)
            }
        }

        compose.onNodeWithTag("race-animal-spin-button").assertIsNotEnabled()
        compose.onNodeWithTag("race-animal-next-button").assertIsNotEnabled()
    }
}
