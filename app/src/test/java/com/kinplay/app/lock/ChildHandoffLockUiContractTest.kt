package com.kinplay.app.lock

import java.nio.file.Files
import java.nio.file.Path
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChildHandoffLockUiContractTest {
    private val source by lazy { Files.newBufferedReader(repositoryRoot().resolve("app/src/main/java/com/kinplay/app/lock/ChildHandoffLock.kt")).use { it.readText() } }

    @Test
    fun controlUsesCompactLockAndKeyStatesAndIndependentAccessibleLabel() {
        assertTrue(source.contains("🔒"))
        assertTrue(source.contains("🔑"))
        assertTrue(source.contains("Child handoff lock control"))
        assertTrue(source.contains("Hold for 3 seconds to"))
    }

    @Test
    fun lockedSurfaceKeepsContentClearAndGuidesOnlyAfterBlockedTap() {
        assertFalse(source.contains("scrim.copy(alpha = 0.58f)"))
        assertTrue(source.contains("Hold key for 3 seconds to unlock"))
        assertTrue(source.contains("onTap"))
        assertTrue(source.contains("showUnlockGuidance"))
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
