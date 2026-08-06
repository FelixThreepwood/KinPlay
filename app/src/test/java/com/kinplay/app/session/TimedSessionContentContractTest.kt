package com.kinplay.app.session

import com.kinplay.app.KinPlayItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimedSessionContentContractTest {
    private val item = KinPlayItem(
        id = "paper_airplane_weather",
        type = "activity",
        status = "active",
        title = "Paper Airplanes (it's easy!) + Weather",
        summary = "Make paper airplanes together and fly them through pretend weather.",
        modes = listOf("pick_a_game"),
        minAge = 6,
        maxAge = 8,
        durationMinutes = 15,
        energyLevel = "low",
        materials = listOf("paper"),
        setupSteps = listOf("Fold the paper and clear a safe lane."),
        playSteps = listOf("Fly in calm weather.", "Fly through pretend wind."),
    )

    @Test
    fun activeSessionSectionsContainOnlyTheStepsNeededToPlay() {
        val sections = item.activeSessionSections()

        assertEquals(listOf("Steps"), sections.map { it.title })
        assertEquals(item.playSteps, sections.single().lines)
        assertTrue(sections.none { section -> section.title == "Materials" || section.title == "Setup" })
    }
}
