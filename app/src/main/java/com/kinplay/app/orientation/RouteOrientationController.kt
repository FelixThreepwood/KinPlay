package com.kinplay.app.orientation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

interface RouteOrientationHost {
    val requestedOrientation: Int
    val isInMultiWindowMode: Boolean
    val isChangingConfigurations: Boolean

    fun writeRequestedOrientation(value: Int)
}

/** Owns one route-scoped orientation request without leaking it into the rest of the app. */
class RouteOrientationController(
    private val host: RouteOrientationHost,
    private val landscapeOrientation: Int,
    restoredOrientation: Int? = null,
) {
    private var savedOrientation: Int? = restoredOrientation

    val hasActiveLease: Boolean
        get() = savedOrientation != null

    val savedOrientationForRecreation: Int?
        get() = savedOrientation

    fun enterLandscape() {
        if (host.isInMultiWindowMode) return
        if (savedOrientation == null) savedOrientation = host.requestedOrientation
        if (host.requestedOrientation != landscapeOrientation) {
            host.writeRequestedOrientation(landscapeOrientation)
        }
    }

    fun restore() {
        val saved = savedOrientation ?: return
        if (host.isInMultiWindowMode || host.isChangingConfigurations) return
        savedOrientation = null
        host.writeRequestedOrientation(saved)
    }
}

private class ActivityRouteOrientationHost(
    private val activity: Activity,
) : RouteOrientationHost {
    override val requestedOrientation: Int
        get() = activity.requestedOrientation

    override val isInMultiWindowMode: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInMultiWindowMode

    override val isChangingConfigurations: Boolean
        get() = activity.isChangingConfigurations

    override fun writeRequestedOrientation(value: Int) {
        activity.requestedOrientation = value
    }
}

/** Keeps the Would You Rather route in landscape while respecting multi-window and recreation. */
@Composable
fun LandscapeWhileVisible(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    LocalConfiguration.current
    val isInMultiWindowMode = activity?.let { host ->
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && host.isInMultiWindowMode
    } ?: true
    var savedOrientation by rememberSaveable { mutableStateOf<Int?>(null) }
    val controller = remember(activity) {
        activity?.let {
            RouteOrientationController(
                host = ActivityRouteOrientationHost(it),
                landscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
                restoredOrientation = savedOrientation,
            )
        }
    }

    if (controller != null && !isInMultiWindowMode) {
        DisposableEffect(controller, isInMultiWindowMode) {
            controller.enterLandscape()
            savedOrientation = controller.savedOrientationForRecreation
            onDispose {
                controller.restore()
                savedOrientation = controller.savedOrientationForRecreation
            }
        }
    }
    content()
}

private fun Context.findActivity(): Activity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return current as? Activity
}
