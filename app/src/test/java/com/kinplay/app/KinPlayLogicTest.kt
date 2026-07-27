package com.kinplay.app

import com.kinplay.app.feedback.FeedbackCaptureContext
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KinPlayLogicTest {
    private val activeQuick = KinPlayItem(
        id = "a",
        type = "activity",
        status = "active",
        title = "Active Quick",
        summary = "Ready",
        modes = listOf("quick_play"),
        minAge = 2,
        maxAge = 8,
        durationMinutes = 5,
        energyLevel = "medium",
    )
    private val inactiveQuick = activeQuick.copy(id = "b", status = "draft", title = "Draft")
    private val calmPrompt = activeQuick.copy(id = "c", type = "prompt", title = "Calm", modes = listOf("calm_down"), energyLevel = "calm")

    @Test
    fun quickCategoryGridUsesTheSixRequestedChoicesInOrder() {
        assertEquals(
            listOf(
                "Quiet Games",
                "At the Dinner Table",
                "Outdoor Adventures",
                "Get the Energy Out",
                "Brain Games",
                "Quality Time",
            ),
            QuickCategory.defaultGrid.map { it.label },
        )
    }

    @Test
    fun oneGameCanAppearInMoreThanOneQuickCategory() {
        val ifToysCouldTalk = activeQuick.copy(
            id = "if_toys_could_talk",
            title = "If Toys Could Talk",
            quickCategories = listOf("quiet_games", "quality_time"),
        )

        assertEquals(listOf(ifToysCouldTalk), listOf(ifToysCouldTalk).itemsForQuickCategory("quiet_games"))
        assertEquals(listOf(ifToysCouldTalk), listOf(ifToysCouldTalk).itemsForQuickCategory("quality_time"))
    }

    @Test
    fun quickCategoryFilteringExcludesDraftContent() {
        val taggedActive = activeQuick.copy(quickCategories = listOf("brain_games"))
        val taggedDraft = taggedActive.copy(id = "draft", status = "draft")

        assertEquals(listOf(taggedActive), listOf(taggedActive, taggedDraft).itemsForQuickCategory("brain_games"))
    }

    @Test
    fun fullGameLibraryIncludesMadLibsAsOrdinaryGames() {
        val madLib = activeQuick.copy(id = "story", type = "mad_libs", modes = listOf("mad_libs"))
        val pack = ContentPack(items = listOf(activeQuick, madLib, inactiveQuick))

        assertEquals(listOf(activeQuick, madLib), pack.gameLibraryItems())
    }

    @Test
    fun directItemLookupExcludesDraftContent() {
        val pack = ContentPack(items = listOf(activeQuick, inactiveQuick))

        assertEquals(activeQuick, pack.activeItemById(activeQuick.id))
        assertEquals(null, pack.activeItemById(inactiveQuick.id))
    }

    @Test
    fun favoriteSelectionsExcludeDraftContent() {
        val pack = ContentPack(items = listOf(activeQuick, inactiveQuick))

        assertEquals(listOf(activeQuick), pack.favoriteItems(setOf(activeQuick.id, inactiveQuick.id)))
    }

    @Test
    fun recentSelectionsExcludeDraftContent() {
        val pack = ContentPack(items = listOf(activeQuick, inactiveQuick))

        assertEquals(listOf(activeQuick), pack.recentItems(listOf(inactiveQuick.id, activeQuick.id)))
    }

    @Test
    fun gameCardsAreCollapsedByDefault() {
        assertFalse(CONTENT_CARD_DEFAULT_EXPANDED)
    }

    @Test
    fun wouldYouRatherSeedItemResolvesToTheDedicatedRouteInsteadOfGenericDetail() {
        val seedItem = activeQuick.copy(id = WOULD_YOU_RATHER_ITEM_ID, title = "Would You Rather")

        assertEquals(WOULD_YOU_RATHER_ROUTE, itemDestination(seedItem))
        assertEquals("detail/${activeQuick.id}", itemDestination(activeQuick))
    }

    @Test
    fun activityDetailFeedbackIsAvailableOnlyWhenUnlockedAndKeepsItemContext() {
        assertEquals(
            FeedbackCaptureContext(
                screen = "detail/${activeQuick.id}",
                contentId = activeQuick.id,
                contentTitle = activeQuick.title,
            ),
            activityDetailFeedbackCapture(
                feedbackEnabled = true,
                isLocked = false,
                itemId = activeQuick.id,
                item = activeQuick,
            ),
        )
        assertEquals(
            null,
            activityDetailFeedbackCapture(
                feedbackEnabled = true,
                isLocked = true,
                itemId = activeQuick.id,
                item = activeQuick,
            ),
        )
        assertEquals(
            null,
            activityDetailFeedbackCapture(
                feedbackEnabled = false,
                isLocked = false,
                itemId = activeQuick.id,
                item = activeQuick,
            ),
        )
    }

    @Test
    fun itemsForModeReturnsOnlyActiveContentForThatMode() {
        val result = listOf(activeQuick, inactiveQuick, calmPrompt).itemsForMode("quick_play")

        assertEquals(listOf(activeQuick), result)
    }

    @Test
    fun pickForMeUsesSeedToChooseStableActiveModeItem() {
        val result = listOf(activeQuick, calmPrompt).pickForMode("quick_play", seed = 3)

        assertEquals(activeQuick, result)
    }

    @Test
    fun recentIdsMoveMostRecentToFrontAndDeduplicate() {
        val result = listOf("old", "a", "older").withRecentFirst("a", limit = 3)

        assertEquals(listOf("a", "old", "older"), result)
    }

    @Test
    fun toggleFavoriteAddsAndRemovesIds() {
        val added = emptySet<String>().toggleFavorite("a")
        val removed = added.toggleFavorite("a")

        assertTrue("a" in added)
        assertFalse("a" in removed)
    }

    @Test
    fun displayAgeRangeUsesMaximumAgeWhenPresent() {
        val toddlerItem = activeQuick.copy(minAge = 2, maxAge = 5)
        val exactAgeItem = activeQuick.copy(minAge = 6, maxAge = 6)

        assertEquals("Ages 2–5", toddlerItem.displayAgeRange())
        assertEquals("Age 6", exactAgeItem.displayAgeRange())
    }

    @Test
    fun displaySafetyTagHumanizesSnakeCaseTags() {
        assertEquals("Parent supervision", "parent_supervision".displayTagLabel())
        assertEquals("No materials", "no_materials".displayTagLabel())
    }

    @Test
    fun pickForModeAvoidingRecentPrefersUnplayedEligibleItems() {
        val newer = activeQuick.copy(id = "newer", title = "Newer")
        val older = activeQuick.copy(id = "older", title = "Older")

        val result = listOf(newer, older).pickForModeAvoidingRecent(
            mode = "quick_play",
            recentIds = listOf("newer"),
            seed = 1,
        )

        assertEquals(older, result)
    }

    @Test
    fun promptDetailSectionsIncludePromptAndFollowUps() {
        val prompt = activeQuick.copy(
            type = "prompt",
            promptText = "What made you smile today?",
            followUps = listOf("Who was there?", "Would you do it again?"),
        )

        val sections = prompt.detailSections()

        assertTrue(sections.any { it.title == "Prompt" && it.lines == listOf("What made you smile today?") })
        assertTrue(sections.any { it.title == "Follow-up questions" && it.lines == listOf("Who was there?", "Would you do it again?") })
    }

    @Test
    fun kpf0001QuietGamesBeginWithExactFamiliarOrder() {
        val familiar = listOf(
            activeQuick.copy(id = "alphabet_story", title = "Alphabet Story", quickCategories = listOf("quiet_games")),
            activeQuick.copy(id = "animal_guessing", title = "Animal Guessing", quickCategories = listOf("quiet_games")),
            activeQuick.copy(id = "would_you_rather", title = "Would You Rather", quickCategories = listOf("quiet_games")),
            activeQuick.copy(id = "charades", title = "Charades", quickCategories = listOf("quiet_games")),
            activeQuick.copy(id = "i_spy", title = "I Spy", quickCategories = listOf("quiet_games")),
        )
        val madLib = activeQuick.copy(
            id = "story_one",
            type = "mad_libs",
            title = "Story One",
            modes = listOf("mad_libs"),
            quickCategories = listOf("quiet_games"),
        )
        val pack = ContentPack(items = familiar.reversed() + madLib)

        assertEquals(
            listOf("I Spy", "Charades", "Would You Rather", "Animal Guessing", "Alphabet Story", "Mad Libs"),
            pack.quietGamesDisplayItems().take(6).map { it.title },
        )
    }

    @Test
    fun kpf0002QuietGamesUseOneMadLibsCollectionAndKeepStoriesOpenable() {
        val storyOne = activeQuick.copy(id = "story_one", type = "mad_libs", title = "Story One", modes = listOf("mad_libs"), quickCategories = listOf("quiet_games"))
        val storyTwo = storyOne.copy(id = "story_two", title = "Story Two")
        val draftStory = storyOne.copy(id = "draft_story", status = "draft")
        val quietGame = activeQuick.copy(id = "quiet", quickCategories = listOf("quiet_games"))
        val pack = ContentPack(items = listOf(quietGame, storyOne, storyTwo, draftStory))

        val displayed = pack.quietGamesDisplayItems()

        assertEquals(1, displayed.count { it.isMadLibsCollection() })
        assertFalse(displayed.any { it.type == "mad_libs" })
        assertEquals(listOf(storyOne, storyTwo), pack.madLibs())
        assertEquals(storyOne, pack.activeItemById("story_one"))
        assertTrue(pack.gameLibraryItems().containsAll(listOf(storyOne, storyTwo)))
    }

    @Test
    fun kpf0006HomeRevisionUsesCompactOneLineDescriptorWithoutInstructionSection() {
        assertTrue(HOME_DESCRIPTOR.isNotBlank())
        assertFalse(HOME_DESCRIPTOR.contains('\n'))
        assertFalse(HOME_INSTRUCTION_SECTION_ENABLED)
        assertEquals("Browse All Games & Activities", BROWSE_LIBRARY_LABEL)
    }

    @Test
    fun kpf0007AllSixCategoriesHaveNonblankSafePlaceCues() {
        assertEquals(6, QuickCategory.defaultGrid.size)
        QuickCategory.defaultGrid.forEach { category ->
            assertTrue("${category.id} needs a place cue", category.placeCue.isNotBlank())
            assertFalse(category.placeCue.contains(Regex("driver|driving|behind the wheel", RegexOption.IGNORE_CASE)))
            assertFalse(category.placeCue.contains(Regex("\\d+\\s+(games|activities)", RegexOption.IGNORE_CASE)))
        }
    }

    @Test
    fun kpf0008ParticipantSuitabilityRendersHumanReadableLabels() {
        assertEquals("Best for 1:1", ParticipantSuitability.ONE_ON_ONE.displayLabel)
        assertEquals("Best for a group", ParticipantSuitability.GROUP.displayLabel)
        assertEquals("Works 1:1 or with a group", ParticipantSuitability.BOTH.displayLabel)
        assertEquals("Works 1:1 or with a group", activeQuick.copy(participantSuitability = ParticipantSuitability.BOTH).participantFitLabel())
        assertTrue(QuickCategory.QUALITY_TIME.placeCue.contains("1:1"))
        assertTrue(QuickCategory.QUALITY_TIME.placeCue.contains("group", ignoreCase = true))
    }

    @Test
    fun kpf0010SetupBurdenIsReadyToShowBeforeOpeningInstructions() {
        assertEquals("No materials", activeQuick.setupBurdenLabel())
        assertEquals(
            "Needs: paper, washable crayons",
            activeQuick.copy(materials = listOf("paper", "washable crayons")).setupBurdenLabel(),
        )
    }

    @Test
    fun collapsedCardPreviewShowsMaterialsAndSetupFromFirstSetupStep() {
        val item = activeQuick.copy(
            materials = listOf("paper", "washable crayons"),
            setupSteps = listOf("Clear a small table.", "Put the crayons in the middle."),
        )

        assertEquals(
            listOf("Needs: paper, washable crayons", "Setup: Clear a small table."),
            item.collapsedCardPreviewLines(),
        )
    }

    @Test
    fun collapsedCardPreviewHandlesMissingSetupSafely() {
        assertEquals(
            listOf("No materials", "Setup: No setup needed"),
            activeQuick.copy(setupSteps = emptyList()).collapsedCardPreviewLines(),
        )
    }

    @Test
    fun setupPreviewShortensLongFirstStepAtAWordBoundary() {
        val item = activeQuick.copy(
            setupSteps = listOf("Clear the very long dining table and place every supply in the center."),
        )

        assertEquals("Setup: Clear the very long…", item.setupPreviewLabel(maxCharacters = 32))
    }

    @Test
    fun contentListBackLabelsDescribeTheScreenPopBackStackActuallyReturnsTo() {
        assertEquals("Back home", contentListBackLabel())
        assertEquals("Back to Quiet Games", contentListBackLabel(isMadLibsSubmenu = true))
    }

    @Test
    fun madLibReadAloudNoteSurvivesJsonParsingAndAppearsInDetailSections() {
        val parsed = KinPlayItem.fromJson(
            JSONObject(
                """
                {
                  "id": "test_story",
                  "type": "mad_libs",
                  "status": "active",
                  "title": "Test Story",
                  "summary": "A parser regression story.",
                  "modes": ["mad_libs"],
                  "minAge": 4,
                  "maxAge": 8,
                  "durationMinutes": 5,
                  "energyLevel": "calm",
                  "materials": [],
                  "safetyTags": ["quiet"],
                  "madLibs": {
                    "fields": [
                      {"key": "animal", "label": "An animal", "kind": "animal", "example": "otter"}
                    ],
                    "template": "The {animal} waved.",
                    "readAloudNote": "Use a grand storyteller voice."
                  }
                }
                """.trimIndent(),
            ),
        )

        assertEquals("Use a grand storyteller voice.", parsed.readAloudNote)
        assertTrue(
            parsed.detailSections().any {
                it.title == "Read-aloud note" && it.lines == listOf("Use a grand storyteller voice.")
            },
        )
    }
}
