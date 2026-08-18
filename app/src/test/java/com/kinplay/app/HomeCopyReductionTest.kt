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
            "Text(\"KinPlay\"",
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
    fun protectedSafetyPrivacyAndInstructionalDetailCopyRemainsUnchanged() {
        listOf(
            "Text(item.summary",
            "item.detailSections().forEach",
            "Text(item.parentNotes)",
            "Safety tags: ${'$'}{item.safetyTags.joinToString { it.displayTagLabel() }}",
            "KinPlay is for adults to guide short play sessions with children. Review the activity, clear the space, and supervise movement or materials.",
            "No accounts, analytics, ads, purchases, camera, microphone, contacts, location, or other sensitive Android permissions are requested.",
        ).forEach { protectedCopy ->
            assertTrue("Protected or functional copy changed: $protectedCopy", mainSource.contains(protectedCopy))
        }
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
