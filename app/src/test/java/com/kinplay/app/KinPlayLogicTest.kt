package com.kinplay.app

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
}
