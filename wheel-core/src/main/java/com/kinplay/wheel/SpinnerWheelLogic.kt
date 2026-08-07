package com.kinplay.wheel

import kotlin.math.floor
import kotlin.random.Random

/** A single label and optional ready-to-read instruction on a spinner wheel. */
data class SpinnerWheelOption(
    val id: String,
    val label: String,
    val detail: String = "",
)

/** Choose a different sector when possible so a repeated spin remains useful. */
fun chooseSpinnerIndex(
    optionCount: Int,
    currentIndex: Int,
    random: Random = Random.Default,
): Int {
    require(optionCount > 0) { "A spinner wheel needs at least one option." }
    require(currentIndex in 0 until optionCount) {
        "Current index $currentIndex is outside a wheel with $optionCount options."
    }
    if (optionCount == 1) return 0

    val candidate = random.nextInt(optionCount - 1)
    return if (candidate >= currentIndex) candidate + 1 else candidate
}

fun nextSpinnerIndex(currentIndex: Int, optionCount: Int): Int {
    require(optionCount > 0) { "A spinner wheel needs at least one option." }
    require(currentIndex in 0 until optionCount) {
        "Current index $currentIndex is outside a wheel with $optionCount options."
    }
    return (currentIndex + 1) % optionCount
}

/**
 * Return an increasing rotation that leaves [targetIndex]'s sector center under a fixed top pointer.
 * The rotation is intentionally cumulative so every spin visibly travels several full turns.
 */
fun spinnerTargetRotation(
    currentRotation: Float,
    targetIndex: Int,
    optionCount: Int,
    minimumTurns: Int = 5,
): Float {
    require(optionCount > 0) { "A spinner wheel needs at least one option." }
    require(targetIndex in 0 until optionCount) {
        "Target index $targetIndex is outside a wheel with $optionCount options."
    }
    require(minimumTurns >= 0) { "Minimum turns cannot be negative." }

    val sectorDegrees = 360f / optionCount
    val desiredRotation = positiveModulo(-(targetIndex + 0.5f) * sectorDegrees, 360f)
    val currentModulo = positiveModulo(currentRotation, 360f)
    var forwardDelta = positiveModulo(desiredRotation - currentModulo, 360f)
    if (forwardDelta < 0.001f) forwardDelta = 360f
    return currentRotation + minimumTurns * 360f + forwardDelta
}

/** Return the sector whose center contains the fixed pointer at twelve o'clock. */
fun spinnerIndexAtPointer(rotation: Float, optionCount: Int): Int {
    require(optionCount > 0) { "A spinner wheel needs at least one option." }
    val sectorDegrees = 360f / optionCount
    val wheelCoordinateAtPointer = positiveModulo(-rotation, 360f)
    return floor(wheelCoordinateAtPointer / sectorDegrees)
        .toInt()
        .coerceIn(0, optionCount - 1)
}

private fun positiveModulo(value: Float, modulus: Float): Float {
    val remainder = value % modulus
    return if (remainder < 0f) remainder + modulus else remainder
}
