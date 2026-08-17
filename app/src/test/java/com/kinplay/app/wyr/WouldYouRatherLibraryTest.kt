package com.kinplay.app.wyr

import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.Locale

class WouldYouRatherLibraryTest {
    private val root = repositoryRoot()
    private val canonicalPath = root.resolve("content/seed/would_you_rather_v1.json")
    private val runtimePath = root.resolve("app/src/main/assets/would_you_rather_v1.json")
    private val reviewPath = root.resolve("docs/content/review/would-you-rather-review.json")

    @Test
    fun canonicalLibraryHasFourOrderedCategoriesWithReviewedPromptCounts() {
        val library = WouldYouRatherLibraryParser.parse(canonicalPath.readUtf8())

        assertEquals(1, library.schemaVersion)
        assertEquals("would_you_rather_v1", library.libraryId)
        assertEquals(
            listOf(
                "cute_silly" to "Cute & Silly",
                "animals" to "Animals",
                "gross" to "Gross",
                "super_gross" to "Super Gross",
            ),
            library.categories.map { it.id to it.title },
        )
        val expectedCounts = mapOf(
            "cute_silly" to 40,
            "animals" to 40,
            "gross" to 40,
            "super_gross" to 40,
        )
        library.categories.forEachIndexed { index, category ->
            assertEquals(index + 1, category.order)
            assertEquals("${category.id} must contain exactly ${expectedCounts.getValue(category.id)} prompts", expectedCounts.getValue(category.id), category.prompts.size)
            assertTrue("${category.id} must be fully approved", category.prompts.all { it.status == WouldYouRatherPromptStatus.APPROVED })
        }
    }

    @Test
    fun projectOwnerPromptSetIsPresentInReviewedCategories() {
        val promptsByText = parseCanonical().categories
            .flatMap { candidate -> candidate.prompts.map { it.text to candidate.id } }
            .toMap()
        val expected = mapOf(
            "Would you rather fly or be invisible?" to "cute_silly",
            "Would you rather talk to animals or read minds?" to "animals",
            "Would you rather have super strength or super speed?" to "cute_silly",
            "Would you rather live underwater or in space?" to "cute_silly",
            "Would you rather never sleep or never eat?" to "cute_silly",
            "Would you rather be always hot or always cold?" to "cute_silly",
            "Would you rather have a pet dinosaur or a pet dragon?" to "animals",
            "Would you rather jump like a frog or swim like a fish?" to "animals",
            "Would you rather eat only pizza or only ice cream?" to "cute_silly",
            "Would you rather shrink to ant size or grow to giant size?" to "animals",
            "Would you rather control the weather or control time?" to "cute_silly",
            "Would you rather be a superhero or a wizard?" to "cute_silly",
            "Would you rather never get tired or never get sick?" to "cute_silly",
            "Would you rather have wings or a tail?" to "animals",
            "Would you rather live in a treehouse or a castle?" to "cute_silly",
            "Would you rather speak every language or play every instrument?" to "cute_silly",
            "Would you rather have night vision or x-ray vision?" to "animals",
            "Would you rather be able to teleport or stop time?" to "cute_silly",
            "Would you rather have infinite candy or infinite toys?" to "cute_silly",
            "Would you rather run on water or walk through walls?" to "cute_silly",
        )

        assertEquals(expected.size, expected.keys.intersect(promptsByText.keys).size)
        expected.forEach { (text, categoryId) -> assertEquals(categoryId, promptsByText[text]) }
    }

    @Test
    fun allReducedStableIdsAndNormalizedTextsAreUniqueAndReadyToRead() {
        val prompts = parseCanonical().categories.flatMap { it.prompts }
        val idPattern = Regex("^wyr_(cute_silly|animals|gross|super_gross)_[0-9]{3}$")

        assertEquals(160, prompts.size)
        assertEquals(160, prompts.map { it.id }.toSet().size)
        assertEquals(160, prompts.map { normalize(it.text) }.toSet().size)
        prompts.forEach { prompt ->
            assertTrue("Invalid stable prompt ID ${prompt.id}", prompt.id.matches(idPattern))
            assertTrue("${prompt.id} must be ready to read", prompt.text.startsWith("Would you rather ") && prompt.text.endsWith("?"))
            assertTrue("${prompt.id} is too long for the distraction-free surface", prompt.text.codePointCount(0, prompt.text.length) <= 180)
            assertTrue("${prompt.id} must present exactly one choice boundary", Regex("\\sor\\s", RegexOption.IGNORE_CASE).findAll(prompt.text).count() == 1)
        }
    }

    @Test
    fun everyRenderedPromptPassesMachineCheckableChildSafetyGuardrails() {
        val forbidden = Regex(
            "\\b(sex|sexy|naked|nude|damn|hell|crap|stupid|idiot|hate|kill|killed|dead|death|die|dying|blood|bloody|weapon|gun|knife|punch|kick|hurt|injury|injured|bully|ugly|fat|skinny|race|religion|disabled|disability|disease|poison|medicine|alcohol|beer|wine|smoke|cigarette|brand|password|address|phone number|email|excrement|poop|pee|urine|vomit|puke|genital|butt)\\b",
            setOf(RegexOption.IGNORE_CASE),
        )
        val superGrossOnly = Regex("\\b(burp|burps|burping|fart|farts|booger|boogers|stinky sock|stinky socks)\\b", RegexOption.IGNORE_CASE)

        parseCanonical().categories.forEach { category ->
            category.prompts.forEach { prompt ->
                assertTrue("${prompt.id} contains prohibited material: ${forbidden.find(prompt.text)?.value}", !forbidden.containsMatchIn(prompt.text))
                if (category.id != "super_gross") {
                    assertTrue("${prompt.id} uses Super Gross material outside that category", !superGrossOnly.containsMatchIn(prompt.text))
                }
            }
        }
    }

    @Test
    fun canonicalAndRuntimeAssetsAreByteIdentical() {
        assertTrue(Files.readAllBytes(canonicalPath).contentEquals(Files.readAllBytes(runtimePath)))
    }

    @Test
    fun reviewRecordProvidesApprovedOriginalProvenanceForEveryExactPrompt() {
        val prompts = parseCanonical().categories.flatMap { category -> category.prompts.map { category.id to it } }
        val review = JsonParser.parseString(reviewPath.readUtf8()).asJsonObject
        val provenance = review.getAsJsonObject("provenance")
        val entries = review.getAsJsonArray("prompts").map { it.asJsonObject }

        assertEquals("would_you_rather_v1", review.get("libraryId").asString)
        assertEquals("original_kinplay_editorial_work", provenance.get("origin").asString)
        assertEquals("No external prompt collection was used or copied.", provenance.get("statement").asString)
        assertEquals("2026-08-17", review.get("reviewedOn").asString)
        assertEquals(160, entries.size)
        assertEquals(160, entries.map { it.get("id").asString }.toSet().size)

        val byId = entries.associateBy { it.get("id").asString }
        prompts.forEach { (categoryId, prompt) ->
            val entry = byId[prompt.id] ?: throw AssertionError("Missing review entry for ${prompt.id}")
            assertEquals(categoryId, entry.get("categoryId").asString)
            assertEquals("approved", entry.get("status").asString)
            assertEquals("original_kinplay_editorial_work", entry.get("origin").asString)
            val expectedReviewedOn = if (entry.has("source")) "2026-08-05" else "2026-08-17"
            assertEquals(expectedReviewedOn, entry.get("reviewedOn").asString)
            assertEquals("passed", entry.get("safetyReview").asString)
            if (entry.has("source")) {
                assertEquals("project_owner_supplied_message_2026-08-05", entry.get("source").asString)
            }
            assertEquals(sha256(prompt.text), entry.get("textSha256").asString)
        }
    }

    @Test
    fun parserRejectsUnknownPromptApprovalStatus() {
        val invalid = canonicalPath.readUtf8().replaceFirst("\"approved\"", "\"pending\"")

        try {
            WouldYouRatherLibraryParser.parse(invalid)
            fail("Expected parser to reject an unknown status")
        } catch (expected: IllegalArgumentException) {
            assertTrue(expected.message.orEmpty().contains("status"))
        }
    }

    @Test
    fun parserRejectsDistinctIdsWithDuplicateNormalizedPromptText() {
        val duplicateTextLibrary = org.json.JSONObject(canonicalPath.readUtf8()).also { root ->
            val prompts = root.getJSONArray("categories").getJSONObject(0).getJSONArray("prompts")
            val firstText = prompts.getJSONObject(0).getString("text")
            // Both texts retain the canonical, parser-valid shape; normalization alone makes them equal.
            prompts.getJSONObject(1).put("text", firstText.replaceFirst("Would you rather ", "Would you rather  "))
        }.toString()

        try {
            WouldYouRatherLibraryParser.parse(duplicateTextLibrary)
            fail("Expected parser to reject duplicate normalized prompt text")
        } catch (expected: IllegalArgumentException) {
            assertTrue(expected.message.orEmpty().contains("Duplicate prompt text"))
        }
    }

    private fun parseCanonical(): WouldYouRatherLibrary =
        WouldYouRatherLibraryParser.parse(canonicalPath.readUtf8())

    private fun normalize(text: String): String = text
        .lowercase(Locale.ROOT)
        .replace(Regex("\\s+"), " ")
        .trim()
        .trimEnd('?', '.', '!')

    private fun sha256(text: String): String = MessageDigest.getInstance("SHA-256")
        .digest(text.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    private fun Path.readUtf8(): String = String(Files.readAllBytes(this), Charsets.UTF_8)

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/kinplay-content.schema.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
