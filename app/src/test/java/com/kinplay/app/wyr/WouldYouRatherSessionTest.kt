package com.kinplay.app.wyr

import com.kinplay.app.WOULD_YOU_RATHER_ITEM_ID
import com.kinplay.app.isWouldYouRatherItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WouldYouRatherSessionTest {
    private val library = testLibrary()

    @Test
    fun exposesExactlyTheFourOrderedCategoryChoices() {
        val session = WouldYouRatherSession(
            library = library,
            store = WouldYouRatherStore(InMemoryWouldYouRatherStateStorage()),
            seed = 41L,
        )

        assertEquals(
            listOf(
                "cute_silly" to "Cute & Silly",
                "animals" to "Animals",
                "gross" to "Gross",
                "super_gross" to "Super Gross",
            ),
            session.categories.map { it.id to it.title },
        )
    }

    @Test
    fun eachOneTapAdvanceDrawsOnceAndPersistsBeforeReturning() {
        val storage = RecordingStorage()
        val session = WouldYouRatherSession(
            library = library,
            store = WouldYouRatherStore(storage),
            seed = 73L,
        )

        val first = session.nextPrompt("animals")
        assertEquals(1, storage.writeCount)
        assertEquals(first.id, WouldYouRatherStateJson.decode(storage.value!!).lastIdByCategory["animals"])

        val second = session.nextPrompt("animals")
        assertEquals(2, storage.writeCount)
        assertNotEquals(first.id, second.id)
        assertEquals(second.id, WouldYouRatherStateJson.decode(storage.value!!).lastIdByCategory["animals"])
    }

    @Test
    fun aNewSessionResumesPersistedCategoryProgressWithoutARepeat() {
        val storage = InMemoryWouldYouRatherStateStorage()
        val firstSession = WouldYouRatherSession(library, WouldYouRatherStore(storage), seed = 17L)
        val seen = List(19) { firstSession.nextPrompt("gross").id }

        val restored = WouldYouRatherSession(library, WouldYouRatherStore(storage), seed = 999L)
        val resumed = restored.nextPrompt("gross").id

        assertFalse(resumed in seen)
        assertEquals(20, seen.plus(resumed).distinct().size)
    }

    @Test
    fun wouldYouRatherContentUsesTheDedicatedFullScreenDestination() {
        assertEquals("would_you_rather_silly_family", WOULD_YOU_RATHER_ITEM_ID)
        assertTrue(isWouldYouRatherItem(WOULD_YOU_RATHER_ITEM_ID))
        assertFalse(isWouldYouRatherItem("another_activity"))
    }

    @Test
    fun configuredPromptTransitionCompletesWithinTwoSeconds() {
        assertTrue(WOULD_YOU_RATHER_PROMPT_FADE_MILLIS in 1 until 2_000)
    }

    private class RecordingStorage : WouldYouRatherStateStorage {
        var value: String? = null
        var writeCount = 0

        override fun read(): String? = value

        override fun write(value: String) {
            writeCount += 1
            this.value = value
        }
    }

    private fun testLibrary(): WouldYouRatherLibrary = WouldYouRatherLibrary(
        schemaVersion = 1,
        libraryId = "would_you_rather_v1",
        title = "Would You Rather",
        locale = "en-US",
        origin = WouldYouRatherOrigin("original_kinplay_editorial_work", "test"),
        categories = listOf(
            "cute_silly" to "Cute & Silly",
            "animals" to "Animals",
            "gross" to "Gross",
            "super_gross" to "Super Gross",
        ).mapIndexed { index, (id, title) ->
            WouldYouRatherCategory(
                id = id,
                title = title,
                order = index + 1,
                prompts = (1..80).map { number ->
                    WouldYouRatherPrompt(
                        id = "wyr_${id}_${number.toString().padStart(3, '0')}",
                        text = "Would you rather choose $id option $number A or choose $id option $number B?",
                        status = WouldYouRatherPromptStatus.APPROVED,
                    )
                },
            )
        },
    )
}
