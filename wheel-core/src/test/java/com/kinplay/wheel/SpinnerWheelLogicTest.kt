package com.kinplay.wheel

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpinnerWheelLogicTest {
    @Test
    fun spinTargetAvoidsTheCurrentChoiceWhenThereIsMoreThanOneChoice() {
        val target = chooseSpinnerIndex(optionCount = 6, currentIndex = 2, random = Random(7))

        assertTrue(target in 0 until 6)
        assertNotEquals(2, target)
    }

    @Test
    fun oneChoiceWheelAlwaysReturnsItsOnlyChoice() {
        assertEquals(0, chooseSpinnerIndex(optionCount = 1, currentIndex = 0, random = Random(7)))
        assertEquals(0, nextSpinnerIndex(currentIndex = 0, optionCount = 1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun emptyWheelIsRejected() {
        chooseSpinnerIndex(optionCount = 0, currentIndex = 0, random = Random(7))
    }

    @Test
    fun targetRotationAdvancesSeveralTurnsAndPlacesRequestedChoiceAtPointer() {
        val rotation = spinnerTargetRotation(
            currentRotation = 12f,
            targetIndex = 4,
            optionCount = 6,
            minimumTurns = 4,
        )

        assertTrue(rotation - 12f >= 4 * 360f)
        assertEquals(4, spinnerIndexAtPointer(rotation, optionCount = 6))
    }

    @Test
    fun nextChoiceWrapsAroundTheWheel() {
        assertEquals(0, nextSpinnerIndex(currentIndex = 5, optionCount = 6))
        assertEquals(3, nextSpinnerIndex(currentIndex = 2, optionCount = 6))
    }

    @Test(expected = IllegalArgumentException::class)
    fun targetIndexMustBeInsideTheWheel() {
        spinnerTargetRotation(currentRotation = 0f, targetIndex = 6, optionCount = 6)
    }
}
