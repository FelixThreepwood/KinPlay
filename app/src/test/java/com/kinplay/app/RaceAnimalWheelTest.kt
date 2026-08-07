package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RaceAnimalWheelTest {
    @Test
    fun productionWheelKeepsTheReviewedAnimalOrder() {
        assertEquals(
            listOf("Kangaroo", "Cheetah", "Rabbit", "Frog", "Turtle", "Penguin"),
            RACE_ANIMAL_OPTIONS.map { it.label },
        )
    }

    @Test
    fun everyAnimalHasAReadyToUseMovementInstruction() {
        assertTrue(RACE_ANIMAL_OPTIONS.all { it.detail.isNotBlank() })
    }
}
