package com.kinplay.app.settings

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

class AndroidLauncherIconGateway(context: Context) : LauncherIconComponentGateway {
    private val applicationContext = context.applicationContext
    private val packageManager = applicationContext.packageManager

    override fun isEnabled(componentClassName: String): Boolean {
        return when (packageManager.getComponentEnabledSetting(component(componentClassName))) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED,
            -> false
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT -> componentClassName == LauncherIconComponents.TEAL
            else -> false
        }
    }

    override fun setEnabled(componentClassName: String, enabled: Boolean) {
        packageManager.setComponentEnabledSetting(
            component(componentClassName),
            if (enabled) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            },
            PackageManager.DONT_KILL_APP,
        )
    }

    private fun component(className: String) = ComponentName(applicationContext.packageName, className)
}