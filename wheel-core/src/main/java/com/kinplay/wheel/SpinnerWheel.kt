package com.kinplay.wheel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlinx.coroutines.launch

private val SPINNER_COLORS = listOf(
    Color(0xFFEF5350),
    Color(0xFFFFB300),
    Color(0xFF66BB6A),
    Color(0xFF42A5F5),
    Color(0xFFAB47BC),
    Color(0xFFFF7043),
    Color(0xFF26A69A),
    Color(0xFFEC407A),
)

/** Pick a label color that remains readable on every default wheel sector. */
internal fun spinnerLabelColor(background: Color): Color {
    fun linearize(channel: Float): Float = if (channel <= 0.03928f) {
        channel / 12.92f
    } else {
        ((channel + 0.055f) / 1.055f).pow(2.4f)
    }

    val luminance =
        0.2126f * linearize(background.red) +
            0.7152f * linearize(background.green) +
            0.0722f * linearize(background.blue)
    return if (luminance > 0.18f) Color.Black else Color.White
}

/**
 * A reusable, offline spinner wheel with a fixed top pointer and an animated circular turn.
 * Tapping the wheel and pressing Spin use the same action, while Next advances one sector.
 */
@Composable
fun SpinnerWheel(
    options: List<SpinnerWheelOption>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    initialSelectedIndex: Int = 0,
    testTag: String = "spinner-wheel",
    spinButtonTestTag: String = "$testTag-spin-button",
    nextButtonTestTag: String = "$testTag-next-button",
    showNextButton: Boolean = false,
    onSelectionChanged: (SpinnerWheelOption) -> Unit = {},
) {
    if (options.isEmpty()) {
        Text(
            text = "Add at least one choice to spin the wheel.",
            modifier = modifier.testTag("$testTag-empty"),
            color = MaterialTheme.colorScheme.error,
        )
        return
    }

    val optionKey = options.joinToString(separator = "\u0001") { it.id }
    val safeInitialIndex = initialSelectedIndex.coerceIn(0, options.lastIndex)
    var selectedIndex by rememberSaveable(optionKey) { mutableIntStateOf(safeInitialIndex) }
    var rotationDegrees by rememberSaveable(optionKey) {
        mutableFloatStateOf(
            spinnerTargetRotation(
                currentRotation = 0f,
                targetIndex = safeInitialIndex,
                optionCount = options.size,
                minimumTurns = 0,
            ),
        )
    }
    // Busy state belongs to the current composition. Persisting it without the
    // coroutine/animation job can restore a permanently disabled wheel after
    // process or configuration recreation.
    var isSpinning by remember(optionKey) { mutableStateOf(false) }
    val rotation = remember(optionKey) { Animatable(rotationDegrees) }
    val scope = rememberCoroutineScope()
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelMedium.copy(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
    )
    val labelStyles = SPINNER_COLORS.map { color ->
        labelStyle.copy(color = spinnerLabelColor(color))
    }
    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    fun animateTo(targetIndex: Int, minimumTurns: Int, durationMillis: Int) {
        if (!enabled || isSpinning) return
        val targetRotation = spinnerTargetRotation(
            currentRotation = rotation.value,
            targetIndex = targetIndex,
            optionCount = options.size,
            minimumTurns = minimumTurns,
        )
        isSpinning = true
        scope.launch {
            try {
                rotation.animateTo(
                    targetValue = targetRotation,
                    animationSpec = tween(
                        durationMillis = durationMillis,
                        easing = FastOutSlowInEasing,
                    ),
                )
                rotationDegrees = targetRotation
                selectedIndex = targetIndex
                onSelectionChanged(options[targetIndex])
            } finally {
                isSpinning = false
            }
        }
    }

    fun spin() = animateTo(
        targetIndex = chooseSpinnerIndex(options.size, selectedIndex),
        minimumTurns = 5,
        durationMillis = 2_600,
    )

    val selectedOption = options[selectedIndex]
    val wheelState = if (isSpinning) "Spinning" else "Stopped"
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 380.dp)
                .aspectRatio(1f)
                .testTag(testTag)
                .semantics {
                    role = Role.Button
                    contentDescription = "Spinner wheel. Selected ${selectedOption.label}"
                    stateDescription = "$wheelState on ${selectedOption.label}"
                }
                .clickable(
                    enabled = enabled && !isSpinning,
                    role = Role.Button,
                    onClickLabel = "Spin the wheel",
                    onClick = ::spin,
                ),
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                val wheelDiameter = min(size.width, size.height)
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = wheelDiameter / 2f - 14.dp.toPx()
                val sectorDegrees = 360f / options.size
                val dividerStroke = 2.dp.toPx()
                val labelRadius = radius * 0.59f

                withTransform({ rotate(rotation.value, center) }) {
                    options.forEachIndexed { index, option ->
                        val startAngle = -90f + index * sectorDegrees
                        drawArc(
                            color = SPINNER_COLORS[index % SPINNER_COLORS.size],
                            startAngle = startAngle,
                            sweepAngle = sectorDegrees,
                            useCenter = true,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = androidx.compose.ui.geometry.Size(radius * 2f, radius * 2f),
                        )
                        val boundaryAngle = Math.toRadians(startAngle.toDouble())
                        drawLine(
                            color = Color.Black.copy(alpha = 0.24f),
                            start = center,
                            end = Offset(
                                x = center.x + cos(boundaryAngle).toFloat() * radius,
                                y = center.y + sin(boundaryAngle).toFloat() * radius,
                            ),
                            strokeWidth = dividerStroke,
                            cap = StrokeCap.Butt,
                        )

                        val textLayout = textMeasurer.measure(
                            text = AnnotatedString(option.label),
                            style = labelStyles[index % labelStyles.size],
                            constraints = Constraints(maxWidth = (radius * 0.68f).toInt()),
                        )
                        val textAngle = Math.toRadians((startAngle + sectorDegrees / 2f).toDouble())
                        val textCenter = Offset(
                            x = center.x + cos(textAngle).toFloat() * labelRadius,
                            y = center.y + sin(textAngle).toFloat() * labelRadius,
                        )
                        drawText(
                            textLayoutResult = textLayout,
                            topLeft = Offset(
                                x = textCenter.x - textLayout.size.width / 2f,
                                y = textCenter.y - textLayout.size.height / 2f,
                            ),
                        )
                    }
                    drawCircle(
                        color = Color.White.copy(alpha = 0.12f),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 3.dp.toPx()),
                    )
                }

                drawCircle(
                    color = surfaceColor,
                    radius = 42.dp.toPx(),
                    center = center,
                )
                drawCircle(
                    color = primaryColor,
                    radius = 42.dp.toPx(),
                    center = center,
                    style = Stroke(width = 3.dp.toPx()),
                )

                val pointer = Path().apply {
                    val pointerWidth = 15.dp.toPx()
                    val pointerBase = 2.dp.toPx()
                    val pointerTip = 31.dp.toPx()
                    moveTo(center.x - pointerWidth, pointerBase)
                    lineTo(center.x + pointerWidth, pointerBase)
                    lineTo(center.x, pointerTip)
                    close()
                }
                drawPath(pointer, color = tertiaryColor)
            }
            Surface(
                modifier = Modifier.align(Alignment.Center).size(70.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isSpinning) "…" else "GO",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
            }
        }

        Text(
            text = if (isSpinning) "The wheel is spinning…" else "Selected: ${selectedOption.label}",
            modifier = Modifier
                .fillMaxWidth()
                .testTag("$testTag-selected"),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = ::spin,
                enabled = enabled && !isSpinning,
                modifier = Modifier.testTag(spinButtonTestTag),
            ) {
                Text(if (isSpinning) "Spinning…" else "Spin")
            }
            if (showNextButton) {
                OutlinedButton(
                    onClick = {
                        animateTo(
                            targetIndex = nextSpinnerIndex(selectedIndex, options.size),
                            minimumTurns = 1,
                            durationMillis = 900,
                        )
                    },
                    enabled = enabled && !isSpinning,
                    modifier = Modifier.testTag(nextButtonTestTag),
                ) {
                    Text("Next")
                }
            }
        }
    }
}
