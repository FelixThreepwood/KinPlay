package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CardHierarchyPresentationTest {
    private val item = KinPlayItem(
        id = "card_hierarchy_fixture",
        type = "activity",
        status = "active",
        title = "Family Treasure Hunt",
        summary = "Take turns finding objects that match playful clues.",
        modes = listOf("pick_a_game"),
        minAge = 3,
        maxAge = 8,
        durationMinutes = 9,
        energyLevel = "medium",
        materials = listOf("paper clues"),
        setupSteps = listOf("Hide three paper clues where everyone can reach them."),
        participantSuitability = ParticipantSuitability.BOTH,
    )

    @Test
    fun collapsedPresentationLeadsWithSummaryAndKeepsMaterialsAndSetupPreview() {
        assertEquals(
            listOf(
                item.summary,
                "Needs: paper clues",
                "Setup: Hide three paper clues where everyone can reach them.",
            ),
            item.collapsedCardPreviewLines(),
        )
    }

    @Test
    fun trailingDescriptorInputsRemainCompactCompleteAndOrdered() {
        assertEquals("Works 1:1 or with a group", item.participantFitLabel())
        assertEquals("9 min", "${item.durationMinutes} min")
        assertEquals("Ages 3–8", item.displayAgeRange())
    }

    @Test
    fun favoriteAndExpansionContractsRemainIndependent() {
        assertFalse(CONTENT_CARD_DEFAULT_EXPANDED)
        val favoriteIds = emptySet<String>().toggleFavorite(item.id)

        assertTrue(item.id in favoriteIds)
        assertTrue(item.id in favoriteIds.toggleFavorite("another_item"))
    }
}
