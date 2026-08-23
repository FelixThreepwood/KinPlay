package com.devlab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DevLabSystemBarsTest {
    @get:Rule
    val compose = createAndroidComposeRule<DevLabActivity>()

    @Test
    fun systemNavigationRemainsVisibleWithDefaultBarBehavior() {
        compose.runOnIdle {
            val window = compose.activity.window
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            assertEquals(WindowInsetsControllerCompat.BEHAVIOR_DEFAULT, controller.systemBarsBehavior)

            val insets = ViewCompat.getRootWindowInsets(window.decorView)
            assertTrue(
                "The configured Android navigation bar must remain visible",
                insets?.isVisible(WindowInsetsCompat.Type.navigationBars()) == true,
            )
        }
    }
}
