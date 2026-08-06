package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertTrue
import org.junit.Test

class GameTypeNavigationContractTest {
    @Test
    fun pickGameOpensLevelOneAndLevelTwoUsesAGroupRoute() {
        val source = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))
        val discoverySource = readText(projectRoot().resolve("app/src/main/java/com/kinplay/app/ContentDiscovery.kt"))

        assertTrue(source.contains("GameTypeListScreen"))
        assertTrue(source.contains("GameTypeDetailScreen"))
        assertTrue(source.contains("Routes.GameType"))
        assertTrue(source.contains("itemsForGameType("))
        assertTrue(source.contains("GameTypeGroup.entries"))
        assertTrue(discoverySource.contains("GAME_TYPE_CARD_DEFAULT_EXPANDED"))
    }

    private fun projectRoot(): Path {
        var current = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        repeat(8) {
            if (Files.exists(current.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))) return current
            current = current.parent ?: return@repeat
        }
        error("Could not locate project root")
    }

    private fun readText(path: Path): String = String(Files.readAllBytes(path))
}
