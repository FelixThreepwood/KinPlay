package com.devlab

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DevLabContractTest {
    @Test
    fun labProvidesThreeDistinctDatasetsForDeviceExploration() {
        assertEquals(listOf("animals", "colors", "long_labels"), DEV_LAB_DEMOS.map { it.id })
        assertEquals(3, DEV_LAB_DEMOS.map { it.id }.toSet().size)
        assertTrue(DEV_LAB_DEMOS.all { it.options.isNotEmpty() })
    }

    @Test
    fun longLabelDatasetExercisesTextFitting() {
        val demo = DEV_LAB_DEMOS.single { it.id == "long_labels" }

        assertTrue(demo.options.any { it.label.length >= 18 })
        assertTrue(demo.options.all { it.detail.isNotBlank() })
    }
}
