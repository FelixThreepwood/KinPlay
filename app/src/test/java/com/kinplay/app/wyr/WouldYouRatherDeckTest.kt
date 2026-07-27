package com.kinplay.app.wyr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

import org.junit.Assert.assertTrue
import org.junit.Test

class WouldYouRatherDeckTest {
    private val library = testLibrary()

    @Test
    fun sameSeedProducesTheSameShuffledSequence() {
        val first = WouldYouRatherDeck(library, seed = 8675309L)
        val second = WouldYouRatherDeck(library, seed = 8675309L)

        val firstSequence = List(160) { first.draw("cute_silly").id }
        val secondSequence = List(160) { second.draw("cute_silly").id }

        assertEquals(firstSequence, secondSequence)
        assertNotEquals(library.category("cute_silly")!!.prompts.map { it.id }, firstSequence.take(80))
    }

    @Test
    fun fixedTinyLibraryAndSeedHaveAGoldenSequence() {
        val tinyLibrary = library.copy(
            categories = listOf(
                WouldYouRatherCategory(
                    id = "tiny",
                    title = "Tiny",
                    order = 1,
                    prompts = (1..5).map { number ->
                        WouldYouRatherPrompt(
                            id = "p$number",
                            text = "Would you rather choose tiny option $number A or choose tiny option $number B?",
                            status = WouldYouRatherPromptStatus.APPROVED,
                        )
                    },
                ),
            ),
        )
        val deck = WouldYouRatherDeck(tinyLibrary, seed = 20260726L)

        assertEquals(
            listOf("p1", "p5", "p3", "p2", "p4", "p1", "p4", "p2", "p5", "p3"),
            List(10) { deck.draw("tiny").id },
        )
    }

    @Test
    fun eachEightyPromptCycleHasNoRepeatsAndCycleBoundaryDoesNotRepeat() {
        val deck = WouldYouRatherDeck(library, seed = 42L)
        val draws = List(161) { deck.draw("animals").id }

        assertEquals(80, draws.take(80).toSet().size)
        assertEquals(80, draws.drop(80).take(80).toSet().size)
        assertNotEquals(draws[79], draws[80])
        assertNotEquals(draws[159], draws[160])
    }

    @Test
    fun categoryBagsAndRandomStreamsAreIsolated() {
        val interleaved = WouldYouRatherDeck(library, seed = 7L)
        val onlyAnimals = WouldYouRatherDeck(library, seed = 7L)

        repeat(37) { interleaved.draw("cute_silly") }
        val animalsAfterOtherDraws = List(100) { interleaved.draw("animals").id }
        val animalsAlone = List(100) { onlyAnimals.draw("animals").id }

        assertEquals(animalsAlone, animalsAfterOtherDraws)
        assertTrue(interleaved.snapshot().remainingIdsByCategory.getValue("cute_silly").size < 80)
    }

    @Test
    fun stalePersistedIdsAreDroppedAndNewLibraryIdsAreRecoveredExactlyOnce() {
        val category = library.category("gross")!!
        val staleState = WouldYouRatherDeckState(
            remainingIdsByCategory = mapOf(
                "gross" to listOf("missing_prompt", category.prompts[3].id, category.prompts[3].id),
                "removed_category" to listOf("also_missing"),
            ),
            lastIdByCategory = mapOf("gross" to "missing_last"),
            randomStateByCategory = mapOf("gross" to 99L),
        )
        val deck = WouldYouRatherDeck(library, seed = 123L, initialState = staleState)

        val cycle = List(80) { deck.draw("gross").id }

        assertEquals(category.prompts.map { it.id }.toSet(), cycle.toSet())
        assertEquals(80, cycle.distinct().size)
        assertEquals(setOf("gross"), deck.snapshot().remainingIdsByCategory.keys)
    }

    @Test
    fun rebuiltLegacyEmptyBagDoesNotRepeatSavedLastIdAtCycleBoundary() {
        val categoryId = "animals"
        val previousId = library.category(categoryId)!!.prompts.first().id
        val legacyState = WouldYouRatherDeckState(
            remainingIdsByCategory = mapOf(categoryId to emptyList()),
            lastIdByCategory = mapOf(categoryId to previousId),
            randomStateByCategory = mapOf(categoryId to 75L),
            libraryIdsByCategory = mapOf(categoryId to emptyList()),
        )
        val deck = WouldYouRatherDeck(library, seed = 123L, initialState = legacyState)

        assertNotEquals(previousId, deck.draw(categoryId).id)
    }

    @Test
    fun parseValidPersistedBagContainingSavedLastIdCannotRepeatAtRestoreBoundary() {
        val categoryId = "animals"
        val category = library.category(categoryId)!!
        val previousId = category.prompts.first().id
        val inconsistentState = WouldYouRatherDeckState(
            remainingIdsByCategory = mapOf(categoryId to listOf(previousId)),
            lastIdByCategory = mapOf(categoryId to previousId),
            randomStateByCategory = mapOf(categoryId to 75L),
            libraryIdsByCategory = mapOf(categoryId to category.prompts.map { it.id }),
        )
        val decodedState = WouldYouRatherStateJson.decode(WouldYouRatherStateJson.encode(inconsistentState))
        val deck = WouldYouRatherDeck(library, seed = 123L, initialState = decodedState)
        val matchingDeck = WouldYouRatherDeck(library, seed = 999L, initialState = decodedState)

        assertEquals(inconsistentState, decodedState)
        val recoveredCycle = List(category.prompts.size) { deck.draw(categoryId).id }
        assertNotEquals(previousId, recoveredCycle.first())
        assertEquals(category.prompts.map { it.id }.toSet(), recoveredCycle.toSet())
        assertEquals(category.prompts.size, recoveredCycle.distinct().size)
        assertEquals(
            "Persisted random state must make recovery deterministic",
            recoveredCycle,
            List(category.prompts.size) { matchingDeck.draw(categoryId).id },
        )
    }

    @Test
    fun persistedStateRoundTripsAndResumesTheExactSequence() {
        val storage = InMemoryWouldYouRatherStateStorage()
        val store = WouldYouRatherStore(storage)
        val original = WouldYouRatherDeck(library, seed = 314159L)
        repeat(23) { original.draw("super_gross") }
        repeat(11) { original.draw("cute_silly") }

        store.save(original.snapshot())
        val encoded = storage.value
        val decoded = store.load()
        val restored = WouldYouRatherDeck(library, seed = 999L, initialState = decoded)

        assertTrue(encoded!!.contains("remainingIdsByCategory"))
        assertEquals(original.snapshot(), decoded)
        assertEquals(
            List(200) { original.draw("super_gross").id },
            List(200) { restored.draw("super_gross").id },
        )
        assertEquals(
            List(120) { original.draw("cute_silly").id },
            List(120) { restored.draw("cute_silly").id },
        )
    }

    @Test
    fun malformedPersistedJsonIsRecoverableAndLoadsAsEmptyState() {
        listOf("{", "[]", "null", "not json").forEach { malformed ->
            assertEquals(WouldYouRatherDeckState(), WouldYouRatherStateJson.decode(malformed))
            assertEquals(
                WouldYouRatherDeckState(),
                WouldYouRatherStore(InMemoryWouldYouRatherStateStorage(malformed)).load(),
            )
        }
    }

    @Test
    fun missingOrWrongTypedPersistedFieldsAreRecoverableAndLoadAsEmptyState() {
        val invalidStates = listOf(
            // Missing required map.
            """{"version":1,"remainingIdsByCategory":{},"lastIdByCategory":{},"randomStateByCategory":{}}""",
            // Version and nested values are deliberately strict; strings must not be coerced.
            """{"version":"1","remainingIdsByCategory":{},"lastIdByCategory":{},"randomStateByCategory":{},"libraryIdsByCategory":{}}""",
            """{"version":1,"remainingIdsByCategory":{"animals":"not-an-array"},"lastIdByCategory":{},"randomStateByCategory":{},"libraryIdsByCategory":{}}""",
            """{"version":1,"remainingIdsByCategory":{"animals":[1]},"lastIdByCategory":{},"randomStateByCategory":{},"libraryIdsByCategory":{}}""",
            """{"version":1,"remainingIdsByCategory":{},"lastIdByCategory":{"animals":7},"randomStateByCategory":{},"libraryIdsByCategory":{}}""",
            """{"version":1,"remainingIdsByCategory":{},"lastIdByCategory":{},"randomStateByCategory":{"animals":"7"},"libraryIdsByCategory":{}}""",
        )

        invalidStates.forEach { invalid ->
            assertEquals(
                "Expected invalid state to decode as empty: $invalid",
                WouldYouRatherDeckState(),
                WouldYouRatherStateJson.decode(invalid),
            )
            assertEquals(
                WouldYouRatherDeckState(),
                WouldYouRatherStore(InMemoryWouldYouRatherStateStorage(invalid)).load(),
            )
        }
    }

    @Test
    fun unsupportedFuturePersistedVersionIsRecoverableAndLoadsAsEmptyState() {
        val futureState = """{"version":2,"remainingIdsByCategory":{},"lastIdByCategory":{},"randomStateByCategory":{},"libraryIdsByCategory":{}}"""

        assertEquals(WouldYouRatherDeckState(), WouldYouRatherStateJson.decode(futureState))
        assertEquals(
            WouldYouRatherDeckState(),
            WouldYouRatherStore(InMemoryWouldYouRatherStateStorage(futureState)).load(),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownCategoryIsRejected() {
        WouldYouRatherDeck(library, seed = 1L).draw("unknown")
    }

    private fun testLibrary(): WouldYouRatherLibrary = WouldYouRatherLibrary(
        schemaVersion = 1,
        libraryId = "would_you_rather_v1",
        title = "Would You Rather",
        locale = "en-US",
        origin = WouldYouRatherOrigin("original_kinplay_editorial_work", "test"),
        categories = listOf("cute_silly", "animals", "gross", "super_gross").mapIndexed { categoryIndex, categoryId ->
            WouldYouRatherCategory(
                id = categoryId,
                title = categoryId,
                order = categoryIndex + 1,
                prompts = (1..80).map { number ->
                    WouldYouRatherPrompt(
                        id = "wyr_${categoryId}_${number.toString().padStart(3, '0')}",
                        text = "Would you rather choose option $number A or choose option $number B?",
                        status = WouldYouRatherPromptStatus.APPROVED,
                    )
                },
            )
        },
    )
}
