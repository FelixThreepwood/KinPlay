package com.kinplay.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kinplay.app.settings.AppColorTheme
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

data class AccessiblePalette(
    val background: Int,
    val onBackground: Int,
    val surface: Int,
    val onSurface: Int,
    val surfaceVariant: Int,
    val onSurfaceVariant: Int,
    val primary: Int,
    val onPrimary: Int,
    val primaryContainer: Int,
    val onPrimaryContainer: Int,
    val secondaryContainer: Int,
    val onSecondaryContainer: Int,
    val tertiaryContainer: Int,
    val onTertiaryContainer: Int,
)

private fun argb(value: Long): Int = value.toInt()

fun accessiblePalette(theme: AppColorTheme): AccessiblePalette = when (theme) {
    AppColorTheme.FOREST -> AccessiblePalette(
        background = argb(0xFFD8E4D8), onBackground = argb(0xFF18251D),
        surface = argb(0xFFFFFBF3), onSurface = argb(0xFF18251D),
        surfaceVariant = argb(0xFFDCE8DD), onSurfaceVariant = argb(0xFF2F4034),
        primary = argb(0xFF24583E), onPrimary = argb(0xFFFFFFFF),
        primaryContainer = argb(0xFFC5EBD1), onPrimaryContainer = argb(0xFF0C2D1E),
        secondaryContainer = argb(0xFFFFE19A), onSecondaryContainer = argb(0xFF2A2000),
        tertiaryContainer = argb(0xFFF4D8E5), onTertiaryContainer = argb(0xFF321523),
    )
    AppColorTheme.OCEAN -> AccessiblePalette(
        background = argb(0xFFD2E4EA), onBackground = argb(0xFF10272E),
        surface = argb(0xFFF9FDFE), onSurface = argb(0xFF10272E),
        surfaceVariant = argb(0xFFD7E7EC), onSurfaceVariant = argb(0xFF2C4148),
        primary = argb(0xFF075B70), onPrimary = argb(0xFFFFFFFF),
        primaryContainer = argb(0xFFB9EAF5), onPrimaryContainer = argb(0xFF002D38),
        secondaryContainer = argb(0xFFFFE09B), onSecondaryContainer = argb(0xFF2A2000),
        tertiaryContainer = argb(0xFFDADDFC), onTertiaryContainer = argb(0xFF161947),
    )
    AppColorTheme.BERRY -> AccessiblePalette(
        background = argb(0xFFE9D9E0), onBackground = argb(0xFF2C1922),
        surface = argb(0xFFFFF8FB), onSurface = argb(0xFF2C1922),
        surfaceVariant = argb(0xFFEDDDE4), onSurfaceVariant = argb(0xFF49333D),
        primary = argb(0xFF7A3055), onPrimary = argb(0xFFFFFFFF),
        primaryContainer = argb(0xFFFFD8E8), onPrimaryContainer = argb(0xFF351022),
        secondaryContainer = argb(0xFFF6E09C), onSecondaryContainer = argb(0xFF261D00),
        tertiaryContainer = argb(0xFFD5EAD7), onTertiaryContainer = argb(0xFF112C18),
    )
    AppColorTheme.SUNSHINE -> AccessiblePalette(
        background = argb(0xFFFFE082), onBackground = argb(0xFF3A2800),
        surface = argb(0xFFFFFBF2), onSurface = argb(0xFF2E2100),
        surfaceVariant = argb(0xFFFFD77A), onSurfaceVariant = argb(0xFF3A2800),
        primary = argb(0xFF9A4D00), onPrimary = argb(0xFFFFFFFF),
        primaryContainer = argb(0xFFFFC56B), onPrimaryContainer = argb(0xFF341500),
        secondaryContainer = argb(0xFFBFE7FF), onSecondaryContainer = argb(0xFF00233A),
        tertiaryContainer = argb(0xFFFFC4E1), onTertiaryContainer = argb(0xFF3D0822),
    )
    AppColorTheme.TROPICAL -> AccessiblePalette(
        background = argb(0xFFAEEED8), onBackground = argb(0xFF002B24),
        surface = argb(0xFFF7FFFB), onSurface = argb(0xFF00251F),
        surfaceVariant = argb(0xFF7FE3C3), onSurfaceVariant = argb(0xFF00362C),
        primary = argb(0xFF006B5B), onPrimary = argb(0xFFFFFFFF),
        primaryContainer = argb(0xFF64E4C3), onPrimaryContainer = argb(0xFF002018),
        secondaryContainer = argb(0xFFFFB5C8), onSecondaryContainer = argb(0xFF420019),
        tertiaryContainer = argb(0xFFFFD18A), onTertiaryContainer = argb(0xFF382000),
    )
}

@Composable
fun KinPlayTheme(theme: AppColorTheme, content: @Composable () -> Unit) {
    val palette = accessiblePalette(theme)
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(palette.primary),
            onPrimary = Color(palette.onPrimary),
            primaryContainer = Color(palette.primaryContainer),
            onPrimaryContainer = Color(palette.onPrimaryContainer),
            secondary = Color(palette.primary),
            onSecondary = Color(palette.onPrimary),
            secondaryContainer = Color(palette.secondaryContainer),
            onSecondaryContainer = Color(palette.onSecondaryContainer),
            tertiary = Color(palette.primary),
            onTertiary = Color(palette.onPrimary),
            tertiaryContainer = Color(palette.tertiaryContainer),
            onTertiaryContainer = Color(palette.onTertiaryContainer),
            background = Color(palette.background),
            onBackground = Color(palette.onBackground),
            surface = Color(palette.surface),
            onSurface = Color(palette.onSurface),
            surfaceVariant = Color(palette.surfaceVariant),
            onSurfaceVariant = Color(palette.onSurfaceVariant),
            outline = Color(palette.onSurfaceVariant),
        ),
        content = content,
    )
}

fun contrastRatio(first: Int, second: Int): Double {
    fun luminance(color: Int): Double {
        fun channel(shift: Int): Double {
            val value = ((color ushr shift) and 0xFF) / 255.0
            return if (value <= 0.04045) value / 12.92 else ((value + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * channel(16) + 0.7152 * channel(8) + 0.0722 * channel(0)
    }
    val firstLuminance = luminance(first)
    val secondLuminance = luminance(second)
    return (max(firstLuminance, secondLuminance) + 0.05) / (min(firstLuminance, secondLuminance) + 0.05)
}
