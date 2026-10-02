package com.kinplay.app

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class ProductionCopyRemovalTest {
    private val root = repositoryRoot()
    private val retiredActivityIds = setOf(
        "rainbow_sort_sprint",
        "chair_train_station",
        "quiet_library_helper",
        "washable_painting_shapes",
        "couch_cushion_quest",
        "five_senses_tour",
        "superpower_for_helping",
        "thank_you_detective",
        "tiny_kindness_plan",
        "family_news_desk",
        "silly_invention_pitch",
    )
    private val prohibitedCopyPatterns = listOf(
        Regex("\\b(?:safe|safety|safely|unsafe)\\b", RegexOption.IGNORE_CASE),
        Regex(
            "\\bprivacy\\b|\\bprivate (?:family )?(?:details|information|data)\\b|" +
                "\\bpersonal (?:information|data)\\b|\\bchild[- ]identifying information\\b|" +
                "\\boffline(?:[- ]first)?\\b|\\blocal[- ](?:seed|storage|content|only)\\b|" +
                "\\b(?:save|saved|store|stores|stored) on this device\\b|" +
                "\\blocal (?:save|storage|content|data)\\b|\\bon[- ]device\\b|" +
                "\\bno network access\\b|" +
                "\\bwithout network access\\b|\\bno data collection\\b",
            RegexOption.IGNORE_CASE,
        ),
        Regex("\\blook without touching\\b", RegexOption.IGNORE_CASE),
        Regex(
            "\\baccounts?\\b|\\bsign[ -]?in\\b|\\blog[ -]?in\\b|\\bprofiles?\\b",
            RegexOption.IGNORE_CASE,
        ),
        Regex(
            "\\bsupervis(?:e|ed|ing|ion|ory)\\b|\\b(?:adult|parent) stays? nearby\\b|" +
            "\\badult[- ]approved\\b|\\bparent[- ]approved\\b|" +
                "\\b(?:adult|parent)[- ]led\\b|\\b(?:adult|parent) (?:chooses|selects|reviews|approves?)\\b|" +
                "\\b(?:adult|parent) review (?:is )?required\\b|\\b(?:crawl|walk) only if\\b|" +
                "\\bgentl(?:e|y|er|est)\\b|" +
                "\\bhazard\\b|\\bwarning\\b|\\brisk\\b|\\ballerg(?:y|ies)\\b|\\bnon[- ]?toxic\\b|" +
                "\\bface covering\\b|\\bchok(?:e|ing)\\b|\\bsuffocation\\b|\\bcollision\\b|" +
                "\\b(?:swallow|ingest|edible|inedible|non-edible)\\b|" +
                "\\b(?:away from|out of|in|into|near) (?:the )?mouths?\\b|" +
                "\\bsmall (?:objects?|items?|pieces?)\\b|" +
                "\\blarge objects?\\b.{0,60}\\b(?:toddlers|young children)\\b|" +
                "\\b(?:toddlers|young children)\\b.{0,60}\\blarge objects?\\b|" +
                "\\b(?:skip|avoid|only|unless).{0,60}\\b(?:taste|smell)\\b|" +
                "\\b(?:taste|smell).{0,60}\\b(?:parent|adult|safe|supervis)\\b|" +
                "\\bwithout (?:sprinting|running|leaping|jumping)\\b|" +
                "\\bkeep (?:the )?(?:walking )?(?:paths?|lanes?|routes?) clear\\b|" +
                "\\bclear (?:the|a|small|safe) (?:space|area|zone|path|floor)\\b|" +
                "\\bno (?:throwing|piling|jumping|running|blindfolds|closing eyes)\\b|" +
                "\\beyes (?:remain|stay) open\\b|\\bwalk only\\b|\\bspace is uncertain\\b|" +
                "\\b(?:balance feels uncertain|space is uncertain|feels unsure)\\b|" +
                "\\bstay within the boundary\\b|\\bstop if (?:anyone )?(?:feels|is) unsure\\b|" +
                "\\bwithin easy reach\\b|\\bstay within the boundary\\b|" +
                "\\baway from (?:roads|stairs|faces|pets|fragile items|obstacles)\\b|" +
                "\\b(?:small|tiny|gentle|quick) (?:sliding )?(?:steps|hops)\\b|" +
                "\\bclear area\\b|\\bwide walking space\\b|\\bslow skating\\b|" +
                "\\bnot a contact race\\b|\\bspace becomes crowded\\b|" +
                "\\bno hopping or running\\b|\\bstay where you are\\b",
            RegexOption.IGNORE_CASE,
        ),
    )

    @Test
    fun removedDestinationsAreAbsentFromProductionNavigationAndMenu() {
        val main = readText("app/src/main/java/com/kinplay/app/MainActivity.kt")
        listOf(
            "Routes.Account",
            "Routes.SafetyPrivacy",
            "const val Account =",
            "const val SafetyPrivacy =",
            "AccountScreen(",
            "SafetyPrivacyScreen(",
            "Text(\"Account\")",
            "Safety and privacy",
        ).forEach { obsolete -> assertFalse("Obsolete destination remains: $obsolete", main.contains(obsolete)) }
        listOf("Settings", "About the app", "Routes.Settings", "Routes.AboutApp").forEach { current ->
            assertTrue("Expected destination disappeared: $current", main.contains(current))
        }
    }

    @Test
    fun canonicalAndPackagedActivityCopyMatchAndContainNoProhibitedFraming() {
        val canonicalDirectory = root.resolve("content/seed")
        val packagedDirectory = root.resolve("app/src/main/assets")
        val canonicalNames = Files.newDirectoryStream(canonicalDirectory, "*.json").use { files ->
            files.map { it.fileName.toString() }.sorted()
        }
        val packagedNames = Files.newDirectoryStream(packagedDirectory, "*.json").use { files ->
            files.map { it.fileName.toString() }.sorted()
        }
        assertEquals("Canonical and packaged seed inventories must match", canonicalNames, packagedNames)

        val violations = canonicalNames.flatMap { name ->
            val canonicalBytes = Files.readAllBytes(canonicalDirectory.resolve(name))
            val packagedBytes = Files.readAllBytes(packagedDirectory.resolve(name))
            assertTrue("Canonical and packaged $name must remain byte-identical", canonicalBytes.contentEquals(packagedBytes))
            val pack = JSONObject(String(canonicalBytes))
            allJsonStrings(pack, name).flatMap { (path, text) ->
                prohibitedCopyPatterns.filter { it.containsMatchIn(text) }.map { "$name:$path [${it.pattern}]: $text" }
            }
        }
        assertTrue("Prohibited activity copy remains:\n${violations.joinToString("\n")}", violations.isEmpty())
    }

    @Test
    fun requestedActivitiesAreRetiredFromActiveContentDiscoveryAndReviewExports() {
        val seed = JSONObject(readText("content/seed/kinplay_seed_v1.json"))
        val items = seed.getJSONArray("items")
        val matchingItems = (0 until items.length()).map(items::getJSONObject)
            .filter { it.getString("id") in retiredActivityIds }
        assertEquals(
            "Every requested activity must remain represented for history",
            retiredActivityIds,
            matchingItems.map { it.getString("id") }.toSet(),
        )
        assertTrue(
            "Requested activities must no longer be active",
            matchingItems.all { it.getString("status") == "retired" },
        )

        val titles = matchingItems.map { it.getString("title") }
        listOf(
            "docs/content/review/activities.txt",
            "docs/content/review/prompts.txt",
            "docs/content/review/mad-libs.txt",
            "docs/content/review/content-index.txt",
        ).forEach { path ->
            val content = readText(path)
            titles.forEach { title ->
                assertFalse("Retired activity '$title' remains in active export $path", content.contains(title))
            }
            retiredActivityIds.forEach { id ->
                assertFalse("Retired activity '$id' remains in active export $path", content.contains(id))
            }
        }

        val productionSources = Files.walk(root.resolve("app/src/main/java")).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.fileName.toString().endsWith(".kt") }.toList()
        }
        productionSources.forEach { path ->
            val source = String(Files.readAllBytes(path))
            retiredActivityIds.forEach { id ->
                assertFalse(
                    "Retired route/discovery reference '$id' remains in ${root.relativize(path)}",
                    source.contains(id),
                )
            }
        }
    }

    @Test
    fun activeReviewExportDoesNotKeepSupersededActivityCopy() {
        val activities = readText("docs/content/review/activities.txt")
        assertTrue(activities.contains("Notice a plant or bug, then make up a story about it."))
        assertFalse(activities.contains("Look without touching"))

        val copyExports = listOf(
            "docs/content/review/activities.txt",
            "docs/content/review/mad-libs.txt",
            "docs/content/review/prompts.txt",
        )
        val copyLine = Regex(
            "(?m)^- (?:Summary|Collapsed description|Setup|Play steps|Parent note|Variations|Prompt|Follow-ups|Template|Read-aloud note):\\s*(.*)$",
        )
        val violations = copyExports.flatMap { path ->
            copyLine.findAll(readText(path)).flatMap { match ->
                val value = match.groupValues[1]
                prohibitedCopyPatterns.filter { it.containsMatchIn(value) }.map { "$path: $value" }
            }.toList()
        }
        assertTrue("Active review exports retain superseded customer copy:\\n${violations.joinToString("\\n")}", violations.isEmpty())
    }

    @Test
    fun contentQaDoesNotRequireRemovedWarningsOrSupervisionCopy() {
        val checklist = readText("docs/launch/content-qa-checklist.md")
        listOf(
            "Parent notes explain supervision",
            "Parent supervision is stated",
            "Safety warnings are retained or relocated",
            "A safety warning is not removed",
            "Materials, furniture, cords, stairs, pets, and fragile objects are addressed where relevant",
        ).forEach { obsolete ->
            assertFalse("Current content QA still requires '$obsolete'", checklist.contains(obsolete, ignoreCase = true))
        }
        assertTrue("Retired content must remain excluded from app flows", checklist.contains("Draft and retired content remain unreachable"))
    }

    @Test
    fun activeReviewExportsLabelEmptyMaterialListsExplicitly() {
        listOf(
            "docs/content/review/activities.txt",
            "docs/content/review/mad-libs.txt",
            "docs/content/review/prompts.txt",
        ).forEach { path ->
            val blankMaterials = Regex("(?m)^- Materials:\\s*$").containsMatchIn(readText(path))
            assertFalse("Review export has an ambiguous empty materials label: $path", blankMaterials)
        }
    }

    @Test
    fun productionUiAndCurrentReleaseCopyContainNoProhibitedFraming() {
        val sourceRoot = root.resolve("app/src/main/java")
        val productionSources = Files.walk(sourceRoot).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.fileName.toString().endsWith(".kt") }
                .filter { it.fileName.toString() != "ReleaseChangelog.kt" }
                .toList()
        }
        val sourceViolations = productionSources.flatMap { path ->
            val relativePath = root.relativize(path).toString()
            KOTLIN_STRING_LITERAL.findAll(stripKotlinComments(readText(relativePath))).flatMap { match ->
                val literal = match.groupValues[1]
                prohibitedCopyPatterns.filter { it.containsMatchIn(literal) }.map { "$relativePath: $literal" }
            }.toList()
        }
        val resourcesRoot = root.resolve("app/src/main/res")
        val resourceFiles = Files.walk(resourcesRoot).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.fileName.toString().endsWith(".xml") }.toList()
        }
        val resourceViolations = resourceFiles.flatMap { path ->
            val relativePath = root.relativize(path).toString()
            val source = readText(relativePath)
            val values = XML_COPY_BLOCK.findAll(source).map { it.groupValues[1].replace(XML_TAG, " ") } +
                XML_COPY_ATTRIBUTE.findAll(source).map { it.groupValues[1] }
            values.flatMap { text ->
                prohibitedCopyPatterns.filter { it.containsMatchIn(text) }.map { "$relativePath: $text" }
            }.toList()
        }
        val violations = sourceViolations + resourceViolations + KIDPLAY_RELEASE_CHANGELOG.first().changes.flatMap { change ->
            prohibitedCopyPatterns.filter { it.containsMatchIn(change.summary) }
                .map { "current release ${change.itemId}: ${change.summary}" }
        }
        assertTrue("Prohibited production UI or current-release copy remains:\n${violations.joinToString("\n")}", violations.isEmpty())

        assertTrue(
            "Only current release notes may appear on the About screen",
            readText("app/src/main/java/com/kinplay/app/MainActivity.kt").contains("KIDPLAY_RELEASE_CHANGELOG.take(1).forEach"),
        )
        assertEquals(
            "Historical release text must remain unchanged in the source record",
            "Reduced repetitive safety copy while retaining essential warnings",
            KIDPLAY_RELEASE_CHANGELOG.single { it.version == "0.7.3" }.changes.first().summary,
        )
    }

    @Test
    fun internalClassificationMetadataRemainsButHasNoVisibleLabelMapper() {
        val schema = JSONObject(readText("content/kinplay-content.schema.json"))
        val tags = schema.getJSONObject("\$defs")
            .getJSONObject("contentItem")
            .getJSONObject("properties")
            .getJSONObject("safetyTags")
            .getJSONObject("items")
            .getJSONArray("enum")
        val identifiers = (0 until tags.length()).map(tags::getString)
        assertTrue("Internal eligibility metadata must remain available", "parent_supervision" in identifiers)

        val main = readText("app/src/main/java/com/kinplay/app/MainActivity.kt")
        assertTrue("Internal classification logic must remain available", main.contains("safetyTags"))
        assertFalse("Internal identifiers must not be translated into user-facing labels", main.contains("displayTagLabel"))
        assertFalse("Internal identifiers must not be summarized on a user-facing surface", main.contains("reviewedSafetyTagSummary"))
    }

    @Test
    fun embeddedCopyArtworkIsAbsentFromPackagedContentAndResourceLookups() {
        val image = root.resolve("app/src/main/res/drawable-nodpi/brain_movement_activities.jpg")
        assertFalse("Artwork with embedded customer copy must not ship", Files.exists(image))

        val seed = JSONObject(readText("content/seed/kinplay_seed_v1.json")).getJSONArray("items")
        val hasReference = (0 until seed.length()).any { index ->
            val assets = seed.getJSONObject(index).optJSONArray("visualAssets") ?: return@any false
            (0 until assets.length()).any { assetIndex ->
                assets.getJSONObject(assetIndex).optString("resource") == "brain_movement_activities"
            }
        }
        assertFalse("No activity may reference artwork containing removed copy", hasReference)

        listOf(
            "app/src/main/java/com/kinplay/app/MainActivity.kt",
            "app/src/main/java/com/kinplay/app/PaperAirplanes.kt",
            "app/src/main/java/com/kinplay/app/TinyMonsterVisualGuide.kt",
        ).forEach { path ->
            val source = readText(path)
            assertFalse("Stale brain-movement image lookup in $path", source.contains("R.drawable.brain_movement_activities"))
        }
    }

    @Test
    fun currentSourceVersionMatchesTheVisibleCurrentChangelogEntry() {
        val build = readText("app/build.gradle.kts")
        val sourceVersion = Regex("""val appVersionName = "([^\"]+)"""").find(build)?.groupValues?.get(1)
        val sourceVersionCode = Regex("""versionCode = (\d+)""").find(build)?.groupValues?.get(1)?.toInt()
        assertEquals("The current source build and customer-visible changelog must agree", sourceVersion, KIDPLAY_RELEASE_CHANGELOG.first().version)
        assertEquals("This feedback batch needs a new monotonic app version", "0.7.5", sourceVersion)
        assertEquals("This feedback batch needs a new monotonic version code", 18, sourceVersionCode)
        assertTrue(
            "The current changelog must include the baseline copy cleanup",
            KIDPLAY_RELEASE_CHANGELOG.first().changes.any { it.itemId == "KP-PRO-062B" },
        )
        assertTrue(
            "The current changelog must include the activity retirement batch",
            KIDPLAY_RELEASE_CHANGELOG.first().changes.any { it.itemId == "KP-PRO-063B" },
        )
        val releaseSummary = KIDPLAY_RELEASE_CHANGELOG.first().changes
            .single { it.itemId == "KP-PRO-062B" }.summary
        assertTrue("The release summary must contain 5–10 words", releaseSummary.trim().split(Regex("\\s+")).size in 5..10)
        assertTrue("Build must use the shared version name", build.contains("versionName = appVersionName"))
    }

    @Test
    fun currentProductSpecsDoNotPrescribeRemovedCustomerCopy() {
        val design = readText("DESIGN.md")
        listOf(
            "safety content",
            "safety, and parent notes",
            "condition or safety note",
            "setup burden, safety",
            "safe portrait path",
            "safe Back behavior",
            "safety warnings",
        ).forEach { obsolete -> assertFalse("Current design still prescribes '$obsolete'", design.contains(obsolete, ignoreCase = true)) }

        val designSystem = readText("docs/design/kidplay-design-system.md")
        listOf(
            "supervised movement with space and safety information",
            "safety text",
            "safe ending",
            "safety cue",
            "safety actions",
            "safe Back behavior",
            "safety wording",
        ).forEach { obsolete -> assertFalse("Current design system still prescribes '$obsolete'", designSystem.contains(obsolete, ignoreCase = true)) }

        val assetQa = readText("docs/design/kidplay-asset-qa.md")
        assertFalse("Asset guidance still requires customer-facing safety instructions", assetQa.contains("safety instruction", ignoreCase = true))

        val mvp = readText("docs/product/mvp-spec.md")
        listOf("Safety tags", "About / Safety", "Parent-led disclaimer", "No data collection statement").forEach { obsolete ->
            assertFalse("MVP spec still prescribes '$obsolete'", mvp.contains(obsolete, ignoreCase = true))
        }
        assertTrue("Internal classification data must remain documented", mvp.contains("Internal classification tags"))

        val currentSpecs = listOf(
            "DESIGN.md",
            "docs/design/kidplay-design-system.md",
            "docs/design/kidplay-asset-qa.md",
            "docs/product/mvp-spec.md",
        )
        val restrictedLanguage = Regex(
            "\\b(?:safe|safety|safely|unsafe|privacy|accounts?|sign[ -]?in|log[ -]?in|profiles?|" +
                "gentl(?:e|y|er|est)|supervis(?:e|ed|ing|ion)|swallow(?:ing)?|chok(?:e|ing)|" +
                "ingest(?:ion)?|edible|inedible|taste|smell|mouth)\\b",
            RegexOption.IGNORE_CASE,
        )
        val violations = currentSpecs.flatMap { path ->
            readText(path).lineSequence().mapIndexedNotNull { index, line ->
                restrictedLanguage.find(line)?.let { "$path:${index + 1}: ${it.value}" }
            }.toList()
        }
        assertTrue("Restricted language remains in current product specs:\\n${violations.joinToString("\\n")}", violations.isEmpty())
    }

    private fun allJsonStrings(value: Any?, path: String): List<Pair<String, String>> = buildList {
        collectJsonStrings(value, path)
    }

    private fun MutableList<Pair<String, String>>.collectJsonStrings(value: Any?, path: String) {
        when (value) {
            is JSONObject -> {
                val keys = value.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    if (key !in NON_COPY_JSON_KEYS) collectJsonStrings(value.get(key), "$path.$key")
                }
            }
            is JSONArray -> for (index in 0 until value.length()) collectJsonStrings(value.get(index), "$path[$index]")
            is String -> add(path to value)
        }
    }

    private fun readText(relativePath: String): String = String(Files.readAllBytes(root.resolve(relativePath)))

    private fun stripKotlinComments(source: String): String = KOTLIN_COMMENT.replace(source, "")

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }

    companion object {
        private val KOTLIN_STRING_LITERAL = Regex("\\\"((?:\\\\.|[^\\\"\\\\])*)\\\"")
        private val XML_COPY_BLOCK = Regex("(?is)<(?:string|item|plurals|string-array|array)\\b[^>]*>(.*?)</(?:string|item|plurals|string-array|array)>")
        private val XML_COPY_ATTRIBUTE = Regex("(?i)(?:android:)?(?:text|title|contentDescription|hint|label|summary)=\\\"([^\\\"]+)\\\"")
        private val XML_TAG = Regex("<[^>]+>")
        private val KOTLIN_COMMENT = Regex("(?s)/\\*.*?\\*/|//[^\\r\\n]*")
        private val NON_COPY_JSON_KEYS = setOf(
            "id",
            "type",
            "status",
            "modes",
            "ageTags",
            "minAge",
            "maxAge",
            "durationMinutes",
            "energyLevel",
            "safetyTags",
            "quickCategories",
            "participantSuitability",
            "formatGroupId",
            "childHandoffLockEligible",
            "resource",
        )
    }
}
