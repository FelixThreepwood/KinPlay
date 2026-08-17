package com.kinplay.app.wyr

import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

data class WouldYouRatherLibrary(
    val schemaVersion: Int,
    val libraryId: String,
    val title: String,
    val locale: String,
    val origin: WouldYouRatherOrigin,
    val categories: List<WouldYouRatherCategory>,
) {
    private val categoriesById = categories.associateBy { it.id }

    fun category(id: String): WouldYouRatherCategory? = categoriesById[id]
}

data class WouldYouRatherOrigin(
    val type: String,
    val statement: String,
)

data class WouldYouRatherCategory(
    val id: String,
    val title: String,
    val order: Int,
    val prompts: List<WouldYouRatherPrompt>,
)

data class WouldYouRatherPrompt(
    val id: String,
    val text: String,
    val status: WouldYouRatherPromptStatus,
)

enum class WouldYouRatherPromptStatus(val serializedName: String) {
    APPROVED("approved");

    companion object {
        fun parse(value: String): WouldYouRatherPromptStatus = entries.singleOrNull { it.serializedName == value }
            ?: throw IllegalArgumentException("Unknown prompt status: $value")
    }
}

object WouldYouRatherLibraryParser {
    private const val SUPPORTED_SCHEMA_VERSION = 1
    private const val LIBRARY_ID = "would_you_rather_v1"
    private const val LIBRARY_TITLE = "Would You Rather"
    private const val LIBRARY_LOCALE = "en-US"
    private const val ORIGIN_TYPE = "original_kinplay_editorial_work"
    private const val ORIGIN_STATEMENT =
        "Original KinPlay editorial work; no external prompt collection was used or copied."
    private data class ExpectedCategory(
        val id: String,
        val title: String,
        val order: Int,
        val promptCount: Int,
    )

    private val expectedCategories = listOf(
        ExpectedCategory("cute_silly", "Cute & Silly", 1, 40),
        ExpectedCategory("animals", "Animals", 2, 40),
        ExpectedCategory("gross", "Gross", 3, 40),
        ExpectedCategory("super_gross", "Super Gross", 4, 40),
    )

    fun parse(json: String): WouldYouRatherLibrary = try {
        val root = JSONObject(json)
        val schemaVersion = root.strictInt("schemaVersion")
        require(schemaVersion == SUPPORTED_SCHEMA_VERSION) {
            "Unsupported schemaVersion: $schemaVersion"
        }

        val libraryId = root.strictString("libraryId", "library metadata libraryId")
        val title = root.strictString("title")
        val locale = root.strictString("locale")
        val originJson = root.strictObject("origin")
        val origin = WouldYouRatherOrigin(
            type = originJson.strictString("type"),
            statement = originJson.strictString("statement"),
        )
        require(
            libraryId == LIBRARY_ID &&
                title == LIBRARY_TITLE &&
                locale == LIBRARY_LOCALE &&
                origin.type == ORIGIN_TYPE &&
                origin.statement == ORIGIN_STATEMENT
        ) { "Invalid Would You Rather library metadata" }

        val categoriesJson = root.strictArray("categories")
        require(categoriesJson.length() == expectedCategories.size) {
            "categories must contain exactly ${expectedCategories.size} ordered categories"
        }
        val categories = categoriesJson.mapObjects { index, category ->
            parseCategory(category, expectedCategories[index])
        }

        require(categories.map { it.id }.distinct().size == categories.size) { "Duplicate category ID" }
        val prompts = categories.flatMap { it.prompts }
        require(prompts.map { it.id }.distinct().size == prompts.size) { "Duplicate prompt ID" }
        require(prompts.map { normalizePromptText(it.text) }.distinct().size == prompts.size) {
            "Duplicate prompt text"
        }

        WouldYouRatherLibrary(
            schemaVersion = schemaVersion,
            libraryId = libraryId,
            title = title,
            locale = locale,
            origin = origin,
            categories = categories,
        )
    } catch (error: IllegalArgumentException) {
        throw error
    } catch (error: Exception) {
        throw IllegalArgumentException("Invalid Would You Rather library: ${error.message}", error)
    }

    private fun parseCategory(category: JSONObject, expected: ExpectedCategory): WouldYouRatherCategory {
        val id = category.strictString("id")
        val title = category.strictString("title")
        val order = category.strictInt("order")
        require(id == expected.id && title == expected.title && order == expected.order) {
            "Invalid ordered category at position ${expected.order}: expected ${expected.id}"
        }

        val promptsJson = category.strictArray("prompts")
        require(promptsJson.length() == expected.promptCount) {
            "Category $id must contain exactly ${expected.promptCount} prompts"
        }
        val prompts = promptsJson.mapObjects { _, prompt ->
            val promptId = prompt.strictString("id", "prompt ID")
            val text = prompt.strictString("text", "prompt text")
            val statusText = prompt.strictString("status", "prompt status")
            val promptNumber = promptId
                .removePrefix("wyr_${expected.id}_")
                .toIntOrNull()
            require(
                promptId.startsWith("wyr_${expected.id}_") &&
                    promptNumber != null &&
                    promptNumber in 1..999
            ) { "Invalid prompt ID: $promptId" }
            validatePromptText(promptId, text)
            val status = WouldYouRatherPromptStatus.parse(statusText)
            WouldYouRatherPrompt(id = promptId, text = text, status = status)
        }
        require(prompts.map { it.id }.distinct().size == prompts.size) { "Duplicate prompt ID" }
        return WouldYouRatherCategory(id = id, title = title, order = order, prompts = prompts)
    }

    private fun validatePromptText(promptId: String, text: String) {
        val length = text.codePointCount(0, text.length)
        val boundaryCount = Regex(" or ").findAll(text).count()
        require(
            length in 24..180 &&
                text.startsWith("Would you rather ") &&
                text.endsWith("?") &&
                boundaryCount == 1
        ) { "Invalid prompt text for $promptId" }
        val options = text.removePrefix("Would you rather ").removeSuffix("?").split(" or ")
        require(options.size == 2 && options.all { it.isNotBlank() }) { "Invalid prompt text for $promptId" }
    }

    private fun JSONObject.strictString(name: String, description: String = name): String = get(name).let { value ->
        require(value is String) { "$description must be a string" }
        value
    }

    private fun JSONObject.strictInt(name: String): Int = get(name).let { value ->
        require(value is Int) { "$name must be an integer" }
        value
    }

    private fun JSONObject.strictObject(name: String): JSONObject = get(name).let { value ->
        require(value is JSONObject) { "$name must be an object" }
        value
    }

    private fun JSONObject.strictArray(name: String): JSONArray = get(name).let { value ->
        require(value is JSONArray) { "$name must be an array" }
        value
    }

    private inline fun <T> JSONArray.mapObjects(transform: (Int, JSONObject) -> T): List<T> =
        (0 until length()).map { index ->
            val value = get(index)
            require(value is JSONObject) { "Array item $index must be an object" }
            transform(index, value)
        }

    private fun normalizePromptText(text: String): String = text
        .lowercase(Locale.ROOT)
        .replace(Regex("\\s+"), " ")
        .trim()
        .trimEnd('?', '.', '!')
}
