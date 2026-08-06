package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeShortcutGraphicsTest {
    private val mainSource = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))

    @Test
    fun homeShortcutsUseStableGraphicalCuesAndRetainReadableLabels() {
        assertEquals(
            listOf("refresh", "grid_view"),
            HOME_SHORTCUTS.map { it.icon },
        )
        assertEquals(
            listOf(
                RANDOM_GAME_LABEL,
                ALL_GAMES_AND_ACTIVITIES_LABEL,
            ),
            HOME_SHORTCUTS.map { it.title },
        )
        assertTrue(HOME_SHORTCUTS.all { it.description.isNotBlank() })
    }

    @Test
    fun shortcutControlsRenderIconAndLabelWithoutLongSubtext() {
        val homeButtonSource = mainSource.substringAfter("fun HomeButton").substringBefore("@Composable")
        assertTrue(mainSource.contains("HomeShortcutIcon"))
        assertTrue(mainSource.contains("Icons.Default.Refresh"))
        assertTrue(mainSource.contains("Icons.Default.GridView"))
        assertTrue(mainSource.contains("Text(shortcut.title"))
        assertTrue(mainSource.contains("shortcut.description"))
        assertFalse(homeButtonSource.contains("Text(subtitle"))
        assertFalse(mainSource.contains("More ways to start"))
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
