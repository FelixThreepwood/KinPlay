package com.kinplay.app

import com.kinplay.app.session.isTimedSessionEssential
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Batch57AcceptanceTest {
    private fun item(
        id: String,
        type: String = "activity",
        modes: List<String> = listOf("pick_a_game"),
        minAge: Int = 4,
    ) = KinPlayItem(
        id = id,
        type = type,
        status = "active",
        title = id.replace('_', ' ').replaceFirstChar(Char::uppercaseChar),
        summary = "A ready-to-use family activity summary.",
        modes = modes,
        minAge = minAge,
        maxAge = 8,
        durationMinutes = 8,
        energyLevel = "calm",
        participantSuitability = ParticipantSuitability.BOTH,
    )

    @Test
    fun homeCategoryGridUsesTwoColumnsOnlyWhenTheViewportCanSupportIt() {
        assertEquals(2, homeCategoryColumnCount(360, 1.0f))
        assertEquals(1, homeCategoryColumnCount(359, 1.0f))
        assertEquals(1, homeCategoryColumnCount(500, 1.5f))
    }

    @Test
    fun allGamesKeepsRecognizableLevelOneOrderAndMadLibsAsOneCollection() {
        val pack = ContentPack(
            items = listOf(
                item("story_spark_circle"),
                item("family_charades_animals"),
                item("animal_guessing_yes_no"),
                item("quiet_color_hunt"),
                item("would_you_rather_silly_family", type = "prompt"),
                item("moon_pancake_mission", type = "mad_libs", modes = listOf("mad_libs")),
                item("dragon_laundry_day", type = "mad_libs", modes = listOf("mad_libs")),
                item("zany_new_game"),
            ),
        )

        assertEquals(
            listOf("quiet_color_hunt", "would_you_rather_silly_family", MAD_LIBS_COLLECTION_ID, "family_charades_animals", "animal_guessing_yes_no", "story_spark_circle"),
            pack.allGamesLevelOneItems().take(6).map { it.id },
        )
        assertEquals(1, pack.allGamesLevelOneItems().count { it.id == MAD_LIBS_COLLECTION_ID })
        assertFalse(pack.allGamesLevelOneItems().any { it.type == "mad_libs" })
    }

    @Test
    fun onlyPlayCriticalActivitiesKeepDetailSessionControls() {
        assertTrue(item("timed_drawing_tiny_monster").isTimedSessionEssential())
        assertTrue(item("rainbow_sort_sprint").isTimedSessionEssential())
        assertTrue(item("cleanup_countdown_game").isTimedSessionEssential())
        assertFalse(item("paper_airplane_weather").isTimedSessionEssential())
        assertFalse(item("race_like_an_animal").isTimedSessionEssential())
    }

    @Test
    fun raceWheelContainsTheReviewedAnimalChoices() {
        assertEquals(listOf("Kangaroo", "Cheetah", "Rabbit", "Frog", "Turtle", "Penguin"), RACE_ANIMAL_CHOICES)
    }

    @Test
    fun batch57UiContractsArePresentInTheNativeSurfaces() {
        val source = readText(repositoryRoot().resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))
        val settings = readText(repositoryRoot().resolve("app/src/main/java/com/kinplay/app/settings/SettingsScreen.kt"))
        assertTrue(source.contains("random-reroll-button"))
        assertTrue(source.contains("home-activity-themes-toggle"))
        assertTrue(source.contains("Game categories"))
        assertTrue(source.contains("content-card-single-column"))
        assertTrue(source.contains("VisualInstructionGuide"))
        assertTrue(settings.contains("label = AppColorTheme::label"))
        assertTrue(settings.contains("verticalOptions = true"))
    }

    private fun repositoryRoot(): Path {
        var current = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        repeat(8) {
            if (Files.exists(current.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))) return current
            current = current.parent ?: return@repeat
        }
        error("Could not locate repository root")
    }

    private fun readText(path: Path): String = String(Files.readAllBytes(path))
}
