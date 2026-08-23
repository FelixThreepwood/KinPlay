package com.devlab

import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class DevLabAboutTest {
    private val root = repositoryRoot()
    private val source = String(Files.readAllBytes(root.resolve("wheel-lab/src/main/java/com/devlab/DevLabActivity.kt")))
    private val buildFile = String(Files.readAllBytes(root.resolve("wheel-lab/build.gradle.kts")))

    @Test
    fun devLabRevisionAndAboutChangelogAreVisible() {
        assertTrue(buildFile.contains("versionCode = 4"))
        assertTrue(buildFile.contains("versionName = \"0.2.3\""))
        assertTrue(source.contains("About Dev Lab"))
        assertTrue(source.contains("Version ${'$'}{BuildConfig.VERSION_NAME}"))
        assertTrue(source.contains("Release notes"))
        assertTrue(source.contains("Feedback forms now preserve active demo context"))
        assertTrue(source.contains("Feedback lists retain state during navigation"))
        assertTrue(source.contains("0.2.3: Keep Animal moves and remove extra demos"))
        assertTrue(source.contains("System navigation remains visible at launch"))
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("settings.gradle.kts"))) current = current.parent
        return current
    }
}
