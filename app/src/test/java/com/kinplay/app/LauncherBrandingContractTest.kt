package com.kinplay.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest

class LauncherBrandingContractTest {
    @Test
    fun visibleApplicationBrandingUsesKidPlayAndTheFoxHeartIcon() {
        val root = repositoryRoot()
        val manifest = readText(root.resolve("app/src/main/AndroidManifest.xml"))
        val strings = readText(root.resolve("app/src/main/res/values/strings.xml"))
        val tealIcon = readText(root.resolve("app/src/main/res/mipmap-anydpi-v26/ic_launcher_teal.xml"))
        val tealForeground = readText(root.resolve("app/src/main/res/drawable/launcher_icon_teal_foreground.xml"))
        val foxMaster = root.resolve("app/src/main/res/drawable-nodpi/launcher_icon_fox_heart_master.jpg")

        assertTrue("The visible app label must be centralized in app_name", manifest.contains("android:label=\"@string/app_name\""))
        assertTrue("The application label must be KidPlay", strings.contains("<string name=\"app_name\">KidPlay</string>"))
        assertEquals("The default launcher alias labels must use app_name", 3, manifest.split("android:label=\"@string/app_name\"").size - 1)
        assertTrue("The default adaptive icon must use the Fox Heart background", tealIcon.contains("@color/launcher_fox_navy"))
        assertTrue("The default adaptive icon must use the Fox Heart foreground", tealIcon.contains("@drawable/launcher_icon_teal_foreground"))
        assertTrue("The default foreground must reference the Fox Heart master", tealForeground.contains("@drawable/launcher_icon_fox_heart_master"))
        assertTrue("The Fox Heart master must be packaged", Files.exists(foxMaster))
        assertEquals(
            "a8dd209cd588e0f1de4c9d58668b851ab477434ad5a5f93a5217dcb109bdbd5b",
            sha256(Files.readAllBytes(foxMaster)),
        )
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

    private fun sha256(bytes: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
}
