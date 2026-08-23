package com.devlab

import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class DevLabWindowContractTest {
    private val source = String(
        Files.readAllBytes(repositoryRoot().resolve("wheel-lab/src/main/java/com/devlab/DevLabActivity.kt")),
    )

    @Test
    fun activityRestoresSystemBarsAndAppliesNavigationBarPadding() {
        assertTrue(source.contains("WindowCompat.setDecorFitsSystemWindows(window, true)"))
        assertTrue(source.contains("window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)"))
        assertTrue(source.contains("WindowCompat.getInsetsController(window, window.decorView)"))
        assertTrue(source.contains("systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT"))
        assertTrue(source.contains("show(WindowInsetsCompat.Type.systemBars())"))
        assertTrue(source.contains(".navigationBarsPadding()"))
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("settings.gradle.kts"))) current = current.parent
        return current
    }
}
