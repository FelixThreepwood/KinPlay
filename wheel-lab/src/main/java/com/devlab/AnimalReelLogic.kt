package com.devlab

import com.kinplay.wheel.SpinnerWheelOption
import kotlin.random.Random

const val MIN_ANIMAL_REEL_STEPS = 28L
const val MAX_ANIMAL_REEL_STEPS = 52L
const val DEV_LAB_APP_NAME = "Dev Lab"

/**
 * Resolve an animal for any reel position without materializing a finite wheel.
 * The seed makes the generated stream stable while the position range remains unbounded.
 */
fun animalAtReelPosition(
    catalog: List<SpinnerWheelOption>,
    position: Long,
    seed: Long,
): SpinnerWheelOption {
    require(catalog.isNotEmpty()) { "An animal reel needs at least one catalog item." }
    return catalog[Random(seed xor position).nextInt(catalog.size)]
}

/** Choose a target below the current position so the visible reel rolls downward. */
fun chooseAnimalReelTarget(
    currentPosition: Long,
    random: Random = Random.Default,
): Long {
    val travelledRows = random.nextLong(
        from = MIN_ANIMAL_REEL_STEPS,
        until = MAX_ANIMAL_REEL_STEPS + 1L,
    )
    return currentPosition - travelledRows
}
