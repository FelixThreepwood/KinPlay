package com.devlab

import com.kinplay.wheel.SpinnerWheelOption
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnimalReelLogicTest {
    private val catalog = listOf(
        SpinnerWheelOption("cat", "Cat"),
        SpinnerWheelOption("dog", "Dog"),
        SpinnerWheelOption("owl", "Owl"),
    )

    @Test
    fun reelGeneratesValidAnimalsAtArbitrarilyLargePositions() {
        val positions = listOf(-9_000_000_000L, -1L, 0L, 1L, 9_000_000_000L)

        positions.forEach { position ->
            val animal = animalAtReelPosition(catalog, position, seed = 41L)
            assertTrue(animal in catalog)
        }
    }

    @Test
    fun reelGenerationIsStableForTheSamePositionAndSeed() {
        assertEquals(
            animalAtReelPosition(catalog, position = -123_456L, seed = 41L),
            animalAtReelPosition(catalog, position = -123_456L, seed = 41L),
        )
    }

    @Test
    fun spinTargetMovesDownwardByARealisticNumberOfRows() {
        val target = chooseAnimalReelTarget(currentPosition = 0L, random = Random(7))
        val travelledRows = 0L - target

        assertTrue(travelledRows in MIN_ANIMAL_REEL_STEPS..MAX_ANIMAL_REEL_STEPS)
    }

    @Test
    fun labBrandingUsesDevLabAndDoesNotExposeLegacyBrand() {
        assertEquals("Dev Lab", DEV_LAB_APP_NAME)
        assertTrue(DEV_LAB_DEMOS.all { demo ->
            listOf(demo.title, demo.description).none { text ->
                text.contains("KinPlay", ignoreCase = true)
            }
        })
    }
}
