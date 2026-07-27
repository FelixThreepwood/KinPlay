package com.kinplay.app.ui

import com.kinplay.app.settings.AppColorTheme
import org.junit.Assert.assertTrue
import org.junit.Test

class KinPlayPaletteTest {
    @Test
    fun allSupportedThemesKeepTextAndControlsAtAccessibleContrast() {
        AppColorTheme.entries.forEach { theme ->
            val palette = accessiblePalette(theme)
            val pairs = listOf(
                palette.onBackground to palette.background,
                palette.onSurface to palette.surface,
                palette.onSurfaceVariant to palette.surfaceVariant,
                palette.onPrimary to palette.primary,
                palette.onPrimaryContainer to palette.primaryContainer,
                palette.onSecondaryContainer to palette.secondaryContainer,
                palette.onTertiaryContainer to palette.tertiaryContainer,
            )
            pairs.forEach { (foreground, background) ->
                assertTrue(
                    "$theme contrast was ${contrastRatio(foreground, background)}",
                    contrastRatio(foreground, background) >= 4.5,
                )
            }
        }
    }

    @Test
    fun backgroundAndCardSurfacesHaveMeasurableVisualSeparation() {
        AppColorTheme.entries.forEach { theme ->
            val palette = accessiblePalette(theme)
            val backgroundToCardRatio = contrastRatio(palette.background, palette.surface)
            assertTrue(
                "$theme background/card surface ratio was $backgroundToCardRatio",
                backgroundToCardRatio >= 1.25,
            )
        }
    }

    @Test
    fun textUsedBySettingsAndWouldYouRatherRemainsReadableAcrossLayers() {
        AppColorTheme.entries.forEach { theme ->
            val palette = accessiblePalette(theme)
            val crossLayerTextPairs = listOf(
                palette.onSurface to palette.background,
                palette.onSurfaceVariant to palette.background,
                palette.onSurfaceVariant to palette.surface,
                palette.onSurface to palette.surfaceVariant,
                palette.primary to palette.background,
                palette.primary to palette.surface,
            )
            crossLayerTextPairs.forEach { (foreground, background) ->
                val ratio = contrastRatio(foreground, background)
                assertTrue("$theme cross-layer contrast was $ratio", ratio >= 4.5)
            }
        }
    }
}
