package com.kinplay.wheel

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class SpinnerWheelPaletteTest {
    @Test
    fun lightSectorsUseDarkLabelsForReadableContrast() {
        assertEquals(Color.Black, spinnerLabelColor(Color(0xFFFFB300)))
        assertEquals(Color.Black, spinnerLabelColor(Color(0xFF66BB6A)))
        assertEquals(Color.Black, spinnerLabelColor(Color(0xFF42A5F5)))
        assertEquals(Color.White, spinnerLabelColor(Color(0xFFAB47BC)))
    }
}
