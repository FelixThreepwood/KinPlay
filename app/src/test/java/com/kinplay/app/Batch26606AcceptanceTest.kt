package com.kinplay.app

import com.kinplay.app.settings.AppColorTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Batch26606AcceptanceTest {
    private val item = KinPlayItem(
        id = "batch_item",
        type = "activity",
        status = "active",
        title = "Batch activity",
        summary = "A concise summary.",
        collapsedDescription = "One sentence for the collapsed card.",
        modes = listOf("quick_play"),
        minAge = 6,
        maxAge = 12,
        durationMinutes = 10,
        energyLevel = "calm",
        materials = emptyList(),
        setupSteps = emptyList(),
        playSteps = listOf("Step one"),
        participantSuitability = ParticipantSuitability.BOTH,
    )

    @Test
    fun homeUsesApprovedDescriptorAndBrowseActionsBeforeRecents() {
        assertEquals("Kid Friendly Family Fun", HOME_DESCRIPTOR)
        assertEquals(
            listOf(RANDOM_GAME_LABEL, ALL_GAMES_AND_ACTIVITIES_LABEL),
            HOME_SHORTCUTS.take(2).map { it.title },
        )
        assertTrue(HOME_SHORTCUTS.indexOfFirst { it.title == RANDOM_GAME_LABEL } < HOME_SHORTCUTS.indexOfFirst { it.title == ALL_GAMES_AND_ACTIVITIES_LABEL })
    }

    @Test
    fun collapsedCardsExposeOnlyNameAndOneSentence() {
        assertEquals(listOf("One sentence for the collapsed card."), item.collapsedCardPreviewLines())
        assertFalse(item.collapsedCardPreviewLines().any { it.contains("No materials") })
        assertFalse(item.collapsedCardPreviewLines().any { it.contains("Setup:") })
    }

    @Test
    fun ageLabelsExposeOnlyTheReviewedMinimumAge() {
        assertEquals("Ages 6+", item.displayAgeRange())
        assertEquals("Ages 2+", item.copy(minAge = 2, maxAge = 2).displayAgeRange())
    }

    @Test
    fun emptyMaterialsAreNotRenderedAsASection() {
        assertFalse(item.detailSections().any { it.title == "Materials" })
        assertTrue(item.copy(materials = listOf("paper")).detailSections().any { it.title == "Materials" })
    }

    @Test
    fun vibrantThemesAreAvailableAlongsideExistingThemes() {
        assertEquals(6, AppColorTheme.entries.size)
        assertTrue(AppColorTheme.entries.any { it == AppColorTheme.SUNSHINE })
        assertTrue(AppColorTheme.entries.any { it == AppColorTheme.TROPICAL })
        assertTrue(AppColorTheme.entries.any { it == AppColorTheme.LAVENDER })
        assertTrue(AppColorTheme.SUNSHINE.description.contains("vibrant", ignoreCase = true))
        assertTrue(AppColorTheme.TROPICAL.description.contains("vibrant", ignoreCase = true))
    }

    @Test
    fun hierarchicalSearchIndexesTitlesDescriptionsAndInstructions() {
        val searchable = item.copy(
            title = "Mirror March",
            summary = "A quiet movement game.",
            playSteps = listOf("Call out an instruction and mirror it."),
        )
        val other = item.copy(id = "other", title = "Paper Toss", summary = "A throwing game.")
        val pack = ContentPack(items = listOf(searchable, other))

        assertEquals(listOf("Mirror March"), pack.searchItems("mirror").map { it.title })
        assertEquals(listOf("Mirror March"), pack.searchItems("instruction").map { it.title })
        assertTrue(pack.searchItems("   ").isEmpty())
    }

    @Test
    fun favoriteToggleIsReversibleAndFavoritesExcludeUnknownIds() {
        val selected = emptySet<String>().toggleFavorite(item.id)
        assertTrue(item.id in selected)
        assertFalse(item.id in selected.toggleFavorite(item.id))
        val pack = ContentPack(items = listOf(item))
        assertEquals(listOf(item), pack.favoriteItems(selected + "unknown"))
    }
}
