package com.kinplay.app

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class CardMetadataFoundationTest {
    private val repositoryRoot = repositoryRoot()
    private val canonicalPath = repositoryRoot.resolve("content/seed/kinplay_seed_v1.json")
    private val runtimePath = repositoryRoot.resolve("app/src/main/assets/kinplay_seed_v1.json")
    private val schemaPath = repositoryRoot.resolve("content/kinplay-content.schema.json")
    private val mapper = ObjectMapper()

    private val root: JsonObject by lazy {
        Files.newBufferedReader(canonicalPath).use { JsonParser.parseReader(it).asJsonObject }
    }

    private val activeItems: List<JsonObject>
        get() = root.getAsJsonArray("items")
            .map { it.asJsonObject }
            .filter { it.string("status") == "active" }

    @Test
    fun kpf0008EveryActiveRealCardHasReviewedParticipantSuitability() {
        val allowed = setOf("one_on_one", "group", "both")

        assertEquals("Active-card inventory changed; review every new or removed card", 54, activeItems.size)
        activeItems.forEach { item ->
            assertTrue("${item.string("id")} needs reviewed participantSuitability", item.has("participantSuitability"))
            assertTrue(
                "${item.string("id")} has invalid participantSuitability",
                item.string("participantSuitability") in allowed,
            )
        }
    }

    @Test
    fun kpf0008SchemaRejectsAnActiveCardWithoutParticipantSuitability() {
        val candidate = canonicalNode().deepCopy<ObjectNode>()
        val activeItemOutsideQualityTime = candidate.withArray("items")
            .first { item ->
                item.path("status").asText() == "active" &&
                    item.path("quickCategories").none { it.asText() == "quality_time" }
            } as ObjectNode
        activeItemOutsideQualityTime.remove("participantSuitability")

        val errors = contentSchema().validate(candidate)

        assertTrue(
            "The schema must require participantSuitability for every active card, not only Quality Time; errors=$errors",
            errors.any { it.message.contains("participantSuitability") },
        )
    }

    @Test
    fun kpf0008ParserRejectsMissingOrUnknownActiveParticipantSuitability() {
        val missing = activeItemJson()
        val unknown = activeItemJson().put("participantSuitability", "everyone")

        assertThrows(IllegalArgumentException::class.java) { KinPlayItem.fromJson(missing) }
        assertThrows(IllegalArgumentException::class.java) { KinPlayItem.fromJson(unknown) }
    }

    @Test
    fun kpf0008ParserRejectsMissingOrUnknownQualityTimeDraftParticipantSuitability() {
        val missing = draftItemJson(quickCategories = listOf("quality_time"))
        val unknown = draftItemJson(quickCategories = listOf("quality_time"))
            .put("participantSuitability", "everyone")

        assertThrows(IllegalArgumentException::class.java) { KinPlayItem.fromJson(missing) }
        assertThrows(IllegalArgumentException::class.java) { KinPlayItem.fromJson(unknown) }
    }

    @Test
    fun kpf0008ParserAllowsMissingParticipantSuitabilityForNonQualityTimeDraft() {
        val parsed = KinPlayItem.fromJson(draftItemJson(quickCategories = listOf("brain_games")))

        assertEquals(null, parsed.participantSuitability)
    }

    @Test
    fun kpf0008ParserPreservesEveryReviewedWireValue() {
        val expected = mapOf(
            "one_on_one" to ParticipantSuitability.ONE_ON_ONE,
            "group" to ParticipantSuitability.GROUP,
            "both" to ParticipantSuitability.BOTH,
        )

        expected.forEach { (wireValue, suitability) ->
            val parsed = KinPlayItem.fromJson(activeItemJson().put("participantSuitability", wireValue))
            assertEquals(suitability, parsed.participantSuitability)
        }
    }

    @Test
    fun kpf0022EveryActiveRealCardHasAConciseUsefulSummarySeparateFromSetupPreview() {
        val genericPlaceholders = setOf("fun activity.", "fun game.", "an activity.", "a game.", "description.")
        val summaries = mutableSetOf<String>()

        assertEquals("Active-card inventory changed; review every new or removed card", 54, activeItems.size)
        activeItems.forEach { item ->
            val id = item.string("id")
            val summary = item.string("summary")
            val codePointLength = summary.codePointCount(0, summary.length)
            val words = summary.trim().split(Regex("\\s+")).filter { it.isNotBlank() }

            assertTrue("$id needs a meaningful summary of at least 20 characters", codePointLength >= 20)
            assertTrue("$id summary is not concise at $codePointLength characters", codePointLength <= 120)
            assertTrue("$id summary needs enough specific copy to be useful", words.size >= 5)
            assertTrue("$id summary must be a complete sentence", summary.endsWith('.') || summary.endsWith('!') || summary.endsWith('?'))
            assertFalse("$id summary is generic placeholder copy", summary.lowercase() in genericPlaceholders)
            assertFalse("$id summary must remain separate from setup/material preview", summary.startsWith("Setup:") || summary.startsWith("Needs:") || summary.startsWith("Materials:"))
            assertTrue("$id summary must be unique real content", summaries.add(summary))
        }
    }

    @Test
    fun shippedMadLibSummariesPreserveTheirPremisesAndExplainTheWordChoiceMechanic() {
        val expectedSummaries = mapOf(
            "moon_pancake_mission" to "Choose words to create and read a silly space breakfast story.",
            "dragon_laundry_day" to "Choose words to create and read a silly story about a dragon helping with chores.",
            "backyard_castle_news" to "Choose words to create and read a silly story about a royal announcement from a pretend castle.",
            "wizard_pajama_parade" to "Choose words to create and read a silly story about a sleepy parade.",
            "robot_cookie_factory" to "Choose words to create and read a silly story about a robot baking pretend cookies with a surprise.",
            "undersea_bedtime_bus" to "Choose words to create and read a silly story about a bus driving under the sea at bedtime.",
            "castle_pizza_problem" to "Choose words to create and read a silly story about a castle feast needing a strange solution.",
            "space_pet_show" to "Choose words to create and read a silly story about pets competing in a zero-gravity show.",
            "breakfast_dragon_train" to "Choose words to create and read a silly story about a dragon train delivering breakfast.",
        )
        val shippedSummaries = activeItems
            .filter { it.string("type") == "mad_libs" }
            .associate { it.string("id") to it.string("summary") }

        assertEquals(expectedSummaries, shippedSummaries)
    }

    @Test
    fun kpf0022SchemaEnforcesReviewedConciseSummaryBounds() {
        val tooShort = canonicalNode().deepCopy<ObjectNode>().also {
            (it.withArray("items")[0] as ObjectNode).put("summary", "Fun.")
        }
        val tooLong = canonicalNode().deepCopy<ObjectNode>().also {
            (it.withArray("items")[0] as ObjectNode).put("summary", "A".repeat(121))
        }

        assertTrue("Schema must reject vague short summaries", contentSchema().validate(tooShort).isNotEmpty())
        assertTrue("Schema must reject summaries over 120 characters", contentSchema().validate(tooLong).isNotEmpty())
    }

    @Test
    fun everyActiveCardReviewExportMatchesItsSummaryAndParticipantSuitability() {
        val reviewFiles = mapOf(
            "activity" to repositoryRoot.resolve("docs/content/review/activities.txt"),
            "prompt" to repositoryRoot.resolve("docs/content/review/prompts.txt"),
            "mad_libs" to repositoryRoot.resolve("docs/content/review/mad-libs.txt"),
        ).mapValues { (_, path) -> path.readUtf8() }
        val index = repositoryRoot.resolve("docs/content/review/content-index.txt").readUtf8()

        activeItems.forEach { item ->
            val id = item.string("id")
            val summary = item.string("summary")
            val suitability = item.string("participantSuitability")
            val section = reviewSection(reviewFiles.getValue(item.string("type")), id)

            assertTrue("$id review export needs its exact summary", section.contains("- Summary: $summary"))
            assertTrue(
                "$id review export needs exact participant suitability",
                section.contains("- Participant suitability: $suitability"),
            )
            assertTrue("$id content index needs its exact summary", index.contains("— $summary"))
            assertTrue("$id content index needs participant fit", index.contains("— $summary — Participant fit: $suitability"))
        }
    }

    @Test
    fun canonicalAndRuntimeCardMetadataRemainByteIdentical() {
        assertTrue(Files.readAllBytes(canonicalPath).contentEquals(Files.readAllBytes(runtimePath)))
    }

    private fun contentSchema(): JsonSchema = JsonSchemaFactory
        .getInstance(SpecVersion.VersionFlag.V202012)
        .getSchema(mapper.readTree(schemaPath.readUtf8()))

    private fun canonicalNode(): JsonNode = mapper.readTree(canonicalPath.readUtf8())

    private fun activeItemJson(): JSONObject = JSONObject()
        .put("id", "parser_card")
        .put("type", "activity")
        .put("status", "active")
        .put("title", "Parser Card")
        .put("summary", "Exercise the participant metadata parser contract.")
        .put("modes", org.json.JSONArray().put("pick_a_game"))
        .put("minAge", 2)
        .put("maxAge", 8)
        .put("durationMinutes", 5)
        .put("energyLevel", "low")

    private fun draftItemJson(quickCategories: List<String>): JSONObject = activeItemJson()
        .put("status", "draft")
        .put(
            "quickCategories",
            org.json.JSONArray().also { categories -> quickCategories.forEach(categories::put) },
        )

    private fun JsonObject.string(name: String): String = get(name)?.asString.orEmpty()

    private fun reviewSection(review: String, id: String): String {
        val idMarker = "- ID: `$id`"
        val idIndex = review.indexOf(idMarker)
        assertTrue("Review export is missing $id", idIndex >= 0)
        val sectionStart = review.lastIndexOf("\n## ", idIndex).coerceAtLeast(0)
        val nextSection = review.indexOf("\n## ", idIndex)
        return review.substring(sectionStart, if (nextSection < 0) review.length else nextSection)
    }

    private fun Path.readUtf8(): String = String(Files.readAllBytes(this), Charsets.UTF_8)

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
