package com.kinplay.app.wyr

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale

class WouldYouRatherHardeningTest {
    private val root = repositoryRoot()
    private val canonicalPath = root.resolve("content/seed/would_you_rather_v1.json")
    private val schemaPath = root.resolve("content/would-you-rather.schema.json")
    private val jackson = ObjectMapper()

    @Test
    fun parserRejectsUnsupportedSchemaVersion() {
        assertParserRejects("schemaVersion") { it.put("schemaVersion", 2) }
    }

    @Test
    fun parserRejectsWrongOrWrongTypedLibraryMetadata() {
        val mutations: List<(JSONObject) -> Unit> = listOf(
            { it.put("libraryId", "another_library") },
            { it.put("title", "Would You Maybe") },
            { it.put("locale", "en-GB") },
            { it.getJSONObject("origin").put("type", "copied") },
            { it.getJSONObject("origin").put("statement", "unknown") },
            { it.put("libraryId", 7) },
        )

        mutations.forEach { mutation -> assertParserRejects("metadata", mutation) }
    }

    @Test
    fun parserRejectsMissingWrongOrMisorderedCategories() {
        assertParserRejects("categories") { it.remove("categories") }
        assertParserRejects("categories") { it.put("categories", "not-an-array") }
        assertParserRejects("categories") { root -> root.getJSONArray("categories").remove(3) }
        assertParserRejects("category") { root ->
            root.getJSONArray("categories").getJSONObject(0).put("order", 2)
        }
    }

    @Test
    fun parserRejectsZeroPromptsAndWrongPromptCount() {
        assertParserRejects("94 prompts") { root ->
            root.getJSONArray("categories").getJSONObject(0).put("prompts", JSONArray())
        }
        assertParserRejects("94 prompts") { root ->
            root.getJSONArray("categories").getJSONObject(0).getJSONArray("prompts").remove(93)
        }
    }

    @Test
    fun parserRejectsInvalidPromptIdsTextAndStatus() {
        val mutations: List<Pair<String, (JSONObject) -> Unit>> = listOf(
            "prompt ID" to { root -> firstPrompt(root).put("id", "wyr_cute_silly_000") },
            "prompt ID" to { root -> firstPrompt(root).put("id", "wyr_animals_001") },
            "prompt text" to { root -> firstPrompt(root).put("text", "Choose this or that?") },
            "prompt text" to { root -> firstPrompt(root).put("text", "Would you rather this or that or another?") },
            "prompt text" to { root -> firstPrompt(root).put("text", "Would you rather this or that") },
            "status" to { root -> firstPrompt(root).put("status", "pending") },
            "prompt ID" to { root -> firstPrompt(root).put("id", 1) },
            "prompt text" to { root -> firstPrompt(root).put("text", true) },
        )

        mutations.forEach { (message, mutation) -> assertParserRejects(message, mutation) }
    }

    @Test
    fun parserRejectsDuplicateCategoryAndPromptIdsNeededByDeckLookup() {
        assertParserRejects("category") { root ->
            root.getJSONArray("categories").getJSONObject(1).put("id", "cute_silly")
        }
        assertParserRejects("Duplicate prompt ID") { root ->
            val prompts = root.getJSONArray("categories").getJSONObject(0).getJSONArray("prompts")
            prompts.getJSONObject(1).put("id", prompts.getJSONObject(0).getString("id"))
        }
    }

    @Test
    fun draft202012SchemaValidatesCanonicalContentAndRejectsStructuralViolations() {
        val schema = contentSchema()
        val canonicalErrors = schema.validate(jackson.readTree(canonicalPath.readUtf8()))
        assertTrue(canonicalErrors.toString(), canonicalErrors.isEmpty())

        val invalidDocuments = listOf(
            canonicalJson().put("schemaVersion", 2),
            canonicalJson().put("libraryId", "wrong"),
            canonicalJson().also { it.getJSONArray("categories").remove(3) },
            canonicalJson().also { firstPrompt(it).put("id", "wyr_cute_silly_095") },
            canonicalJson().also { firstPrompt(it).put("text", "Would you rather one or two or three?") },
            canonicalJson().also { firstPrompt(it).put("status", "pending") },
        )
        invalidDocuments.forEach { invalid ->
            val errors = schema.validate(jackson.readTree(invalid.toString()))
            assertFalse("Draft 2020-12 schema unexpectedly accepted an invalid document", errors.isEmpty())
        }
    }

    @Test
    fun duplicateIdsAndNormalizedTextRemainDomainChecksBeyondJsonSchema() {
        val schema = contentSchema()
        val duplicateId = canonicalJson().also { root ->
            val prompts = root.getJSONArray("categories").getJSONObject(0).getJSONArray("prompts")
            prompts.getJSONObject(1).put("id", prompts.getJSONObject(0).getString("id"))
        }
        val duplicateText = canonicalJson().also { root ->
            val prompts = root.getJSONArray("categories").getJSONObject(0).getJSONArray("prompts")
            prompts.getJSONObject(1).put("text", prompts.getJSONObject(0).getString("text"))
        }

        // JSON Schema cannot express uniqueness of one normalized object property. The runtime parser owns it.
        listOf(duplicateId, duplicateText).forEach { duplicate ->
            val errors = schema.validate(jackson.readTree(duplicate.toString()))
            assertTrue(errors.toString(), errors.isEmpty())
            try {
                WouldYouRatherLibraryParser.parse(duplicate.toString())
                fail("Expected domain parser to reject schema-valid semantic duplicate")
            } catch (_: IllegalArgumentException) {
                // Expected.
            }
        }
    }

    @Test
    fun everyPromptPresentsTwoDistinctReadableOptions() {
        val prompts = WouldYouRatherLibraryParser.parse(canonicalPath.readUtf8()).categories.flatMap { it.prompts }
        val optionVocabulary = mutableSetOf<String>()

        prompts.forEach { prompt ->
            val parts = prompt.text.split(" or ")
            assertEquals("${prompt.id} must have exactly one option boundary", 2, parts.size)
            val options = listOf(
                parts[0].removePrefix("Would you rather "),
                parts[1].removeSuffix("?"),
            ).map(::normalize)
            assertTrue("${prompt.id} repeats the same choice on both sides", options[0] != options[1])
            assertTrue("${prompt.id} has an unreadably short choice", options.all { it.length >= 3 })
            optionVocabulary += options
        }

        // Individual choices may intentionally recur in different pairings; the rendered
        // 340 prompt texts, not each side of the choice, are the stable unique content units.
        assertTrue("Expected a varied option vocabulary", optionVocabulary.size >= 120)
    }

    private fun contentSchema(): JsonSchema {
        val factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
        val schemaNode: JsonNode = jackson.readTree(schemaPath.readUtf8())
        return factory.getSchema(schemaNode)
    }

    private fun canonicalJson(): JSONObject = JSONObject(canonicalPath.readUtf8())

    private fun firstPrompt(root: JSONObject): JSONObject = root
        .getJSONArray("categories")
        .getJSONObject(0)
        .getJSONArray("prompts")
        .getJSONObject(0)

    private fun assertParserRejects(messageFragment: String, mutate: (JSONObject) -> Unit) {
        val invalid = canonicalJson().also(mutate).toString()
        try {
            WouldYouRatherLibraryParser.parse(invalid)
            fail("Expected parser to reject invalid library")
        } catch (expected: IllegalArgumentException) {
            assertTrue(
                "Expected '${expected.message}' to contain '$messageFragment'",
                expected.message.orEmpty().contains(messageFragment, ignoreCase = true),
            )
        }
    }

    private fun normalize(text: String): String = text
        .lowercase(Locale.ROOT)
        .replace(Regex("\\s+"), " ")
        .trim()
        .trimEnd('?', '.', '!')

    private fun Path.readUtf8(): String = String(Files.readAllBytes(this), Charsets.UTF_8)

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/kinplay-content.schema.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
