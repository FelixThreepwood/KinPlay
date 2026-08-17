package com.kinplay.app

import com.google.gson.JsonParser
import java.nio.file.Files
import java.nio.file.Path
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChildHandoffEligibilityTest {
    @Test
    fun pureEligibilityHelperUsesTheReviewedItemDecision() {
        val eligible = testItem.copy(childHandoffLockEligible = true)
        val ineligible = testItem.copy(childHandoffLockEligible = false)

        assertTrue(shouldShowChildHandoffLock(eligible))
        assertFalse(shouldShowChildHandoffLock(ineligible))
    }

    @Test
    fun everyActiveSeedItemHasAnExplicitReviewedDecision() {
        val root = Files.newBufferedReader(repositoryRoot().resolve("content/seed/kinplay_seed_v1.json")).use {
            JsonParser.parseReader(it).asJsonObject
        }
        val activeItems = root.getAsJsonArray("items").map { it.asJsonObject }
            .filter { it.get("status").asString == "active" }

        assertEquals(54, activeItems.size)
        assertTrue(activeItems.all { it.has("childHandoffLockEligible") && it.get("childHandoffLockEligible").isJsonPrimitive })
        assertEquals(
            setOf("family_charades_animals", WOULD_YOU_RATHER_ITEM_ID),
            activeItems.filter { it.get("childHandoffLockEligible").asBoolean }.map { it.get("id").asString }.toSet(),
        )
    }

    @Test
    fun eligibilityMatrixCoversEveryActiveItemAndMatchesTheSeed() {
        val root = Files.newBufferedReader(repositoryRoot().resolve("content/seed/kinplay_seed_v1.json")).use {
            JsonParser.parseReader(it).asJsonObject
        }
        val matrix = Files.newBufferedReader(repositoryRoot().resolve("docs/testing/feedback/KPF_0026_LOCK_ELIGIBILITY_MATRIX.json")).use {
            JsonParser.parseReader(it).asJsonObject
        }
        val seedDecisions = root.getAsJsonArray("items").map { it.asJsonObject }
            .filter { it.get("status").asString == "active" }
            .associate { it.get("id").asString to it.get("childHandoffLockEligible").asBoolean }
        val matrixDecisions = matrix.getAsJsonArray("entries").associate { entry ->
            entry.asJsonObject.get("itemId").asString to entry.asJsonObject.get("eligible").asBoolean
        }

        assertEquals(seedDecisions, matrixDecisions)
    }

    private val testItem = KinPlayItem(
        id = "test_item",
        type = "activity",
        status = "active",
        title = "Test item",
        summary = "A test item with enough summary text.",
        modes = listOf("pick_a_game"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 5,
        energyLevel = "calm",
    )

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
