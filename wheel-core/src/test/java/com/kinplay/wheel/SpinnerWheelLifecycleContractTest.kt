package com.kinplay.wheel

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpinnerWheelLifecycleContractTest {
    @Test
    fun busyStateIsTransientAndAnimationCancellationReleasesIt() {
        val source = readText(projectRoot().resolve("wheel-core/src/main/java/com/kinplay/wheel/SpinnerWheel.kt"))

        assertTrue(source.contains("var isSpinning by remember(optionKey)"))
        assertFalse(source.contains("var isSpinning by rememberSaveable(optionKey)"))
        assertTrue(source.contains("finally"))
    }

    private fun projectRoot(): Path {
        var current = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        repeat(8) {
            if (Files.exists(current.resolve("wheel-core/src/main/java/com/kinplay/wheel/SpinnerWheel.kt"))) return current
            current = current.parent ?: return@repeat
        }
        error("Could not locate project root from ${System.getProperty("user.dir")}")
    }

    private fun readText(path: Path): String = String(Files.readAllBytes(path))
}
