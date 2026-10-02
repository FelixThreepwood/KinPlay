package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeApplicationMenuTest {
    private val mainSource = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))

    @Test
    fun homeAppBarExposesAnAccessibleThreeLineMenu() {
        assertTrue(mainSource.contains("app-menu-button"))
        assertTrue(mainSource.contains("Open app menu"))
        assertTrue(mainSource.contains("Icons.Default.Menu"))
    }

    @Test
    fun menuProvidesSettingsAndAboutWithoutRemovedDestinations() {
        listOf("Settings", "About the app").forEach { label ->
            assertTrue("Missing application-menu destination: $label", mainSource.contains(label))
        }
        listOf("AccountScreen(", "SafetyPrivacyScreen(", "Text(\"Account\")", "Safety and privacy").forEach { removed ->
            assertFalse("Removed destination remains in production source: $removed", mainSource.contains(removed))
        }
        assertTrue(mainSource.contains("AboutApp"))
        assertTrue(mainSource.contains("leadingIcon = { Icon(Icons.Default.Settings"))
        assertTrue(mainSource.contains("leadingIcon = { Icon(Icons.Default.Info"))
    }

    @Test
    fun onlyCurrentApplicationDestinationsHaveProductionRoutes() {
        assertFalse(mainSource.contains("Routes.Account"))
        assertTrue(mainSource.contains("Routes.AboutApp"))
        assertFalse(mainSource.contains("Routes.SafetyPrivacy"))
        assertTrue(mainSource.contains("Routes.Settings"))
        assertTrue(mainSource.contains("HOME_SHORTCUTS"))
    }

    private fun projectRoot(): Path {
        var current = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        repeat(8) {
            if (Files.exists(current.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))) return current
            current = current.parent ?: return@repeat
        }
        error("Could not locate project root from ${System.getProperty("user.dir")}")
    }

    private fun readText(path: Path): String = String(Files.readAllBytes(path))
}
