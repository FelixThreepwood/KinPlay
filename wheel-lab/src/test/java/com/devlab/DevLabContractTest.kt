package com.devlab

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DevLabContractTest {
    @Test
    fun labRetainsOnlyAnimalMovesAfterRemovingExploratoryDemos() {
        assertEquals(listOf("animals"), DEV_LAB_DEMOS.map { it.id })
        assertEquals("Animal moves", DEV_LAB_DEMOS.single().title)
        assertTrue(DEV_LAB_DEMOS.single().options.isNotEmpty())
    }
}
