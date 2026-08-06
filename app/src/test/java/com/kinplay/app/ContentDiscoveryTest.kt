package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentDiscoveryTest {
    private fun item(
        id: String,
        title: String = id,
        type: String = "activity",
        quickCategories: List<String> = emptyList(),
        safetyTags: List<String> = emptyList(),
        energyLevel: String = "calm",
    ) = KinPlayItem(
        id = id,
        type = type,
        status = "active",
        title = title,
        summary = "A ready-to-use family activity for this test.",
        modes = listOf("pick_a_game"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 5,
        energyLevel = energyLevel,
        quickCategories = quickCategories,
        safetyTags = safetyTags,
        participantSuitability = ParticipantSuitability.BOTH,
    )

    @Test
    fun levelOneUsesFiniteReviewedGameTypeGroups() {
        assertEquals(
            listOf(
                "Word games",
                "Guessing games",
                "Arts and making",
                "Move and play",
                "Pretend and stories",
                "Brain and movement",
            ),
            GameTypeGroup.entries.map { it.label },
        )
    }

    @Test
    fun discoveryGroupsPlaceGamesAtLevelTwoAndHideRawMadLibStories() {
        val items = listOf(
            item("three_word_story", "Five Word Story", type = "prompt"),
            item("quiet_color_hunt", "I Spy", safetyTags = listOf("quiet")),
            item("paper_airplane_weather", "Paper Airplanes", quickCategories = listOf("outdoor_adventures")),
            item("animal_mirror_parade", "Animal Mirror Parade", safetyTags = listOf("movement"), energyLevel = "medium"),
            item("moon_pancake_mission", "Moon Pancake Mission", type = "mad_libs"),
        )
        val pack = ContentPack(items = items)

        assertTrue(pack.itemsForGameType(GameTypeGroup.WORD_GAMES.id).any { it.id == "three_word_story" })
        assertTrue(pack.itemsForGameType(GameTypeGroup.GUESSING_GAMES.id).any { it.id == "quiet_color_hunt" })
        assertTrue(pack.itemsForGameType(GameTypeGroup.ARTS_AND_MAKING.id).any { it.id == "paper_airplane_weather" })
        assertTrue(pack.itemsForGameType(GameTypeGroup.MOVE_AND_PLAY.id).any { it.id == "animal_mirror_parade" })
        assertFalse(pack.discoveryItems().any { it.type == "mad_libs" })
        assertTrue(pack.discoveryItems().any { it.isMadLibsCollection() })
    }

    @Test
    fun levelOneCardsExposeOnlyNameAndBriefDescriptionBeforeOpening() {
        assertFalse(GAME_TYPE_CARD_DEFAULT_EXPANDED)
        assertTrue(GameTypeGroup.entries.all { it.description.length in 20..120 })
    }
}
