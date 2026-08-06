package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharadesLibraryTest {
    @Test
    fun bundledDeckHasExactlyFortyCardsInEachReviewedCategory() {
        val deck = CharadesDeck.fromJson(JSONObject(String(Files.readAllBytes(repositoryRoot().resolve("content/seed/charades_v1.json")))))

        assertEquals(120, deck.cards.size)
        assertEquals(setOf("animals", "activities", "objects"), deck.cards.map { it.category }.toSet())
        CHARADES_CATEGORIES.forEach { category -> assertEquals(40, deck.cardsIn(category).size) }
        assertEquals(120, deck.cards.map { it.id }.toSet().size)
        assertTrue(deck.cards.all { it.prompt.isNotBlank() })
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
