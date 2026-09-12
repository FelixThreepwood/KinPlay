package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KidPlayBrandingContractTest {
    @Test
    fun renderedAppSurfacesUseKidPlayWithoutChangingStableInternalIdentity() {
        val root = repositoryRoot()
        val mainSource = readText(root.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt"))

        assertTrue(mainSource.contains("Text(\"KidPlay\", fontSize = titleFontSize"))
        assertTrue(mainSource.contains("Text(\"KidPlay\", style = MaterialTheme.typography.headlineLarge"))
        assertTrue(
            mainSource.contains(
                "KidPlay is for adults to guide short play sessions with children. Review the activity, clear the space, and supervise movement or materials.",
            ),
        )
        assertTrue(mainSource.contains("root.optString(\"title\", \"KidPlay Seed Pack\")"))

        assertFalse(mainSource.contains("Text(\"KinPlay\""))
        assertFalse(mainSource.contains("KinPlay is for adults to guide short play sessions with children."))
        assertFalse(mainSource.contains("root.optString(\"title\", \"KinPlay Seed Pack\")"))
        assertTrue(mainSource.contains("context.assets.open(\"kinplay_seed_v1.json\")"))
        assertTrue(mainSource.contains("fun KinPlayApp()"))
    }

    @Test
    fun feedbackAndAccessibilitySurfacesUseKidPlay() {
        val root = repositoryRoot()
        val feedbackModels = readText(root.resolve("app/src/main/java/com/kinplay/app/feedback/FeedbackModels.kt"))
        val feedbackStore = readText(root.resolve("app/src/main/java/com/kinplay/app/feedback/FeedbackStore.kt"))
        val feedbackUi = readText(root.resolve("app/src/main/java/com/kinplay/app/feedback/FeedbackUi.kt"))
        val lockSource = readText(root.resolve("app/src/main/java/com/kinplay/app/lock/ChildHandoffLock.kt"))

        assertTrue(feedbackModels.contains("[KidPlay Beta][Feedback Batch]"))
        assertTrue(feedbackModels.contains("KidPlay app-development Discord channel"))
        assertTrue(feedbackStore.contains("ClipData.newPlainText(\"KidPlay feedback\""))
        assertTrue(feedbackUi.contains("Text(\"Return to KidPlay\")"))
        assertTrue(lockSource.contains("KidPlay controls are blocked"))

        assertFalse(feedbackModels.contains("[KinPlay Beta][Feedback Batch]"))
        assertFalse(feedbackModels.contains("KinPlay app-development Discord channel"))
        assertFalse(feedbackStore.contains("ClipData.newPlainText(\"KinPlay feedback\""))
        assertFalse(feedbackUi.contains("Text(\"Return to KinPlay\")"))
        assertFalse(lockSource.contains("KinPlay controls are blocked"))

        assertTrue(feedbackModels.contains("appendLine(\"KINPLAY_FEEDBACK_V1\")"))
    }

    @Test
    fun runtimeSeedPackUsesKidPlayTitleAndKeepsStablePackIdAndParity() {
        val root = repositoryRoot()
        val canonical = readText(root.resolve("content/seed/kinplay_seed_v1.json"))
        val runtime = readText(root.resolve("app/src/main/assets/kinplay_seed_v1.json"))

        assertTrue(canonical.contains("\"title\": \"KidPlay Seed Pack v1\""))
        assertTrue(runtime.contains("\"title\": \"KidPlay Seed Pack v1\""))
        assertFalse(canonical.contains("\"title\": \"KinPlay Seed Pack v1\""))
        assertFalse(runtime.contains("\"title\": \"KinPlay Seed Pack v1\""))
        assertTrue(runtime.contains("\"packId\": \"kinplay_seed_v1\""))
        assertEquals(canonical, runtime)
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }

    private fun readText(path: Path): String =
        Files.readAllBytes(path).toString(Charsets.UTF_8)
}
