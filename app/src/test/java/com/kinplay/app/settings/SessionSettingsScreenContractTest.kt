package com.kinplay.app.settings

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionSettingsScreenContractTest {
    @Test
    fun settingsScreenExposesThePersistedDefaultRoundChoice() {
        val source = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/settings/SettingsScreen.kt"))

        assertTrue(source.contains("title = \"Default rounds\""))
        assertTrue(source.contains("options = SessionRounds.entries"))
        assertTrue(source.contains("selected = settings.defaultRounds"))
        assertTrue(source.contains("settings.copy(defaultRounds = it)"))
        assertTrue(source.contains("settings.defaultRounds.label"))
    }

    @Test
    fun eligibleDetailsPageExposesResolvedSessionControlsAndStartAction() {
        val source = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))
        val settingsSource = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/settings/AppSettings.kt"))

        assertTrue(source.contains("isTimedSessionEligible"))
        assertTrue(source.contains("SessionConfigurationControls"))
        assertTrue(source.contains("Start session"))
        assertTrue(source.contains("Applied session"))
        assertTrue(source.contains("onSaveSessionOverride"))
        assertTrue(source.contains("SessionChoiceStrip"))
        assertTrue(source.contains("horizontalScroll"))
        assertTrue(source.contains("options = SessionRounds.entries"))
        assertTrue(settingsSource.contains("FIFTEEN(\"15\""))
    }

    private fun projectRoot(): Path {
        var current = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        repeat(8) {
            if (Files.exists(current.resolve("app/src/main/java/com/kinplay/app/settings/SettingsScreen.kt"))) return current
            current = current.parent ?: return@repeat
        }
        error("Could not locate project root from ${System.getProperty("user.dir")}")
    }

    private fun readText(path: Path): String = String(Files.readAllBytes(path))
}
