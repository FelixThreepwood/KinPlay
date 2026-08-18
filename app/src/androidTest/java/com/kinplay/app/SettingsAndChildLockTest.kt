package com.kinplay.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction


import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsAndChildLockTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun settingsDestinationChangesAndPersistsFinitePreferencesAcrossRecreation() {
        compose.onNodeWithTag("app-menu-button").performClick()
        compose.onNodeWithText("Settings").performScrollTo().performClick()
        compose.onNodeWithTag("settings-screen").assertIsDisplayed()
        compose.onNodeWithTag("setting-theme-lavender").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("setting-theme-ocean", useUnmergedTree = true).performScrollTo().performSemanticsAction(SemanticsActions.OnClick)
        compose.onNodeWithTag("setting-theme-ocean").assertIsSelected()
        compose.onNodeWithTag("setting-timer-90_seconds").performScrollTo().performClick()
        compose.onNodeWithTag("setting-timer-90_seconds").assertIsSelected()
        compose.onNodeWithTag("setting-duration-20_minutes").performScrollTo().performClick()
        compose.onNodeWithTag("setting-duration-20_minutes").assertIsSelected()
        compose.onNodeWithTag("setting-rounds-5").performScrollTo().performClick()
        compose.onNodeWithTag("setting-rounds-5").assertIsSelected()
        compose.activityRule.scenario.recreate()

        compose.onNodeWithTag("setting-timer-90_seconds").performScrollTo().assertIsSelected()
        compose.onNodeWithTag("setting-duration-20_minutes").performScrollTo().assertIsSelected()
        compose.onNodeWithTag("setting-rounds-5").performScrollTo().assertIsSelected()
        compose.onNodeWithTag("setting-theme-ocean").performScrollTo().assertIsSelected()
        compose.onNodeWithText(
            "Current plan: 5 rounds • 90 seconds per turn • 20 minutes activities • Ocean theme",
        ).performScrollTo().assertIsDisplayed()
    }

}
