package com.kinplay.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinplay.app.settings.AppColorTheme
import com.kinplay.app.ui.KinPlayTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MusicControlsLifecycleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun playingButtonDoesNotRestoreAsPlayingAfterActivityRecreation() {
        compose.setContent {
            KinPlayTheme(AppColorTheme.FOREST) {
                BundledMusicControls(itemId = "freeze_dance_statues", enabled = true)
            }
        }

        compose.onNodeWithTag("music-play-pause").assertTextEquals("Play").performClick()
        compose.onNodeWithTag("music-play-pause").assertTextEquals("Pause")

        compose.activityRule.scenario.recreate()

        compose.onNodeWithTag("music-play-pause").assertTextEquals("Play")
    }
}
