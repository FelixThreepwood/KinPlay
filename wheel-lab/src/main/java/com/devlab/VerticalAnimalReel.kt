package com.devlab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
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
import com.kinplay.wheel.SpinnerWheelOption
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random
import kotlinx.coroutines.launch

private val ANIMAL_REEL_COLORS = listOf(
    Color(0xFF174A7E),
    Color(0xFF7A2147),
    Color(0xFF126B64),
    Color(0xFF5B3A91),
    Color(0xFF8A4B16),
    Color(0xFF245C3C),
    Color(0xFF7B2D69),
    Color(0xFF1D5F72),
)

/**
 * A lab-only vertical drum reel. Lower reel positions move the rendered rows
 * from the top of the viewport toward the bottom, behind a fixed center pointer.
 */
@Composable
fun VerticalAnimalReel(
    catalog: List<SpinnerWheelOption>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    initialPosition: Long = 0L,
    testTag: String = "animal-reel",
    spinButtonTestTag: String = "$testTag-spin-button",
    onSelectionChanged: (SpinnerWheelOption) -> Unit = {},
) {
    if (catalog.isEmpty()) {
        Text(
            text = "Add at least one animal to roll the reel.",
            modifier = modifier.testTag("$testTag-empty"),
            color = MaterialTheme.colorScheme.error,
        )
        return
    }

    val catalogKey = catalog.joinToString(separator = "\u0001") { it.id }
    var settledPosition by rememberSaveable(catalogKey) {
        mutableLongStateOf(initialPosition)
    }
    var reelSeed by rememberSaveable(catalogKey) {
        mutableLongStateOf(Random.Default.nextLong())
    }
    var isRolling by remember(catalogKey) { mutableStateOf(false) }
    val reelPosition = remember(catalogKey) { Animatable(settledPosition.toFloat()) }
    val scope = rememberCoroutineScope()
    val random = remember(catalogKey) { Random.Default }
    val textMeasurer = rememberTextMeasurer()
    val rowTextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
    )
    val settledAnimal = animalAtReelPosition(catalog, settledPosition, reelSeed)

    LaunchedEffect(catalogKey) {
        onSelectionChanged(settledAnimal)
    }

    fun spin() {
        if (!enabled || isRolling) return

        val targetPosition = chooseAnimalReelTarget(
            currentPosition = settledPosition,
            random = random,
        )
        val targetAnimal = animalAtReelPosition(catalog, targetPosition, reelSeed)
        isRolling = true
        scope.launch {
            try {
                reelPosition.animateTo(
                    targetValue = targetPosition.toFloat(),
                    animationSpec = tween(
                        durationMillis = 3_200,
                        easing = FastOutSlowInEasing,
                    ),
                )
                settledPosition = targetPosition
                onSelectionChanged(targetAnimal)
            } finally {
                isRolling = false
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .testTag(testTag)
                .semantics {
                    role = Role.Button
                    contentDescription = "Animal reel. Selected ${settledAnimal.label}"
                    stateDescription = if (isRolling) {
                        "Rolling downward"
                    } else {
                        "Stopped on ${settledAnimal.label}"
                    }
                }
                .clickable(
                    enabled = enabled && !isRolling,
                    role = Role.Button,
                    onClickLabel = "Roll the animal reel",
                    onClick = ::spin,
                ),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val frame = 10.dp.toPx()
                val drumLeft = 16.dp.toPx()
                val drumTop = 12.dp.toPx()
                val drumRight = size.width - 16.dp.toPx()
                val drumBottom = size.height - 12.dp.toPx()
                val drumWidth = drumRight - drumLeft
                val centerY = (drumTop + drumBottom) / 2f
                val baseRowHeight = 68.dp.toPx()
                val visibleDistance = 5.4f
                val currentPosition = reelPosition.value

                drawRoundRect(
                    color = Color(0xFF0C1628),
                    topLeft = Offset.Zero,
                    size = size,
                    cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx()),
                )
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF233D5B), Color(0xFF0A1322)),
                        startY = drumTop,
                        endY = drumBottom,
                    ),
                    topLeft = Offset(drumLeft, drumTop),
                    size = Size(drumWidth, drumBottom - drumTop),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx()),
                )

                clipRect(
                    left = drumLeft,
                    top = drumTop,
                    right = drumRight,
                    bottom = drumBottom,
                ) {
                    val firstPosition = floor(currentPosition).toLong() - 6L
                    val lastPosition = floor(currentPosition).toLong() + 6L
                    for (reelIndex in firstPosition..lastPosition) {
                        val distanceRows = reelIndex - currentPosition
                        if (abs(distanceRows) > visibleDistance) continue

                        val edgeFraction = (abs(distanceRows) / visibleDistance).coerceIn(0f, 1f)
                        val perspective = 1f - 0.54f * edgeFraction.pow(1.18f)
                        val rowHeight = baseRowHeight * perspective
                        val rowWidth = drumWidth * (1f - 0.12f * edgeFraction)
                        val rowX = (size.width - rowWidth) / 2f
                        val rowY = centerY + distanceRows * baseRowHeight - rowHeight / 2f
                        val rowAlpha = 1f - 0.52f * edgeFraction
                        val rowColor = ANIMAL_REEL_COLORS[positiveIndex(reelIndex, ANIMAL_REEL_COLORS.size)]
                        val animal = animalAtReelPosition(catalog, reelIndex, reelSeed)
                        val labelColor = reelLabelColor(rowColor).copy(alpha = rowAlpha)

                        drawRoundRect(
                            color = rowColor.copy(alpha = rowAlpha),
                            topLeft = Offset(rowX, rowY),
                            size = Size(rowWidth, rowHeight),
                            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                        )
                        drawLine(
                            color = Color.White.copy(alpha = 0.20f * rowAlpha),
                            start = Offset(rowX + 8.dp.toPx(), rowY + 3.dp.toPx()),
                            end = Offset(rowX + rowWidth - 8.dp.toPx(), rowY + 3.dp.toPx()),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round,
                        )

                        val textLayout = textMeasurer.measure(
                            text = AnnotatedString(animal.label),
                            style = rowTextStyle.copy(color = labelColor),
                            constraints = Constraints(maxWidth = (rowWidth * 0.76f).toInt()),
                        )
                        drawText(
                            textLayoutResult = textLayout,
                            topLeft = Offset(
                                x = size.width / 2f - textLayout.size.width / 2f,
                                y = centerY + distanceRows * baseRowHeight - textLayout.size.height / 2f,
                            ),
                        )
                    }
                }

                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0C1628).copy(alpha = 0.92f),
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF0C1628).copy(alpha = 0.92f),
                        ),
                        startY = drumTop,
                        endY = drumBottom,
                    ),
                    topLeft = Offset(drumLeft, drumTop),
                    size = Size(drumWidth, drumBottom - drumTop),
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0C1628).copy(alpha = 0.72f),
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF0C1628).copy(alpha = 0.72f),
                        ),
                        startX = drumLeft,
                        endX = drumRight,
                    ),
                    topLeft = Offset(drumLeft, drumTop),
                    size = Size(drumWidth, drumBottom - drumTop),
                )

                val selectionHeight = baseRowHeight * 1.04f
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.10f),
                    topLeft = Offset(drumLeft + 6.dp.toPx(), centerY - selectionHeight / 2f),
                    size = Size(drumWidth - 12.dp.toPx(), selectionHeight),
                    cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx()),
                )
                drawRoundRect(
                    color = Color(0xFFFFD54F),
                    topLeft = Offset(drumLeft + 6.dp.toPx(), centerY - selectionHeight / 2f),
                    size = Size(drumWidth - 12.dp.toPx(), selectionHeight),
                    cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx()),
                    style = Stroke(width = 3.dp.toPx()),
                )

                val pointer = Path().apply {
                    moveTo(size.width - 8.dp.toPx(), centerY - 19.dp.toPx())
                    lineTo(size.width - 8.dp.toPx(), centerY + 19.dp.toPx())
                    lineTo(size.width - 36.dp.toPx(), centerY)
                    close()
                }
                drawPath(pointer, color = Color(0xFFFFC107))
                drawRoundRect(
                    color = Color(0xFF6E7F93),
                    topLeft = Offset(frame, frame),
                    size = Size(size.width - frame * 2f, size.height - frame * 2f),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                    style = Stroke(width = 3.dp.toPx()),
                )
            }
        }

        Text(
            text = if (isRolling) {
                "Rolling downward…"
            } else {
                "Stopped on: ${settledAnimal.label}"
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("$testTag-selected"),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        Button(
            onClick = ::spin,
            enabled = enabled && !isRolling,
            modifier = Modifier.testTag(spinButtonTestTag),
        ) {
            Text(if (isRolling) "Rolling…" else "Spin")
        }
    }
}

private fun positiveIndex(value: Long, size: Int): Int {
    val remainder = value % size.toLong()
    return if (remainder < 0L) (remainder + size).toInt() else remainder.toInt()
}

private fun reelLabelColor(background: Color): Color {
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
