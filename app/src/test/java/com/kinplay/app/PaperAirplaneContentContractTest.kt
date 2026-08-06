package com.kinplay.app

import java.nio.file.Files
import java.nio.file.Path
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaperAirplaneContentContractTest {
    @Test
    fun canonicalPaperAirplaneEntryContainsBothModelsSixStepsAndVisualAssets() {
        val root = repositoryRoot()
        val json = JSONObject(String(Files.readAllBytes(root.resolve("content/seed/kinplay_seed_v1.json"))))
        val item = (0 until json.getJSONArray("items").length())
            .map { json.getJSONArray("items").getJSONObject(it) }
            .single { it.getString("id") == "paper_airplane_weather" }
        val models = item.getJSONArray("paperAirplaneModels")
        val assets = item.getJSONArray("visualAssets")

        assertEquals("Paper Airplanes (it's easy!) + Weather", item.getString("title"))
        assertEquals(2, models.length())
        assertEquals(setOf("basic_classic_dart", "glide_trickster"), (0 until models.length()).map { models.getJSONObject(it).getString("id") }.toSet())
        assertTrue((0 until models.length()).all { models.getJSONObject(it).getJSONArray("steps").length() == 6 })
        assertEquals(2, assets.length())
        assertTrue(item.getJSONArray("playSteps").toString().contains("pretend weather"))
    }

    private fun repositoryRoot(): Path {
        var current = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (!Files.exists(current.resolve("content/seed/kinplay_seed_v1.json"))) {
            current = current.parent ?: error("Could not find repository root")
        }
        return current
    }
}
