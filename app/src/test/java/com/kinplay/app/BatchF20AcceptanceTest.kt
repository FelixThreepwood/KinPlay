package com.kinplay.app

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kinplay.app.session.sessionDefaultDuration
import com.kinplay.app.settings.ActivityDuration
import com.kinplay.app.wyr.formatWouldYouRatherPrompt
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class BatchF20AcceptanceTest {
    private val root = repositoryRoot()
    private val mainSource = readText(root.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))
    private val wyrSource = readText(root.resolve("app/src/main/java/com/kinplay/app/wyr/WouldYouRatherScreen.kt"))
    private val contentRoot: JsonObject = JsonParser.parseString(
        readText(root.resolve("content/seed/kinplay_seed_v1.json")),
    ).asJsonObject

    @Test
    fun releaseTargetAndVersionedDatedChangelogAreReady() {
        val build = readText(root.resolve("app/build.gradle.kts"))
        assertTrue(build.contains("val appVersionName = \"0.7.3\""))
        assertTrue(build.contains("versionCode = 16"))
        assertEquals("0.7.3", KIDPLAY_RELEASE_CHANGELOG.first().version)
        assertEquals("2026-08-23", KIDPLAY_RELEASE_CHANGELOG.first().releaseDate)
        assertTrue(KIDPLAY_RELEASE_CHANGELOG.all { it.releaseDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) })
        KIDPLAY_RELEASE_CHANGELOG.flatMap { it.changes }.forEach { change ->
            val wordCount = change.summary.trim().split(Regex("\\s+")).size
            assertTrue("${change.itemId} summary has $wordCount words", wordCount in 5..10)
        }
    }

    @Test
    fun productionSurfacesNoLongerMountThreeSecondHandoffLock() {
        assertFalse(mainSource.contains("ChildHandoffLockContainer"))
        assertFalse(wyrSource.contains("ChildHandoffLockContainer"))
        assertFalse(mainSource.contains("3-second child handoff lock"))
        assertFalse(mainSource.contains("if (shouldShowChildHandoffLock"))
    }

    @Test
    fun feedbackOverlayIsMountedForTheWholeNavigationHost() {
        assertTrue(mainSource.contains("FeedbackOverlay("))
        assertTrue(mainSource.contains("Routes.WouldYouRather"))
        assertTrue(mainSource.contains("Routes.TimedSession"))
        assertTrue(mainSource.contains("feedbackRoute"))
    }

    @Test
    fun levelOneHeadingAndCardContractsAreImplemented() {
        assertTrue(mainSource.contains("all-games-heading"))
        assertTrue(mainSource.contains("fontFeatureSettings = \"smcp\""))
        assertTrue(mainSource.contains("levelOne = true"))
        assertTrue(mainSource.contains("content-card-"))
        assertTrue(mainSource.contains("onClickLabel = item.title"))
        assertFalse(mainSource.contains("Recognizable games first; Mad Libs stays together as one collection."))
        assertFalse(mainSource.contains("formatGroup.description"))
    }

    @Test
    fun wouldYouRatherFormatterKeepsScenarioBoundaryOnTwoLines() {
        assertEquals(
            "Would you rather fly OR\nbe invisible?",
            formatWouldYouRatherPrompt("Would you rather fly or be invisible?"),
        )
        assertEquals(
            "Would you rather fly OR\nbe invisible?",
            formatWouldYouRatherPrompt("Would you rather fly OR\nbe invisible?"),
        )
    }

    @Test
    fun detailTemplateUsesConciseRoleAndInstructionSections() {
        val item = KinPlayItem.fromJson(JSONObject(item("quiet_color_hunt").toString()))
        val titles = item.detailSections().map { it.title }
        assertTrue(titles.contains("Players"))
        assertTrue(titles.contains("Steps"))
        assertTrue(titles.contains("Clues and suggestions"))
        assertTrue(titles.contains("Variations"))
        assertFalse(mainSource.contains("Text(\"${'$'}{index + 1}\", fontWeight = FontWeight.Bold"))
    }

    @Test
    fun authorizedContentChangesRetireShapeDetectiveAndReviewSafariAndSafetyCopy() {
        val shape = item("shape_detective")
        assertEquals("retired", shape.string("status"))

        val spy = item("quiet_color_hunt")
        val spyText = listOf(
            spy.string("title"),
            spy.string("summary"),
            spy.stringList("setupSteps").joinToString(" "),
            spy.stringList("playSteps").joinToString(" "),
            spy.string("parentNotes"),
            spy.stringList("variations").joinToString(" "),
        ).joinToString(" ")
        assertFalse(spyText.contains("safe", ignoreCase = true))

        val safari = item("backyard_micro_safari")
        assertEquals(3, safari.get("durationMinutes").asInt)
        val safariText = safari.toString()
        assertTrue(safariText.contains("three-minute", ignoreCase = true))
        assertTrue(safariText.contains("explain", ignoreCase = true))
        assertTrue(safariText.contains("approved", ignoreCase = true))
        assertTrue(safariText.contains("undisturbed", ignoreCase = true) || safariText.contains("without touching", ignoreCase = true))
        assertEquals(ActivityDuration.THREE_MINUTES, sessionDefaultDuration(KinPlayItem.fromJson(JSONObject(safari.toString()))))

        val rainbow = item("rainbow_sort_sprint")
        assertFalse(rainbow.string("summary").contains("safe", ignoreCase = true))
        assertFalse(rainbow.stringList("materials").joinToString().contains("safe", ignoreCase = true))
        assertTrue(rainbow.string("parentNotes").contains("Avoid small items", ignoreCase = true))
    }

    @Test
    fun retiredShapeDetectiveIsExcludedFromEveryRuntimeSelectionPath() {
        val shape = KinPlayItem.fromJson(JSONObject(item("shape_detective").toString()))
        val pack = ContentPack(items = listOf(shape))

        assertTrue(pack.activeItems().isEmpty())
        assertTrue(pack.discoveryItems().isEmpty())
        assertTrue(pack.searchItems("shape").isEmpty())
        assertTrue(pack.favoriteItems(setOf("shape_detective")).isEmpty())
        assertTrue(pack.recentItems(listOf("shape_detective")).isEmpty())
        assertEquals(null, pack.activeItemById("shape_detective"))
        assertEquals(null, pack.items.pickForMode("brain_games", seed = 7L))
    }

    @Test
    fun canonicalAndPackagedSeedsRemainByteIdentical() {
        assertTrue(
            Files.readAllBytes(root.resolve("content/seed/kinplay_seed_v1.json"))
                .contentEquals(Files.readAllBytes(root.resolve("app/src/main/assets/kinplay_seed_v1.json"))),
        )
    }

    private fun item(id: String): JsonObject = contentRoot.getAsJsonArray("items")
        .map { it.asJsonObject }
        .single { it.string("id") == id }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not locate repository root")
        }
        return current
    }

    private fun readText(path: Path): String = path.toFile().readText()

    private fun JsonObject.string(name: String): String = get(name).asString

    private fun JsonObject.stringList(name: String): List<String> = getAsJsonArray(name).map { it.asString }
}
