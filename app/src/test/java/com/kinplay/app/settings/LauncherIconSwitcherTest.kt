package com.kinplay.app.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LauncherIconSwitcherTest {
    @Test
    fun paletteAndCodecAreFiniteAndBackwardCompatible() {
        assertEquals(listOf("teal", "sunshine"), LauncherIconVariant.entries.map { it.wireValue })
        assertEquals(LauncherIconVariant.TEAL, LauncherIconVariant.fromWireValue(null))
        assertEquals(LauncherIconVariant.TEAL, LauncherIconVariant.fromWireValue("unknown"))
        assertEquals(LauncherIconVariant.SUNSHINE, LauncherIconVariant.fromWireValue("sunshine"))
    }

    @Test
    fun selectionPlanEnablesTargetBeforeDisablingEveryOtherAlias() {
        assertEquals(
            LauncherIconSelectionPlan(
                enableFirst = LauncherIconComponents.SUNSHINE,
                disableAfter = listOf(LauncherIconComponents.TEAL),
            ),
            LauncherIconSelectionPlan.forVariant(LauncherIconVariant.SUNSHINE),
        )
    }

    @Test
    fun applyingTheCurrentVariantIsIdempotent() {
        val gateway = FakeGateway(enabled = mutableSetOf(LauncherIconComponents.TEAL))

        val result = LauncherIconSwitcher(gateway).switchTo(LauncherIconVariant.TEAL)

        assertEquals(LauncherIconSwitchResult.ALREADY_APPLIED, result)
        assertTrue(gateway.writes.isEmpty())
        assertEquals(setOf(LauncherIconComponents.TEAL), gateway.enabled)
    }

    @Test
    fun successfulSwitchEnablesFirstAndLeavesExactlyOneAliasEnabled() {
        val gateway = FakeGateway(enabled = mutableSetOf(LauncherIconComponents.TEAL))

        val result = LauncherIconSwitcher(gateway).switchTo(LauncherIconVariant.SUNSHINE)

        assertEquals(LauncherIconSwitchResult.APPLIED, result)
        assertEquals(
            listOf(
                LauncherIconComponents.SUNSHINE to true,
                LauncherIconComponents.TEAL to false,
            ),
            gateway.writes,
        )
        assertEquals(setOf(LauncherIconComponents.SUNSHINE), gateway.enabled)
    }

    @Test
    fun packageManagerFailureRollsBackToThePreviouslyLaunchableAlias() {
        val gateway = FakeGateway(
            enabled = mutableSetOf(LauncherIconComponents.TEAL),
            failOnceOn = LauncherIconComponents.TEAL to false,
        )

        val result = LauncherIconSwitcher(gateway).switchTo(LauncherIconVariant.SUNSHINE)

        assertEquals(LauncherIconSwitchResult.FAILED_SAFE, result)
        assertEquals(setOf(LauncherIconComponents.TEAL), gateway.enabled)
    }

    @Test
    fun targetEnableFailureNeverDisablesTheSafeCurrentAlias() {
        val gateway = FakeGateway(
            enabled = mutableSetOf(LauncherIconComponents.TEAL),
            failOnceOn = LauncherIconComponents.SUNSHINE to true,
        )

        val result = LauncherIconSwitcher(gateway).switchTo(LauncherIconVariant.SUNSHINE)

        assertEquals(LauncherIconSwitchResult.FAILED_SAFE, result)
        assertEquals(setOf(LauncherIconComponents.TEAL), gateway.enabled)
        assertTrue(LauncherIconComponents.TEAL to false !in gateway.writes)
    }

    private class FakeGateway(
        val enabled: MutableSet<String>,
        private var failOnceOn: Pair<String, Boolean>? = null,
    ) : LauncherIconComponentGateway {
        val writes = mutableListOf<Pair<String, Boolean>>()

        override fun isEnabled(componentClassName: String): Boolean = componentClassName in enabled

        override fun setEnabled(componentClassName: String, enabled: Boolean) {
            writes += componentClassName to enabled
            if (failOnceOn == componentClassName to enabled) {
                failOnceOn = null
                throw IllegalStateException("simulated PackageManager failure")
            }
            if (enabled) this.enabled += componentClassName else this.enabled -= componentClassName
        }
    }
}