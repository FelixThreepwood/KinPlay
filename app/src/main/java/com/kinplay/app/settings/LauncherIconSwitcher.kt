package com.kinplay.app.settings

/** The complete launcher-icon palette. These wire values are persisted and must remain stable. */
enum class LauncherIconVariant(
    val wireValue: String,
    val label: String,
    val description: String,
    val componentClassName: String,
) {
    TEAL(
        wireValue = "teal",
        label = "Fox Heart",
        description = "Orange fox curled around a gold heart on deep navy",
        componentClassName = LauncherIconComponents.TEAL,
    ),
    SUNSHINE(
        wireValue = "sunshine",
        label = "Sunshine",
        description = "Warm sunshine with a deep teal emblem",
        componentClassName = LauncherIconComponents.SUNSHINE,
    ),
    ;

    companion object {
        fun fromWireValue(value: String?): LauncherIconVariant =
            entries.firstOrNull { it.wireValue == value } ?: TEAL
    }
}

object LauncherIconComponents {
    const val TEAL = "com.kinplay.app.LauncherTeal"
    const val SUNSHINE = "com.kinplay.app.LauncherSunshine"
    val all = listOf(TEAL, SUNSHINE)
}

data class LauncherIconSelectionPlan(
    val enableFirst: String,
    val disableAfter: List<String>,
) {
    companion object {
        fun forVariant(variant: LauncherIconVariant) = LauncherIconSelectionPlan(
            enableFirst = variant.componentClassName,
            disableAfter = LauncherIconComponents.all.filterNot { it == variant.componentClassName },
        )
    }
}

interface LauncherIconComponentGateway {
    fun isEnabled(componentClassName: String): Boolean
    fun setEnabled(componentClassName: String, enabled: Boolean)
}

enum class LauncherIconSwitchResult {
    APPLIED,
    ALREADY_APPLIED,
    FAILED_SAFE,
}

/**
 * Enables the requested alias before disabling the old one, so a non-atomic PackageManager
 * failure never intentionally leaves the app without a launcher entry. A partial failure is
 * rolled back to the single previously enabled alias (or Teal, the manifest-safe default).
 */
class LauncherIconSwitcher(private val gateway: LauncherIconComponentGateway) {
    fun switchTo(variant: LauncherIconVariant): LauncherIconSwitchResult {
        val snapshot = runCatching { enabledComponents() }
            .getOrElse { return LauncherIconSwitchResult.FAILED_SAFE }
        val target = variant.componentClassName
        if (snapshot == setOf(target)) return LauncherIconSwitchResult.ALREADY_APPLIED

        val plan = LauncherIconSelectionPlan.forVariant(variant)
        return try {
            if (target !in snapshot) gateway.setEnabled(plan.enableFirst, true)
            plan.disableAfter.forEach { component ->
                if (gateway.isEnabled(component)) gateway.setEnabled(component, false)
            }
            check(enabledComponents() == setOf(target))
            LauncherIconSwitchResult.APPLIED
        } catch (_: Throwable) {
            restoreLaunchableSnapshot(snapshot)
            LauncherIconSwitchResult.FAILED_SAFE
        }
    }

    private fun enabledComponents(): Set<String> =
        LauncherIconComponents.all.filterTo(mutableSetOf(), gateway::isEnabled)

    private fun restoreLaunchableSnapshot(snapshot: Set<String>) {
        val safeComponent = snapshot.singleOrNull() ?: LauncherIconComponents.TEAL
        val safeIsEnabled = runCatching {
            if (!gateway.isEnabled(safeComponent)) gateway.setEnabled(safeComponent, true)
            gateway.isEnabled(safeComponent)
        }.getOrDefault(false)
        if (!safeIsEnabled) return

        LauncherIconComponents.all.filterNot { it == safeComponent }.forEach { component ->
            runCatching {
                if (gateway.isEnabled(component)) gateway.setEnabled(component, false)
            }
        }
    }
}