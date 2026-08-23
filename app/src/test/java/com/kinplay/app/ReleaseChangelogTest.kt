package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path

class ReleaseChangelogTest {
    private val root = repositoryRoot()
    private val changelogPath = root.resolve("app/src/main/java/com/kinplay/app/ReleaseChangelog.kt")
    private val mainSource = String(Files.readAllBytes(root.resolve("app/src/main/java/com/kinplay/app/MainActivity.kt")))
    private val buildFile = String(Files.readAllBytes(root.resolve("app/build.gradle.kts")))

    @Test
    fun currentReleaseUsesTheSecondCumulativeFeedbackRevision() {
        assertTrue(buildFile.contains("val appVersionName = \"0.7.2\""))
        assertTrue(buildFile.contains("versionCode = 15"))
    }

    @Test
    fun aboutScreenRendersVersionedReleaseNotes() {
        assertTrue(mainSource.contains("Release notes"))
        assertTrue(mainSource.contains("KIDPLAY_RELEASE_CHANGELOG"))
    }

    @Test
    fun fourBatchAuditItemsHaveVersionedFiveToTenWordSummaries() {
        val expectedByVersion = mapOf(
            "0.6.3" to (listOf("KPF-0004", "KPF-0010", "KPF-0017", "KPF-0055", "KPF-0058") + (64..77).map { "KPF-%04d".format(it) }),
            "0.7.1" to listOf("KPF-0022", "KPF-0043", "KPF-0060", "KPF-0078") + (79..88).map { "KPF-%04d".format(it) },
            "0.7.2" to listOf("KPF-0022", "KPF-0063", "KPF-0089"),
        )
        assertTrue("Release changelog source is missing", Files.exists(changelogPath))
        val changelogSource = String(Files.readAllBytes(changelogPath))
        expectedByVersion.forEach { (version, ids) ->
            val releaseBlock = changelogSource.substringAfter("version = \"$version\"").substringBefore("ReleaseVersion(")
            val entries = Regex("ReleaseChange\\(\"(KPF-\\d{4})\", \"([^\"]+)\"\\)")
                .findAll(releaseBlock)
                .associate { it.groupValues[1] to it.groupValues[2] }
            assertEquals("Wrong changelog item count for $version", ids.size, entries.size)
            ids.forEach { id ->
                val summary = entries.getValue(id)
                assertTrue("$id summary must contain 5-10 words: $summary", summary.trim().split(Regex("\\s+")).size in 5..10)
                assertTrue("Missing $id in source", changelogSource.contains(id))
            }
        }
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) current = current.parent
        return current
    }
}
