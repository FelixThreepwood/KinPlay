package com.kinplay.app

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class ProtectedSafetyWarningsRegressionTest {
    private val root = repositoryRoot()
    private val canonical by lazy { readJson(root.resolve("content/seed/kinplay_seed_v1.json")) }
    private val runtime by lazy { readJson(root.resolve("app/src/main/assets/kinplay_seed_v1.json")) }
    private val matrix by lazy {
        readJson(root.resolve("docs/testing/feedback/KPF_0032_SAFETY_DECISION_MATRIX.json"))
    }

    // Owner directive 1552919740996128859 supersedes the old runtime-copy decisions only
    // for these current surfaces. The historical KPF-0032 matrix remains byte-for-byte intact.
    private val ownerOverriddenContentPaths = setOf(
        "items[animal_mirror_parade].setupSteps[0]",
        "items[animal_mirror_parade].parentNotes",
        "items[couch_cushion_quest].summary",
        "items[couch_cushion_quest].setupSteps[0]",
        "items[freeze_dance_statues].setupSteps[0]",
        "items[freeze_dance_statues].playSteps[0]",
        "items[freeze_dance_statues].parentNotes",
        "items[pillow_boat_adventure].parentNotes",
        "items[calm_cloud_breaths].playSteps[2]",
        "items[quiet_color_hunt].setupSteps[0]",
        "items[family_charades_animals].setupSteps[0]",
        "items[family_charades_animals].playSteps[2]",
        "items[family_charades_animals].variations[2]",
        "items[timed_drawing_tiny_monster].setupSteps[0]",
        "items[sock_skating_rink].summary",
        "items[sock_skating_rink].setupSteps[0]",
        "items[sock_skating_rink].playSteps[0]",
        "items[sock_skating_rink].playSteps[1]",
        "items[sock_skating_rink].playSteps[2]",
        "items[sock_skating_rink].parentNotes",
        "items[stuffed_animal_rescue].summary",
        "items[stuffed_animal_rescue].setupSteps[0]",
        "items[stuffed_animal_rescue].playSteps[0]",
        "items[stuffed_animal_rescue].playSteps[1]",
        "items[kitchen_band_rehearsal].summary",
        "items[teddy_bear_doctor].parentNotes",
        "items[shape_detective].setupSteps[0]",
        "items[pirate_map_steps].playSteps[0]",
        "items[pirate_map_steps].playSteps[1]",
        "items[whisper_zoo].summary",
        "items[whisper_zoo].playSteps[2]",
        "items[rainbow_sort_sprint].summary",
        "items[rainbow_sort_sprint].materials[0]",
        "items[rainbow_sort_sprint].setupSteps[0]",
        "items[rainbow_sort_sprint].parentNotes",
        "items[five_senses_tour].summary",
        "items[five_senses_tour].playSteps[2]",
        "items[five_senses_tour].parentNotes",
        "items[cleanup_countdown_game].setupSteps[0]",
        "items[paper_airplane_weather].setupSteps[1]",
        "items[paper_airplane_weather].playSteps[0]",
        "items[paper_airplane_weather].playSteps[2]",
        "items[paper_airplane_weather].parentNotes",
        "items[paper_airplane_weather].variations[1]",
        "items[backyard_micro_safari].summary",
        "items[backyard_micro_safari].setupSteps[0]",
        "items[backyard_micro_safari].playSteps[2]",
        "items[backyard_micro_safari].parentNotes",
        "items[chair_train_station].setupSteps[0]",
        "items[chair_train_station].playSteps[0]",
        "items[memory_tray_peek].summary",
        "items[memory_tray_peek].materials[1]",
        "items[memory_tray_peek].setupSteps[0]",
        "items[tiny_tower_engineers].summary",
        "items[tiny_tower_engineers].setupSteps[0]",
        "items[family_recipe_pretend].parentNotes",
        "items[pirate_map_steps].setupSteps[0]",
        "items[hallway_balance_beam].summary",
        "items[hallway_balance_beam].setupSteps[0]",
        "items[hallway_balance_beam].playSteps[0]",
        "items[hallway_balance_beam].parentNotes",
        "items[hallway_balance_beam].variations[0]",
        "items[hallway_balance_beam].variations[1]",
        "items[breakfast_dragon_train].madLibs.readAloudNote",
        "items[race_like_an_animal].summary",
        "items[race_like_an_animal].setupSteps[0]",
        "items[race_like_an_animal].setupSteps[1]",
        "items[race_like_an_animal].playSteps[0]",
        "items[race_like_an_animal].playSteps[1]",
        "items[race_like_an_animal].playSteps[2]",
        "items[race_like_an_animal].playSteps[3]",
        "items[race_like_an_animal].parentNotes",
        "items[race_like_an_animal].variations[0]",
        "items[race_like_an_animal].variations[1]",
        "items[indoor_pillow_marco_polo].title",
        "items[indoor_pillow_marco_polo].summary",
        "items[indoor_pillow_marco_polo].setupSteps[0]",
        "items[indoor_pillow_marco_polo].setupSteps[1]",
        "items[indoor_pillow_marco_polo].setupSteps[2]",
        "items[indoor_pillow_marco_polo].playSteps[0]",
        "items[indoor_pillow_marco_polo].playSteps[1]",
        "items[indoor_pillow_marco_polo].playSteps[2]",
        "items[indoor_pillow_marco_polo].playSteps[3]",
        "items[indoor_pillow_marco_polo].parentNotes",
        "items[indoor_pillow_marco_polo].variations[0]",
        "items[indoor_pillow_marco_polo].variations[1]",
        "items[washable_painting_shapes].summary",
        "items[washable_painting_shapes].materials[1]",
        "items[washable_painting_shapes].setupSteps[0]",
        "items[washable_painting_shapes].parentNotes",
        "items[bilateral_mirror_moves].summary",
        "items[bilateral_mirror_moves].playSteps[0]",
        "items[bilateral_mirror_moves].playSteps[2]",
        "items[cross_body_move_mix].setupSteps[0]",
        "items[cross_body_move_mix].parentNotes",
        "items[cross_body_move_mix].variations[1]",
        "items[memory_tray_peek].parentNotes",
        "items[tiny_tower_engineers].parentNotes",
    )
    private val ownerRemovedHistoricalContentPaths = setOf(
        "items[indoor_pillow_marco_polo].playSteps[3]",
        "items[pirate_map_steps].parentNotes",
    )
    private val humanizerApprovedContentRewrites = mapOf(
        "items[freeze_dance_statues].playSteps[2]" to
            "When the parent calls \"Freeze!\" everyone turns into a statue.",
        "items[three_word_story].followUps[1]" to "Can you make it funnier or sillier?",
    )
    private val ownerOverriddenKotlinIds = setOf(
        "main-home-about-label",
        "main-home-about-subtitle",
        "main-quick-play-safe-summary",
        "main-details-safety-tags-label",
        "main-about-page-title",
        "main-about-parent-guidance",
        "main-about-permissions",
        "main-about-lock-limits",
        "main-about-content-source",
        "feedback-ui-privacy",
        "feedback-payload-child-data",
        "feedback-ui-local-first-email-handoff",
        "settings-device-local-save",
    )

    @Test
    fun decisionMatrixIsCompleteAndUsesFailSafeClassification() {
        assertEquals(2, matrix.get("schemaVersion").asInt)
        assertEquals("independent_safety_spec_review_passed", matrix.get("reviewStatus").asString)
        assertFalse(matrix.get("uiDeletionAuthorized").asBoolean)

        val entries = matrix.getAsJsonArray("entries").map { it.asJsonObject }
        assertTrue("Safety decision matrix must not be empty", entries.isNotEmpty())
        assertEquals(entries.size, entries.map { it.string("id") }.distinct().size)
        assertEquals(SOURCE_TYPES, entries.map { it.string("sourceType") }.toSet())
        entries.forEach { entry ->
            val decision = entry.string("decision")
            assertTrue("${entry.string("id")} has invalid decision $decision", decision in DECISIONS)
            assertTrue("${entry.string("id")} needs a rationale", entry.string("rationale").isNotBlank())
            assertTrue("${entry.string("id")} needs a sourcePath", entry.string("sourcePath").isNotBlank())
            if (entry.get("protected").asBoolean) {
                assertTrue(
                    "${entry.string("id")} is protected and must default to retain or relocate",
                    decision == "retain" || decision == "relocate",
                )
                assertTrue("${entry.string("id")} needs a protected risk", entry.string("protectedRisk").isNotBlank())
            }
            if (decision == "relocate") {
                val plan = entry.getAsJsonObject("relocationPlan")
                assertTrue("${entry.string("id")} needs relocationPlan", plan != null)
                assertTrue("${entry.string("id")} needs a destination surface", plan.string("destinationSurface").isNotBlank())
                assertTrue("${entry.string("id")} needs a destination target", plan.string("destinationTarget").isNotBlank())
                assertTrue("${entry.string("id")} needs an actionable target instruction", plan.string("targetInstruction").isNotBlank())
                assertTrue("${entry.string("id")} needs retained wording or meaning", plan.string("requiredWordingOrMeaning").isNotBlank())
                assertTrue("${entry.string("id")} needs prominence", plan.string("prominence").isNotBlank())
                assertTrue(
                    "${entry.string("id")} needs a post-relocation regression expectation",
                    plan.string("postRelocationRegressionExpectation").isNotBlank(),
                )
                if (entry.string("sourceType") == "content") {
                    assertTrue(
                        "${entry.string("id")} relocation must identify its item target",
                        plan.string("destinationTarget").contains(entry.string("itemId")),
                    )
                }
            }
        }

        val classifiedContentPaths = entries
            .filter { it.string("sourceType") == "content" }
            .map { it.string("sourcePath") }
            .toSet()
        val currentContentPaths = allUserVisibleContentPaths(canonical)
        val runtimeContentPaths = allUserVisibleContentPaths(runtime)
        assertEquals(
            "Only specifically removed owner-overridden fields may be absent",
            ownerRemovedHistoricalContentPaths,
            classifiedContentPaths - currentContentPaths,
        )
        assertEquals(
            "No new user-visible content path may bypass the historical matrix",
            emptySet<String>(),
            currentContentPaths - classifiedContentPaths,
        )
        assertEquals(
            "Current copy inventory must exactly reconcile with the immutable historical matrix",
            classifiedContentPaths - ownerRemovedHistoricalContentPaths,
            currentContentPaths,
        )
        entries.filter { it.string("sourceType") == "content" }.forEach { entry ->
            val path = entry.string("sourcePath")
            when {
                path in ownerRemovedHistoricalContentPaths -> {
                    assertFalse("Owner-removed historical copy remains in canonical content at $path", path in currentContentPaths)
                    assertFalse("Owner-removed historical copy remains in runtime content at $path", path in runtimeContentPaths)
                }
                path in ownerOverriddenContentPaths -> {
                    val canonicalText = contentText(canonical, entry)
                    assertFalse("Owner-overridden copy remains at $path", canonicalText == entry.string("expectedText"))
                    assertEquals("Canonical/runtime drift at $path", canonicalText, contentText(runtime, entry))
                }
                path in humanizerApprovedContentRewrites -> {
                    val canonicalText = contentText(canonical, entry)
                    assertEquals("Approved Humanizer copy changed at $path", humanizerApprovedContentRewrites.getValue(path), canonicalText)
                    assertEquals("Canonical/runtime drift at $path", canonicalText, contentText(runtime, entry))
                }
                else -> {
                    val canonicalText = contentText(canonical, entry)
                    assertEquals("Stale canonical inventory at $path", entry.string("expectedText"), canonicalText)
                    assertEquals("Canonical/runtime drift at $path", entry.string("expectedText"), contentText(runtime, entry))
                }
            }
        }
        assertTrue(
            "Approved Humanizer follow-up must be in the active prompt export",
            readText(root.resolve("docs/content/review/prompts.txt")).contains(humanizerApprovedContentRewrites.getValue("items[three_word_story].followUps[1]")),
        )
    }

    @Test
    fun knownReviewBlockersAreExplicitlyAndSemanticallyClassified() {
        val entries = matrix.getAsJsonArray("entries").map { it.asJsonObject }.associateBy { it.string("sourcePath") }
        val protectedPaths = setOf(
            "items[pillow_boat_adventure].parentNotes",
            "items[sock_skating_rink].parentNotes",
            "items[backyard_micro_safari].parentNotes",
            "items[chair_train_station].setupSteps[0]",
            "items[chair_train_station].parentNotes",
            "items[memory_tray_peek].parentNotes",
        )
        protectedPaths.forEach { path ->
            val entry = requireNotNull(entries[path]) { "Missing reviewed warning $path" }
            assertTrue("$path must be protected", entry.get("protected").asBoolean)
            assertTrue("$path needs an explicit risk", entry.string("protectedRisk").isNotBlank())
        }

        (1..3).map { "items[chair_train_station].playSteps[$it]" }.forEach { path ->
            val entry = requireNotNull(entries[path]) { "Full visible-content inventory must include $path" }
            assertFalse("Station label at $path is not a safety warning", entry.get("protected").asBoolean)
            assertTrue("Station label rationale must document semantic review", entry.string("rationale").contains("station", ignoreCase = true))
        }

        val mandatoryPreviouslyMissedPaths = setOf(
            "items[couch_cushion_quest].setupSteps[0]",
            "items[sock_skating_rink].playSteps[0]",
            "items[race_like_an_animal].playSteps[1]",
            "items[race_like_an_animal].playSteps[2]",
            "items[hallway_balance_beam].variations[0]",
            "items[rainbow_sort_sprint].materials[0]",
            "items[memory_tray_peek].materials[1]",
            "items[washable_painting_shapes].materials[1]",
            "items[indoor_pillow_marco_polo].title",
        )
        mandatoryPreviouslyMissedPaths.forEach { path ->
            val entry = requireNotNull(entries[path]) { "Missing independently reviewed path $path" }
            assertTrue("$path is safety-bearing and must be protected", entry.get("protected").asBoolean)
        }
    }

    @Test
    fun secondReviewSafetyBearingPathsHaveSpecificCorrectProtectedRisks() {
        val entries = matrix.getAsJsonArray("entries").map { it.asJsonObject }.associateBy { it.string("sourcePath") }
        val requiredRiskCues = linkedMapOf(
            "items[indoor_pillow_marco_polo].setupSteps[1]" to setOf("fall", "collision", "spacing", "space"),
            "items[indoor_pillow_marco_polo].summary" to setOf("face covering", "suffocation", "fall", "collision", "spacing"),
            "items[indoor_pillow_marco_polo].parentNotes" to setOf("face covering", "suffocation", "fall", "collision", "spacing"),
            "items[race_like_an_animal].setupSteps[1]" to setOf("contact", "collision", "fall", "impact"),
            "items[family_recipe_pretend].setupSteps[0]" to setOf("food", "cooking", "burn", "allergy", "choking"),
            "items[hallway_balance_beam].variations[1]" to setOf("fall", "trip", "balance", "collision"),
        )

        val violations = buildList {
            requiredRiskCues.forEach { (path, cues) ->
                val entry = entries[path]
                when {
                    entry == null -> add("$path is missing")
                    entry.get("protected")?.asBoolean != true -> add("$path is not protected")
                    entry.string("protectedRisk").isBlank() -> add("$path has a blank protectedRisk")
                    cues.none { cue -> entry.string("protectedRisk").contains(cue, ignoreCase = true) } ->
                        add("$path has unrelated protectedRisk '${entry.string("protectedRisk")}' (expected one of ${cues.joinToString()})")
                }
            }
        }
        assertTrue("Second-review safety classification gaps:\n${violations.joinToString("\n")}", violations.isEmpty())
    }

    @Test
    fun protectedHistoricalCopyIsPreservedExceptForTheExplicitOwnerOverrideSet() {
        val protectedEntries = matrix.getAsJsonArray("entries")
            .map { it.asJsonObject }
            .filter { it.string("sourceType") == "content" && it.get("protected").asBoolean }
        assertTrue("Protected content inventory must not be empty", protectedEntries.isNotEmpty())
        protectedEntries.forEach { entry ->
            val expected = entry.string("expectedText")
            val path = entry.string("sourcePath")
            if (path in ownerRemovedHistoricalContentPaths) {
                assertFalse("Owner-removed historical copy remains in canonical content at $path", path in allUserVisibleContentPaths(canonical))
                assertFalse("Owner-removed historical copy remains in runtime content at $path", path in allUserVisibleContentPaths(runtime))
            } else if (path in ownerOverriddenContentPaths) {
                assertFalse("Owner-overridden text remains at $path", expected == contentText(canonical, entry))
                assertEquals("Canonical/runtime drift at $path", contentText(canonical, entry), contentText(runtime, entry))
            } else if (path in humanizerApprovedContentRewrites) {
                assertEquals("Approved Humanizer copy changed at $path", humanizerApprovedContentRewrites.getValue(path), contentText(canonical, entry))
                assertEquals("Canonical/runtime drift at $path", contentText(canonical, entry), contentText(runtime, entry))
            } else {
                assertEquals("Canonical warning changed at $path", expected, contentText(canonical, entry))
                assertEquals("Runtime warning changed at $path", expected, contentText(runtime, entry))
            }
        }
    }

    @Test
    fun everyProtectedKotlinPlatformPrivacyAndSafetyNoticeStillExists() {
        val protectedEntries = matrix.getAsJsonArray("entries")
            .map { it.asJsonObject }
            .filter { it.string("sourceType") == "kotlin" && it.get("protected").asBoolean }
        assertTrue("Protected Kotlin notice inventory must not be empty", protectedEntries.isNotEmpty())
        protectedEntries.forEach { entry ->
            val sourcePath = entry.string("sourcePath").replace(SOURCE_LINE_SUFFIX, "")
            val source = readText(root.resolve(sourcePath))
            val snippet = entry.string("expectedText")
            assertTrue("${entry.string("id")} needs expectedText", snippet.isNotBlank())
            if (entry.string("id") in ownerOverriddenKotlinIds) {
                assertFalse("Owner-overridden notice remains for ${entry.string("id")}", source.contains(snippet))
            } else {
                assertTrue("Protected Kotlin notice missing for ${entry.string("id")}", source.contains(snippet))
            }
        }
    }

    @Test
    fun kotlinNoticeScopeIncludesReviewedPrivacyPlatformAndAccessibilityBoundaries() {
        val entries = matrix.getAsJsonArray("entries").map { it.asJsonObject }.associateBy { it.string("id") }
        val required = setOf(
            "feedback-ui-local-first-email-handoff",
            "settings-device-local-save",
            "lock-accessibility-countdown-state",
            "lock-visible-countdown",
            "lock-boundary-compact",
            "lock-active-semantics",
            "lock-active-body",
            "main-about-lock-limits",
        )
        required.forEach { id ->
            val entry = requireNotNull(entries[id]) { "Kotlin notice scope is missing $id" }
            assertEquals("kotlin", entry.string("sourceType"))
            assertTrue("$id must be protected", entry.get("protected").asBoolean)
            assertTrue("$id needs an explicit risk", entry.string("protectedRisk").isNotBlank())
        }
    }

    @Test
    fun feedbackHandoffAndDestructiveStateNoticesHaveStableInventoryIdsAndExactSourceSnippets() {
        val entries = matrix.getAsJsonArray("entries").map { it.asJsonObject }.associateBy { it.string("id") }
        val feedbackUi = readText(root.resolve(FEEDBACK_UI_PATH))
        val required = linkedMapOf(
            "feedback-ui-handoff-platform-limitation" to "The email app cannot report whether Send was tapped. ",
            "feedback-ui-handoff-explicit-confirmation" to "Confirm only if you sent ${'$'}{pendingHandoffNoteIds.size} note(s).",
            "feedback-ui-immediate-send-success-saved-state" to
                "Email app opened. Confirm here after sending; notes remain unsent until then.",
            "feedback-ui-immediate-send-failure-saved-state" to
                "No email app opened. Use Copy batch below; notes are still saved.",
            "feedback-ui-selected-send-success-saved-state" to
                "Email app opened. Confirm here after sending; selected notes remain unsent until then.",
            "feedback-ui-selected-send-failure-saved-state" to
                "No email app opened. Copy the selected notes instead.",
            "feedback-ui-delete-all-unsent-destructive-warning" to
                "Delete every unsent note from this device? Archived notes will remain.",
            "feedback-ui-handoff-confirmed-archive-state" to
                "${'$'}handedOffCount sent note(s) moved to the archive.",
            "feedback-ui-handoff-unconfirmed-unsent-state" to
                "Handoff not confirmed. Notes remain unsent and selected.",
            "feedback-ui-copy-selected-unsent-state" to
                "Selected notes copied as a formatted batch; they remain unsent.",
            "feedback-ui-archive-email-exclusion-state" to
                "Archived notes are excluded from email batches.",
        )

        val violations = buildList {
            required.forEach { (id, exactSnippet) ->
                val entry = entries[id]
                when {
                    entry == null -> add("missing matrix id $id")
                    entry.string("sourceType") != "kotlin" -> add("$id must use sourceType kotlin")
                    !entry.string("sourcePath").matches(Regex("${Regex.escape(FEEDBACK_UI_PATH)}:\\d+")) ->
                        add("$id must point to $FEEDBACK_UI_PATH with a source line")
                    entry.string("expectedText") != exactSnippet -> add("$id expectedText must exactly match '$exactSnippet'")
                    entry.get("protected")?.asBoolean != true -> add("$id must be protected")
                    entry.string("protectedRisk").isBlank() -> add("$id needs a protectedRisk")
                }
                if (!feedbackUi.contains(exactSnippet)) add("FeedbackUi.kt no longer contains exact snippet for $id: '$exactSnippet'")
            }
        }
        assertTrue("Feedback UI safety inventory gaps:\n${violations.joinToString("\n")}", violations.isEmpty())
    }

    @Test
    fun contentSummaryRelocationsPreserveExpandedCardCueAndDetailsPointOfUseWarning() {
        val relocatedSummaries = matrix.getAsJsonArray("entries")
            .map { it.asJsonObject }
            .filter {
                it.string("sourceType") == "content" &&
                    it.string("field") == "summary" &&
                    it.string("decision") == "relocate" &&
                    it.string("sourcePath") !in ownerOverriddenContentPaths
            }
        assertTrue("The matrix must exercise content summary relocation requirements", relocatedSummaries.isNotEmpty())

        val violations = buildList {
            relocatedSummaries.forEach { entry ->
                val id = entry.string("id")
                val itemId = entry.string("itemId")
                val plan = entry.getAsJsonObject("relocationPlan")
                if (plan == null) {
                    add("$id has no relocationPlan")
                    return@forEach
                }

                val cardCue = plan.string("expandedCardCue")
                val detailsWarning = plan.string("detailsPointOfUseWarning")
                val cardAssertion = plan.string("postRelocationExpandedCardAssertion")
                val detailsAssertion = plan.string("postRelocationDetailsPageAssertion")
                val prominence = plan.string("prominence")
                val equalOrGreaterProminence =
                    prominence.contains("equal", ignoreCase = true) ||
                        prominence.contains("greater", ignoreCase = true) ||
                        prominence.contains("at least as prominent", ignoreCase = true)

                if (cardCue.isBlank() || !cardCue.contains(itemId)) {
                    add("$id needs an item-specific expandedCardCue")
                }
                if (detailsWarning.isBlank() || !detailsWarning.contains(itemId)) {
                    add("$id needs an item-specific detailsPointOfUseWarning")
                }
                if (!equalOrGreaterProminence ||
                    !prominence.contains("expanded card", ignoreCase = true) ||
                    !prominence.contains("details", ignoreCase = true)
                ) {
                    add("$id prominence must guarantee equal-or-greater prominence on expanded card and details page")
                }
                if (cardAssertion.isBlank() || !cardAssertion.contains(itemId)) {
                    add("$id needs an item-specific postRelocationExpandedCardAssertion")
                }
                if (detailsAssertion.isBlank() || !detailsAssertion.contains(itemId)) {
                    add("$id needs an item-specific postRelocationDetailsPageAssertion")
                }
            }
        }
        assertTrue("Incomplete two-surface content summary relocation plans:\n${violations.joinToString("\n")}", violations.isEmpty())
    }

    @Test
    fun matrixDeclaresExactComposeInstrumentationCoverageForAllWarningSurfaces() {
        val coverage = matrix.getAsJsonObject("instrumentationCoverage")
        assertTrue("Matrix needs a top-level instrumentationCoverage declaration", coverage != null)
        assertEquals(INSTRUMENTATION_TEST_CLASS, coverage.string("testClass"))
        val surfaces = coverage.getAsJsonArray("surfaces")?.map { it.asString }?.toSet().orEmpty()
        assertEquals(setOf("collapsed_card", "expanded_card", "details_page"), surfaces)
    }

    @Test
    fun internalClassificationTagsRemainInTheSchemaWithoutUserFacingLabels() {
        val tagEntries = matrix.getAsJsonArray("entries")
            .map { it.asJsonObject }
            .filter { it.string("sourceType") == "safety_tag" }
        val schemaTags = readJson(root.resolve("content/kinplay-content.schema.json"))
            .getAsJsonObject("\$defs")
            .getAsJsonObject("contentItem")
            .getAsJsonObject("properties")
            .getAsJsonObject("safetyTags")
            .getAsJsonObject("items")
            .getAsJsonArray("enum")
            .map { it.asString }
            .toSet()
        assertEquals(schemaTags, tagEntries.map { it.string("tag") }.toSet())

        val mainActivity = readText(root.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))
        assertTrue("Tag metadata remains available to internal logic", mainActivity.contains("safetyTags"))
        assertFalse("Tag labels must not be rendered", mainActivity.contains("displayTagLabel"))
        assertFalse("Tag summaries must not be rendered", mainActivity.contains("reviewedSafetyTagSummary"))
        val sibling = tagEntries.single { it.string("tag") == "sibling_friendly" }
        assertFalse(
            "Sibling-friendly rationale must not claim every tagged item has participantSuitability",
            sibling.string("rationale").contains("participant-fit UI", ignoreCase = true),
        )
    }

    @Test
    fun ownerOverrideRemovesSelectedCopyWhileKeepingActivityAndInternalDataContracts() {
        val pack = ContentPack.fromJson(JSONObject(readText(root.resolve("content/seed/kinplay_seed_v1.json"))))
        fun item(id: String) = pack.items.single { it.id == id }

        // Current content still has ordinary, usable summaries and detail bindings.
        assertEquals("retired", item("rainbow_sort_sprint").status)
        assertTrue(item("memory_tray_peek").collapsedCardPreviewLines().single().isNotBlank())
        assertEquals("retired", item("washable_painting_shapes").status)
        assertTrue(item("sock_skating_rink").collapsedCardPreviewLines().single().isNotBlank())

        val currentCopy = pack.items.flatMap { contentItem ->
            buildList {
                add(contentItem.summary)
                addAll(contentItem.materials)
                addAll(contentItem.setupSteps)
                addAll(contentItem.playSteps)
                add(contentItem.parentNotes)
                addAll(contentItem.variations)
                contentItem.visualAssets.forEach { add(it.altText) }
            }
        }.joinToString(" ")
        assertFalse("Owner-overridden copy must not remain in current content", OWNER_PROHIBITED_COPY.containsMatchIn(currentCopy))

        // Classification tags remain machine-readable, but neither the card nor details translates them.
        assertTrue(item("quiet_color_hunt").safetyTags.isNotEmpty())
        assertTrue(pack.items.any { "parent_supervision" in it.safetyTags })
        assertTrue(item("quiet_color_hunt").collapsedCardPreviewLines().single().isNotBlank())
        assertTrue(item("quiet_color_hunt").detailSections().isNotEmpty())

        val main = readText(root.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))
        listOf(
            "item.collapsedCardDescriptionAnnotated()",
            "item.detailSections().forEach",
            "Text(item.parentNotes)",
        ).forEach { binding -> assertTrue("Missing reachable UI binding: $binding", main.contains(binding)) }
        assertFalse(main.contains("displayTagLabel"))
        assertFalse(main.contains("reviewedSafetyTagSummary"))
    }

    private val OWNER_PROHIBITED_COPY = Regex(
        "\\b(?:safe|safety|safely|unsafe|supervis(?:e|ed|ing|ion|ory)|adult[- ]approved|parent[- ]approved|" +
            "privacy|private|account|accounts|sign[ -]?in|log[ -]?in|profile|profiles)\\b|" +
            "within easy reach|stay within the boundary|without (?:sprinting|running|leaping|jumping)|" +
            "stop if (?:anyone )?(?:feels|is) unsure|carefully (?:on|across)|small steps only|" +
            "slippery or crowded floors|clear area away from|not a contact race|wide walking space|" +
            "small rabbit hops|quick, small steps|low-impact movement|no hopping or running",
        RegexOption.IGNORE_CASE,
    )

    private fun allUserVisibleContentPaths(pack: JsonObject): Set<String> = pack.getAsJsonArray("items")
        .flatMap { element ->
            val item = element.asJsonObject
            val id = item.string("id")
            buildList {
                CONTENT_FIELDS.forEach { field ->
                    val value = item.get(field) ?: return@forEach
                    if (value.isJsonArray) {
                        value.asJsonArray.forEachIndexed { index, text ->
                            if (text.asString.isNotBlank()) add("items[$id].$field[$index]")
                        }
                    } else if (value.asString.isNotBlank()) {
                        add("items[$id].$field")
                    }
                }
                item.getAsJsonObject("madLibs")?.let { madLibs ->
                    madLibs.getAsJsonArray("fields")?.forEachIndexed { index, field ->
                        val objectValue = field.asJsonObject
                        listOf("label", "example").forEach { property ->
                            if (objectValue.string(property).isNotBlank()) add("items[$id].madLibs.fields[$index].$property")
                        }
                    }
                    listOf("template", "readAloudNote").forEach { property ->
                        if (madLibs.string(property).isNotBlank()) add("items[$id].madLibs.$property")
                    }
                }
            }
        }
        .toSet()

    private fun contentText(pack: JsonObject, entry: JsonObject): String {
        val item = pack.getAsJsonArray("items")
            .map { it.asJsonObject }
            .single { it.string("id") == entry.string("itemId") }
        val suffix = entry.string("sourcePath").substringAfter("].")
        DIRECT_ARRAY_PATH.matchEntire(suffix)?.let { match ->
            return item.getAsJsonArray(match.groupValues[1])[match.groupValues[2].toInt()].asString
        }
        MAD_LIB_FIELD_PATH.matchEntire(suffix)?.let { match ->
            return item.getAsJsonObject("madLibs")
                .getAsJsonArray("fields")[match.groupValues[1].toInt()].asJsonObject
                .string(match.groupValues[2])
        }
        if (suffix.startsWith("madLibs.")) return item.getAsJsonObject("madLibs").string(suffix.substringAfter('.'))
        return item.string(suffix)
    }


    private fun readText(path: Path): String = String(Files.readAllBytes(path))

    private fun readJson(path: Path): JsonObject = Files.newBufferedReader(path).use {
        JsonParser.parseReader(it).asJsonObject
    }

    private fun JsonObject.string(name: String): String = get(name)?.asString.orEmpty()

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }

    companion object {
        private val DECISIONS = setOf("retain", "relocate", "collapse", "remove")
        private val SOURCE_TYPES = setOf("content", "kotlin", "safety_tag")
        private val SOURCE_LINE_SUFFIX = Regex(":\\d+$")
        private const val FEEDBACK_UI_PATH = "app/src/main/java/com/kinplay/app/feedback/FeedbackUi.kt"
        private const val INSTRUMENTATION_TEST_CLASS = "com.kinplay.app.SafetyWarningPresentationTest"
        private val CONTENT_FIELDS = listOf(
            "title", "summary", "materials", "setupSteps", "playSteps", "parentNotes", "variations", "promptText", "followUps",
        )
        private val DIRECT_ARRAY_PATH = Regex("([A-Za-z]+)\\[(\\d+)]")
        private val MAD_LIB_FIELD_PATH = Regex("madLibs\\.fields\\[(\\d+)]\\.(label|example)")
    }
}
