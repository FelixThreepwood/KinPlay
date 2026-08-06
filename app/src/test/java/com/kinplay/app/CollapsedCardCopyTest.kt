package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollapsedCardCopyTest {
    @Test
    fun reviewedCardsExposeBriefDescriptionsAndEmphasisWithoutChangingRawSummaries() {
        val spy = KinPlayItem(
            id = "quiet_color_hunt",
            type = "activity",
            status = "active",
            title = "I Spy",
            summary = "Long internal summary.",
            collapsedDescription = "Pick a visible object in the room with a guessing game.",
            collapsedEmphasis = listOf("guessing game"),
            modes = listOf("pick_a_game"),
            minAge = 2,
            maxAge = 8,
            durationMinutes = 5,
            energyLevel = "calm",
        )

        assertEquals(
            "Pick a visible object in the room with a guessing game.",
            spy.collapsedCardPreviewLines().first(),
        )
        assertEquals(listOf("guessing game"), spy.collapsedEmphasis)
        assertTrue(spy.summary.startsWith("Long"))
    }
}
