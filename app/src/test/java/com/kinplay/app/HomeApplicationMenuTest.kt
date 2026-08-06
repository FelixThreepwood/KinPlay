package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
    fun menuProvidesEveryStagedApplicationDestination() {
        listOf("Settings", "Account", "About the app", "Safety and privacy").forEach { label ->
            assertTrue("Missing application-menu destination: $label", mainSource.contains(label))
        }
        assertTrue(mainSource.contains("No account system is included in this MVP"))
        assertTrue(mainSource.contains("SafetyPrivacy"))
        assertTrue(mainSource.contains("AboutApp"))
        assertTrue(mainSource.contains("leadingIcon = { Icon(Icons.Default.Settings"))
        assertTrue(mainSource.contains("leadingIcon = { Icon(Icons.Default.Person"))
        assertTrue(mainSource.contains("leadingIcon = { Icon(Icons.Default.Info"))
        assertTrue(mainSource.contains("leadingIcon = { Icon(Icons.Default.Lock"))
    }

    @Test
    fun menuDestinationsHaveRealRoutesAndDuplicateSettingsIsIntentional() {
        assertTrue(mainSource.contains("Routes.Account"))
        assertTrue(mainSource.contains("Routes.AboutApp"))
        assertTrue(mainSource.contains("Routes.SafetyPrivacy"))
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
