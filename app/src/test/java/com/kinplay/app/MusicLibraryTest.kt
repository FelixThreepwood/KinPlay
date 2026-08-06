package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MusicLibraryTest {
    @Test
    fun bundledMusicOffersSixDistinctOfflineTracks() {
        assertEquals(6, KINPLAY_MUSIC_TRACKS.size)
        assertEquals(6, KINPLAY_MUSIC_TRACKS.map { it.id }.toSet().size)
        assertTrue(KINPLAY_MUSIC_TRACKS.all { it.title.isNotBlank() && it.description.isNotBlank() })
        assertTrue(KINPLAY_MUSIC_TRACKS.all { it.resourceId != 0 })
    }

    @Test
    fun musicIsShownOnlyForActivitiesThatOptIntoTheParentController() {
        assertTrue(supportsBundledMusic("freeze_dance_statues"))
        assertTrue(supportsBundledMusic("kitchen_band_rehearsal"))
        assertFalse(supportsBundledMusic("paper_airplane_weather"))
        assertFalse(supportsBundledMusic("bilateral_mirror_moves"))
    }
}
