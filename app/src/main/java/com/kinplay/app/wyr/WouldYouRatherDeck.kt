package com.kinplay.app.wyr

/** Serializable progress for independent per-category shuffled bags. */
data class WouldYouRatherDeckState(
    val remainingIdsByCategory: Map<String, List<String>> = emptyMap(),
    val lastIdByCategory: Map<String, String> = emptyMap(),
    val randomStateByCategory: Map<String, Long> = emptyMap(),
    val libraryIdsByCategory: Map<String, List<String>> = emptyMap(),
)

/**
 * Pure, deterministic shuffled-bag deck.
 *
 * Each category owns its own random stream, so draws in one category cannot alter
 * another category's sequence. A snapshot contains enough state to resume exactly.
 */
class WouldYouRatherDeck(
    library: WouldYouRatherLibrary,
    private val seed: Long,
    initialState: WouldYouRatherDeckState = WouldYouRatherDeckState(),
) {
    private val categories = library.categories.associateBy { it.id }
    private val remaining = linkedMapOf<String, MutableList<String>>()
    private val last = linkedMapOf<String, String>()
    private val randoms = linkedMapOf<String, StableRandom>()

    init {
        initialState.remainingIdsByCategory.forEach { (categoryId, savedRemaining) ->
            val category = categories[categoryId] ?: return@forEach
            val currentIds = category.prompts.map { it.id }
            val currentSet = currentIds.toSet()
            val knownIds = initialState.libraryIdsByCategory[categoryId].orEmpty()
            val cleanRemaining = savedRemaining.filter { it in currentSet }.distinct().toMutableList()
            val random = StableRandom(initialState.randomStateByCategory[categoryId] ?: categorySeed(categoryId))
            val savedLastId = initialState.lastIdByCategory[categoryId]?.takeIf { it in currentSet }

            // A state written by this version records the IDs it knew. Only IDs newly
            // added to the library are recovered. Legacy/corrupt state without that
            // inventory is conservatively rebuilt into one complete valid cycle.
            // A valid in-progress bag cannot contain the most recently drawn ID. If it
            // does, rebuild that category rather than trusting a parse-valid but
            // semantically inconsistent snapshot that could repeat across restoration.
            if (savedLastId != null && savedLastId in cleanRemaining) {
                cleanRemaining.clear()
                cleanRemaining += shuffled(currentIds, random)
            } else {
                val recoveredIds = if (knownIds.isEmpty()) {
                    currentIds.filterNot { it in cleanRemaining }
                } else {
                    val knownSet = knownIds.toSet()
                    currentIds.filter { it !in knownSet && it !in cleanRemaining }
                }
                cleanRemaining += shuffled(recoveredIds, random)
            }
            avoidCycleBoundaryRepeat(cleanRemaining, savedLastId)
            remaining[categoryId] = cleanRemaining
            randoms[categoryId] = random
            savedLastId?.let { last[categoryId] = it }
        }
    }

    fun draw(categoryId: String): WouldYouRatherPrompt {
        val category = requireNotNull(categories[categoryId]) { "Unknown Would You Rather category: $categoryId" }
        val bag = remaining.getOrPut(categoryId) { mutableListOf() }
        val random = randoms.getOrPut(categoryId) { StableRandom(categorySeed(categoryId)) }
        if (bag.isEmpty()) {
            bag += shuffled(category.prompts.map { it.id }, random)
            avoidCycleBoundaryRepeat(bag, last[categoryId])
        }

        val id = bag.removeAt(0)
        last[categoryId] = id
        return category.prompts.first { it.id == id }
    }

    fun snapshot(): WouldYouRatherDeckState {
        val activeCategoryIds = remaining.keys
        return WouldYouRatherDeckState(
            remainingIdsByCategory = activeCategoryIds.associateWith { remaining.getValue(it).toList() },
            lastIdByCategory = activeCategoryIds.mapNotNull { id -> last[id]?.let { id to it } }.toMap(),
            randomStateByCategory = activeCategoryIds.associateWith { randoms.getValue(it).state },
            libraryIdsByCategory = activeCategoryIds.associateWith { id -> categories.getValue(id).prompts.map { it.id } },
        )
    }

    private fun avoidCycleBoundaryRepeat(bag: MutableList<String>, previousId: String?) {
        if (bag.size > 1 && bag.first() == previousId) {
            val replacementIndex = bag.indexOfFirst { it != previousId }
            val first = bag[0]
            bag[0] = bag[replacementIndex]
            bag[replacementIndex] = first
        }
    }

    private fun shuffled(ids: List<String>, random: StableRandom): MutableList<String> =
        ids.toMutableList().also { values ->
            for (index in values.lastIndex downTo 1) {
                val other = random.nextInt(index + 1)
                val value = values[index]
                values[index] = values[other]
                values[other] = value
            }
        }

    private fun categorySeed(categoryId: String): Long {
        var hash = -3750763034362895579L
        categoryId.forEach { character ->
            hash = hash xor character.code.toLong()
            hash *= 1099511628211L
        }
        return seed xor hash
    }

    private class StableRandom(var state: Long) {
        fun nextInt(bound: Int): Int {
            require(bound > 0)
            state += -7046029254386353131L
            var value = state
            value = (value xor (value ushr 30)) * -4658895280553007687L
            value = (value xor (value ushr 27)) * -7723592293110705685L
            value = value xor (value ushr 31)
            return ((value ushr 1) % bound.toLong()).toInt()
        }
    }
}
