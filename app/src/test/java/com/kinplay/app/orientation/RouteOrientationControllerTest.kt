package com.kinplay.app.orientation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteOrientationControllerTest {
    @Test
    fun enteringPlaySavesThePriorRequestAndRestoresItOnce() {
        val host = FakeOrientationHost(requestedOrientation = 7)
        val controller = RouteOrientationController(host, landscapeOrientation = 11)

        controller.enterLandscape()
        controller.enterLandscape()
        assertEquals(11, host.requestedOrientation)
        assertEquals(listOf(11), host.writes)

        controller.restore()
        controller.restore()
        assertEquals(7, host.requestedOrientation)
        assertEquals(listOf(11, 7), host.writes)
    }

    @Test
    fun multiWindowEntryDoesNotForceLandscapeOrLaterRestoreARequest() {
        val host = FakeOrientationHost(requestedOrientation = 7, isInMultiWindowMode = true)
        val controller = RouteOrientationController(host, landscapeOrientation = 11)

        controller.enterLandscape()
        controller.restore()

        assertEquals(7, host.requestedOrientation)
        assertTrue(host.writes.isEmpty())
    }

    @Test
    fun configurationRecreationDoesNotRestoreTheOldRequestIntoTheNewActivity() {
        val host = FakeOrientationHost(requestedOrientation = 7, isChangingConfigurations = true)
        val controller = RouteOrientationController(host, landscapeOrientation = 11)

        controller.enterLandscape()
        controller.restore()

        assertEquals(11, host.requestedOrientation)
        assertEquals(listOf(11), host.writes)
        assertTrue(controller.hasActiveLease)

        host.isChangingConfigurations = false
        controller.restore()
        assertEquals(7, host.requestedOrientation)
        assertEquals(listOf(11, 7), host.writes)
        assertFalse(controller.hasActiveLease)
    }

    private class FakeOrientationHost(
        override var requestedOrientation: Int,
        override var isInMultiWindowMode: Boolean = false,
        override var isChangingConfigurations: Boolean = false,
    ) : RouteOrientationHost {
        val writes = mutableListOf<Int>()

        override fun writeRequestedOrientation(value: Int) {
            requestedOrientation = value
            writes += value
        }
    }
}
