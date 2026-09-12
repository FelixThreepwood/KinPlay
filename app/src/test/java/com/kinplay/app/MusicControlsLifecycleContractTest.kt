package com.kinplay.app

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class MusicControlsLifecycleContractTest {
    private val source by lazy {
        String(Files.readAllBytes(repositoryRoot().resolve("app/src/main/java/com/kinplay/app/MusicControls.kt")))
    }

    @Test
    fun playbackFlagIsNotRestoredWithoutTheMediaPlayerInstance() {
        assertTrue(source.contains("var isPlaying by remember(itemId) { mutableStateOf(false) }"))
        assertFalse(source.contains("var isPlaying by rememberSaveable(itemId)"))
    }

    @Test
    fun audioAttributesArePassedToTheFactoryThatPreparesThePlayer() {
        assertTrue(source.contains("val audioAttributes = AudioAttributes.Builder()"))
        assertTrue(source.contains("MediaPlayer.create(context, selectedTrack.resourceId, audioAttributes, 0)"))
        assertFalse(source.contains("MediaPlayer.create(context, selectedTrack.resourceId)?.apply {"))
        assertFalse(source.contains("setAudioAttributes("))
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
