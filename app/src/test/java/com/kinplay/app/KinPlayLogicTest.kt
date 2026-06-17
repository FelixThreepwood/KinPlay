package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KinPlayLogicTest {
    private val activeQuick = KinPlayItem(
        id = "a",
        type = "activity",
        status = "active",
        title = "Active Quick",
        summary = "Ready",
        modes = listOf("quick_play"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 5,
        energyLevel = "medium",
    )
    private val inactiveQuick = activeQuick.copy(id = "b", status = "draft", title = "Draft")
    private val calmPrompt = activeQuick.copy(id = "c", type = "prompt", title = "Calm", modes = listOf("calm_down"), energyLevel = "calm")

    @Test
    fun itemsForModeReturnsOnlyActiveContentForThatMode() {
        val result = listOf(activeQuick, inactiveQuick, calmPrompt).itemsForMode("quick_play")

        assertEquals(listOf(activeQuick), result)
    }

    @Test
    fun pickForMeUsesSeedToChooseStableActiveModeItem() {
        val result = listOf(activeQuick, calmPrompt).pickForMode("quick_play", seed = 3)

        assertEquals(activeQuick, result)
    }

    @Test
    fun recentIdsMoveMostRecentToFrontAndDeduplicate() {
        val result = listOf("old", "a", "older").withRecentFirst("a", limit = 3)

        assertEquals(listOf("a", "old", "older"), result)
    }

    @Test
    fun toggleFavoriteAddsAndRemovesIds() {
        val added = emptySet<String>().toggleFavorite("a")
        val removed = added.toggleFavorite("a")

        assertTrue("a" in added)
        assertFalse("a" in removed)
    }
}
