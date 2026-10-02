package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeCopyReductionTest {
    private val mainSource = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))

    @Test
    fun reportedAndAuditedNonessentialCopyIsAbsentFromRenderedHomeListAndDetail() {
        listOf(
            "Offline, parent-led choices for ages 2–8",
            "Saved picks for faster family starts",
            "Return to what already worked",
            "offline local cards",
        ).forEach { copy ->
            assertFalse("Nonessential copy remains: $copy", mainSource.contains(copy))
        }
        assertFalse(
            "List content must not repeat the TopAppBar title",
            mainSource.contains("SectionTitle(title, \"${'$'}{items.size} offline local cards\")"),
        )
        assertFalse(
            "Details must not repeat the TopAppBar title",
            mainSource.contains("Text(item.title, style = MaterialTheme.typography.headlineMedium"),
        )
    }

    @Test
    fun compactHomeRetainsIdentityPurposeCategoriesActionsAndB6ShortcutScope() {
        listOf(
            "Text(\"KidPlay\"",
            "HOME_DESCRIPTOR,",
            "QuickCategoryGrid",
            "HOME_SHORTCUTS",
            "HOME_SHORTCUTS.forEach",
            "HomeButton(",
            "shortcut = shortcut",
        ).forEach { binding ->
            assertTrue("Required Home binding changed or disappeared: $binding", mainSource.contains(binding))
        }
        assertFalse("The removed instructional section must stay disabled", HOME_INSTRUCTION_SECTION_ENABLED)
    }

    @Test
    fun categoryCardsUseMinimumHeightInsteadOfClippingLargeText() {
        assertTrue(mainSource.contains(".heightIn(min = cardHeight)"))
        assertFalse(mainSource.contains(".height(cardHeight)"))
    }

    @Test
    fun activityDetailsKeepUsefulCopyWithoutTechnicalDisclosureLabels() {
        listOf(
            "Text(item.summary",
            "item.detailSections().forEach",
            "Text(item.parentNotes)",
        ).forEach { protectedCopy ->
            assertTrue("Activity detail binding disappeared: $protectedCopy", mainSource.contains(protectedCopy))
        }
        assertFalse(mainSource.contains("displayTagLabel"))
        assertFalse(mainSource.contains("reviewedSafetyTagSummary"))
        assertFalse(mainSource.contains("No account system is included in this MVP"))
    }

    @Test
    fun homeDoesNotShowAnUnsupportedReadinessPercentage() {
        assertFalse(mainSource.contains("StatPill(\"100%\", \"ready\")"))
    }

    private fun projectRoot(): Path {
        var current = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        repeat(8) {
            if (Files.exists(current.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))) return current
            current = current.parent ?: return@repeat
        }
        error("Could not locate project root from ${System.getProperty("user.dir")}")
    }

    private fun readText(path: Path): String = String(Files.readAllBytes(path))
}
