package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.json.JSONObject
import java.nio.file.Files

class SafetySurfaceRevisionTest {
    @Test
    fun iSpySetupPreviewUsesNeutralHelpHeading() {
        val item = KinPlayItem(
            id = "quiet_color_hunt",
            type = "activity",
            status = "active",
            title = "I Spy",
            summary = "Play the familiar I Spy guessing game with ready-made color and shape rounds.",
            modes = listOf("calm_down"),
            minAge = 2,
            maxAge = 8,
            durationMinutes = 5,
            energyLevel = "calm",
            safetyTags = listOf("parent_supervision"),
            setupSteps = listOf("Choose one object everyone can see, then use the clues below."),
            playSteps = listOf(
                "The adult starts: I spy with my little eye something blue.",
                "Players point or guess until someone finds a blue object.",
                "Continue with a visible shape clue.",
            ),
        )

        val preview = item.setupPreviewLabel()
        assertTrue(preview.startsWith("Setup:"))
        assertTrue(item.detailSections().any { it.title == "Clues and suggestions" })
        assertFalse(preview.contains("tired", ignoreCase = true))
    }

    @Test
    fun internalClassificationIsNotRenderedOnTheNormalDetailsSurface() {
        val item = KinPlayItem(
            id = "quiet_color_hunt",
            type = "activity",
            status = "active",
            title = "I Spy",
            summary = "Play I Spy with ready-made color and shape clues.",
            modes = listOf("calm_down"),
            minAge = 2,
            maxAge = 8,
            durationMinutes = 5,
            energyLevel = "calm",
            safetyTags = listOf("parent_supervision"),
            setupSteps = listOf("Choose one object everyone can see, then use the clues below."),
        )

        assertEquals(listOf("parent_supervision"), item.safetyTags)
        val visibleDetails = item.detailSections().flatMap { it.lines }.joinToString(" ")
        assertFalse(visibleDetails.contains("parent_supervision"))
        assertFalse(visibleDetails.contains("Parent supervision", ignoreCase = true))
    }

    @Test
    fun canonicalAndRuntimeIspyCopyUsesNeutralReadyMadeHelp() {
        val root = repositoryRoot()
        val paths = listOf(
            root.resolve("content/seed/kinplay_seed_v1.json"),
            root.resolve("app/src/main/assets/kinplay_seed_v1.json"),
        )
        paths.forEach { path ->
            val items = JSONObject(Files.newBufferedReader(path).use { it.readText() }).getJSONArray("items")
            val item = (0 until items.length())
                .map { items.getJSONObject(it) }
                .single { it.getString("id") == "quiet_color_hunt" }
            val setup = item.getJSONArray("setupSteps").getString(0)
            assertEquals("Pick one thing everyone can see, then use the clues below.", setup)
            assertTrue(setup.contains("clues", ignoreCase = true))
            assertFalse(setup.contains("supervis", ignoreCase = true))
        }
    }

    private fun repositoryRoot(): java.nio.file.Path {
        var current = java.nio.file.Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!java.nio.file.Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
