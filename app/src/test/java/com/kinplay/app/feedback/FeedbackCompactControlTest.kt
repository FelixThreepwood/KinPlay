package com.kinplay.app.feedback

import java.nio.file.Files
import java.nio.file.Path
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedbackCompactControlTest {
    private val source by lazy {
        Files.newBufferedReader(repositoryRoot().resolve("app/src/main/java/com/kinplay/app/feedback/FeedbackUi.kt")).use { it.readText() }
    }

    @Test
    fun feedbackLauncherUsesCompactNoteControlWithAccessibleLabel() {
        assertTrue(source.contains("FloatingActionButton"))
        assertTrue(source.contains("📝"))
        assertTrue(source.contains("Open feedback"))
        assertTrue(source.contains("feedback-control"))
        assertFalse(source.contains("ExtendedFloatingActionButton"))
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
