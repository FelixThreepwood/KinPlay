package com.kinplay.app

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class ContentRevisionInvariantTest {
    private val root: JsonObject by lazy {
        Files.newBufferedReader(repositoryRoot().resolve("content/seed/kinplay_seed_v1.json")).use {
            JsonParser.parseReader(it).asJsonObject
        }
    }

    private val items: List<JsonObject>
        get() = root.getAsJsonArray("items").map { it.asJsonObject }

    private val activeItems: List<JsonObject>
        get() = items.filter { it.string("status") == "active" }

    @Test
    fun realSeedAndAndroidAssetAreByteIdentical() {
        val repositoryRoot = repositoryRoot()
        assertTrue(
            Files.readAllBytes(repositoryRoot.resolve("content/seed/kinplay_seed_v1.json")).contentEquals(
                Files.readAllBytes(repositoryRoot.resolve("app/src/main/assets/kinplay_seed_v1.json")),
            ),
        )
    }

    @Test
    fun everyRealContentActivityStepFitsTheSchemaLengthLimit() {
        items.forEach { item ->
            listOf("setupSteps" to 180, "playSteps" to 240).forEach { (field, maxLength) ->
                item.strings(field).forEachIndexed { index, step ->
                    val actualLength = step.codePointCount(0, step.length)
                    assertTrue(
                        "${item.string("id")}.$field[$index] is $actualLength characters; schema maximum is $maxLength",
                        actualLength <= maxLength,
                    )
                }
            }
        }
    }

    @Test
    fun kpf0001RealSeedDisplaysExactFirstSixQuietGames() {
        val expected = listOf("I Spy", "Charades", "Would You Rather", "Animal Guessing", "Alphabet Story", "Mad Libs")
        expected.take(5).forEach { title ->
            assertEquals(1, activeItems.count { it.string("title") == title && it.strings("quickCategories").contains("quiet_games") })
        }
        assertTrue(activeItems.any { it.string("type") == "mad_libs" })

        val quietItems = activeItems.filter { it.strings("quickCategories").contains("quiet_games") }
        val familiarTitles = expected.take(5).filter { title -> quietItems.any { it.string("title") == title } }
        val remainingTitles = quietItems
            .filterNot { it.string("type") == "mad_libs" || it.string("title") in familiarTitles }
            .map { it.string("title") }
        val displayTitles = familiarTitles + "Mad Libs" + remainingTitles

        assertEquals(expected, displayTitles.take(6))
    }

    @Test
    fun alphabetStoryReadyToReadSequenceIncludesAThenBThenC() {
        val steps = activeItem("story_spark_circle").strings("playSteps")

        val aIndex = steps.indexOfFirst { it.contains("A tiny alligator") }
        val bIndex = steps.indexOfFirst { it.contains("Bouncy bears") }
        val cIndex = steps.indexOfFirst { it.contains("Curious cats") }
        assertTrue("Alphabet Story must supply the ready-to-read A sentence", aIndex >= 0)
        assertTrue("Alphabet Story must supply the ready-to-read B sentence", bIndex >= 0)
        assertTrue("Alphabet Story must place A before B and B before C", aIndex < bIndex && bIndex < cIndex)
    }

    @Test
    fun couchCushionQuestSuppliesReadyMadeObjectsAndConcreteClues() {
        val item = activeItem("couch_cushion_quest")
        val setupText = item.strings("setupSteps").joinToString(" ")
        val playText = item.strings("playSteps").joinToString(" ")

        listOf("couch cushion", "shoe", "book").forEach { choice ->
            assertTrue("Couch Cushion Quest must list the ready-made object $choice as a material", item.strings("materials").contains(choice))
            assertTrue("Couch Cushion Quest must supply the object choice $choice", setupText.contains(choice, ignoreCase = true))
        }
        assertFalse(item.strings("safetyTags").contains("no_materials"))
        listOf("Clue 1", "Clue 2", "Clue 3", "soft", "foot", "pages").forEach { clueText ->
            assertTrue("Couch Cushion Quest must supply concrete clue text containing $clueText", playText.contains(clueText, ignoreCase = true))
        }
    }

    @Test
    fun pirateMapStepsSuppliesReadyToCopyRouteAndTreasureChoices() {
        val item = activeItem("pirate_map_steps")
        val setupText = item.strings("setupSteps").joinToString(" ")
        val playText = item.strings("playSteps").joinToString(" ")

        assertTrue(setupText.contains("X (start) → couch → doorway → table (treasure)"))
        listOf("four tiny steps", "turn right", "three tiny steps", "turn left", "golden banana", "sparkling seashell", "tiny dragon egg").forEach { routeText ->
            assertTrue("Pirate Map Steps must supply route/treasure text containing $routeText", playText.contains(routeText, ignoreCase = true))
        }
        assertFalse("Pirate Map Steps must not ask the parent to invent the treasure", playText.contains("invent", ignoreCase = true))
    }

    @Test
    fun kpf0003WouldYouRatherHasLabeledReadyToReadGrossAndSillySet() {
        val item = activeItem("would_you_rather_silly_family")
        val text = buildString {
            append(item.string("promptText"))
            item.strings("variations").forEach { append(' ').append(it) }
            item.strings("followUps").forEach { append(' ').append(it) }
        }
        assertTrue(text.contains("Gross & Silly"))
        assertTrue(Regex("Would you rather", RegexOption.IGNORE_CASE).findAll(text).count() >= 4)
        assertFalse(text.contains(Regex("crass|sexual|violent", RegexOption.IGNORE_CASE)))
    }

    @Test
    fun kpf0004RaceLikeAnAnimalIsActiveCompleteAndSafelyCategorized() {
        val item = activeItem("race_like_an_animal")
        val text = item.allText()
        assertTrue(item.strings("quickCategories").contains("get_energy_out"))
        assertTrue(item.strings("quickCategories").contains("outdoor_adventures"))
        listOf("kangaroo", "cheetah", "rabbit", "frog", "clear", "walk", "low-impact").forEach {
            assertTrue("Race Like an Animal must mention $it", text.contains(it, ignoreCase = true))
        }
    }

    @Test
    fun kpf0005PillowMarcoPoloUsesOnlyTheSafetyRedesignedVariant() {
        val item = activeItem("indoor_pillow_marco_polo")
        val text = item.allText()
        assertTrue(item.string("title").contains("Eyes-Open"))
        assertTrue(item.string("summary").contains("safe", ignoreCase = true))
        listOf(
            "eyes remain open", "walk only", "stationary", "adult supervises", "stairs", "furniture edges",
            "cords", "pets", "fragile", "no throwing", "face covering", "piling", "jumping", "stop if",
            "chaotic",
        ).forEach { assertTrue("Safe Marco Polo must mention $it", text.contains(it, ignoreCase = true)) }
        assertTrue(item.strings("quickCategories").contains("get_energy_out"))
        assertTrue(item.strings("quickCategories").contains("quality_time"))
    }

    @Test
    fun kpf0008EveryQualityTimeItemHasValidParticipantSuitabilityIncludingDrafts() {
        val allowed = setOf("one_on_one", "group", "both")
        items.filter { it.strings("quickCategories").contains("quality_time") }.forEach { item ->
            assertTrue("${item.string("id")} needs participantSuitability", item.has("participantSuitability"))
            assertTrue("${item.string("id")} has invalid participantSuitability", item.string("participantSuitability") in allowed)
        }
    }

    @Test
    fun kpf0009DrawingColoringAndPaintingAreActiveSafeActivities() {
        assertNotNull(activeItem("timed_drawing_tiny_monster"))
        listOf("washable_coloring_together", "washable_painting_shapes").forEach { id ->
            val item = activeItem(id)
            assertEquals("activity", item.string("type"))
            val text = item.allText()
            listOf("washable", "non-toxic", "surface", "supervis").forEach {
                assertTrue("$id must mention $it", text.contains(it, ignoreCase = true))
            }
        }
    }

    @Test
    fun kpf0010EveryActiveItemIsImmediatelyReadyToUseWithoutInventingContent() {
        activeItems.forEach { item ->
            assertTrue("${item.string("id")} must explicitly list materials", item.has("materials"))
            when (item.string("type")) {
                "activity" -> {
                    assertTrue("${item.string("id")} needs setup", item.getAsJsonArray("setupSteps").size() > 0)
                    assertTrue("${item.string("id")} needs complete rules", item.getAsJsonArray("playSteps").size() > 0)
                }
                "prompt" -> assertTrue("${item.string("id")} needs a ready-to-read prompt", item.string("promptText").isNotBlank())
                "mad_libs" -> {
                    assertTrue("${item.string("id")} needs fields", item.getAsJsonObject("madLibs").getAsJsonArray("fields").size() > 0)
                    assertTrue("${item.string("id")} needs a template", item.getAsJsonObject("madLibs").get("template").asString.isNotBlank())
                }
            }
        }
    }

    @Test
    fun kpf0010FlaggedActivitiesSupplyAllContentInsteadOfAskingFamiliesToInventIt() {
        assertSupplies(
            id = "paper_airplane_weather",
            "fold the paper in half lengthwise",
            "top corners",
            "center crease",
            "fold each wing down",
            "open both wings",
        )
        assertSupplies(
            id = "copycat_clap_code",
            "pattern 1: clap-clap-pause-clap",
            "pattern 2: clap-pause-clap-clap",
            "pattern 3: clap-clap-clap-pause-clap",
        )
        assertSupplies(
            id = "chair_train_station",
            "stop 1: teddy bear town",
            "stop 2: dinosaur park",
            "stop 3: moon station",
        )
        assertSupplies(
            id = "stuffed_animal_rescue",
            "rescue 1: bear",
            "rescue 2: bunny",
            "rescue 3: puppy",
            "beside the couch",
            "under the table",
            "beside a pillow",
        )
        assertSupplies(
            id = "blanket_fort_post_office",
            "mail 1: you make me smile",
            "mail 2: please do three silly wiggles",
            "mail 3: a big hug is waiting at the fort",
        )
        assertSupplies(
            id = "family_recipe_pretend",
            "rainbow dragon soup",
            "two cloud puffs",
            "three rainbow noodles",
            "one spoonful of moon peas",
            "stir three times",
        )

        val forbiddenRequests = Regex("\\b(invent|make up|create your own|pick a pretend dish|adds? one imaginary ingredient)\\b", RegexOption.IGNORE_CASE)
        listOf(
            "paper_airplane_weather",
            "copycat_clap_code",
            "chair_train_station",
            "stuffed_animal_rescue",
            "blanket_fort_post_office",
            "family_recipe_pretend",
        ).forEach { id ->
            assertFalse("$id must not delegate required content creation to the family", activeItem(id).allText().contains(forbiddenRequests))
        }
    }

    @Test
    fun everyMadLibReviewEntryIncludesSeedQuickCategoriesParticipantSuitabilityAndReadAloudNote() {
        val review = String(Files.readAllBytes(repositoryRoot().resolve("docs/content/review/mad-libs.txt")))
        val madLibItems = activeItems.filter { it.string("type") == "mad_libs" }

        assertEquals(madLibItems.size, Regex("(?m)^- Type: mad_libs$").findAll(review).count())
        madLibItems.forEach { item ->
            val section = reviewSection(review, item.string("id"))
            assertTrue(
                "${item.string("id")} review entry needs exact quick-category metadata",
                section.contains("- Quick categories: ${item.strings("quickCategories").joinToString()}")
            )
            assertTrue(
                "${item.string("id")} review entry needs participant-suitability metadata",
                section.contains("- Participant suitability: ${item.string("participantSuitability")}")
            )
            assertTrue(
                "${item.string("id")} review entry needs its read-aloud note",
                section.contains("- Read-aloud note: ${item.getAsJsonObject("madLibs").get("readAloudNote").asString}")
            )
        }
    }

    @Test
    fun draftContentRemainsExcludedFromActiveInvariantSet() {
        assertTrue(items.any { it.string("status") == "draft" })
        assertFalse(activeItems.any { it.string("status") == "draft" })
    }

    private fun activeItem(id: String): JsonObject =
        activeItems.singleOrNull { it.string("id") == id }
            ?: throw AssertionError("Missing active content item $id")

    private fun JsonObject.string(name: String): String = get(name)?.asString.orEmpty()

    private fun JsonObject.strings(name: String): List<String> =
        getAsJsonArray(name)?.map { it.asString }.orEmpty()

    private fun JsonObject.allText(): String = entrySet().joinToString(" ") { it.value.toString() }

    private fun assertSupplies(id: String, vararg requiredText: String) {
        val text = activeItem(id).allText()
        requiredText.forEach { expected ->
            assertTrue("$id must supply ready-to-use content containing: $expected", text.contains(expected, ignoreCase = true))
        }
    }

    private fun reviewSection(review: String, id: String): String {
        val idMarker = "- ID: `$id`"
        val idIndex = review.indexOf(idMarker)
        assertTrue("Mad Lib review is missing $id", idIndex >= 0)
        val sectionStart = review.lastIndexOf("\n## ", idIndex).coerceAtLeast(0)
        val nextSection = review.indexOf("\n## ", idIndex)
        return review.substring(sectionStart, if (nextSection < 0) review.length else nextSection)
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
