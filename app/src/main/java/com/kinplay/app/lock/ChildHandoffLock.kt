package com.kinplay.app.lock

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val ChildHandoffLockStateSaver = Saver<MutableState<ChildHandoffLockState>, Boolean>(
    save = { it.value.isLocked },
    restore = { mutableStateOf(ChildHandoffLockState(isLocked = it)) },
)

/**
 * Blocks controls and back inside KinPlay only. Android system navigation, notifications,
 * power controls, and leaving the app remain controlled by Android and the device owner.
 */
@Composable
fun ChildHandoffLockContainer(
    modifier: Modifier = Modifier,
    content: @Composable (isLocked: Boolean) -> Unit,
) {
    val stateHolder = rememberSaveable(saver = ChildHandoffLockStateSaver) {
        mutableStateOf(ChildHandoffLockState())
    }
    var lockState by stateHolder
    fun updateState(update: (ChildHandoffLockState) -> ChildHandoffLockState) {
        lockState = update(lockState)
    }

    BackHandler(enabled = !lockState.allows(InAppAction.BACK)) {
        // Deliberately consume in-app back while locked.
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (lockState.isLocked) Modifier.clearAndSetSemantics { } else Modifier),
        ) {
            content(lockState.isLocked)
        }
        if (lockState.isLocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.58f))
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent().changes.forEach { it.consume() }
                            }
                        }
                    }
                    .semantics {
                        contentDescription = "Child handoff lock active. KinPlay controls are blocked. Android system controls remain available."
                        stateDescription = "Locked"
                    }
                    .testTag("child-lock-blocker"),
                contentAlignment = Alignment.Center,
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.padding(28.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("CHILD HANDOFF LOCKED", fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                        Text(
                            "In-app controls and Back are blocked. Android system controls are still available.",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
        HoldToToggleLock(
            state = lockState,
            onStateUpdate = ::updateState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp),
        )
    }
}

@Composable
private fun HoldToToggleLock(
    state: ChildHandoffLockState,
    onStateUpdate: ((ChildHandoffLockState) -> ChildHandoffLockState) -> Unit,
    modifier: Modifier = Modifier,
) {
    var frameTimeMillis by remember { mutableLongStateOf(0L) }
    val scope = rememberCoroutineScope()
    val holdStartedAtMillis = state.holdStartedAtMillis

    LaunchedEffect(holdStartedAtMillis) {
        val startedAt = holdStartedAtMillis ?: run {
            frameTimeMillis = 0L
            return@LaunchedEffect
        }
        frameTimeMillis = startedAt
        while (true) {
            val nowMillis = withFrameMillis { it }
            frameTimeMillis = nowMillis
            if (state.shouldToggle(nowMillis)) {
                onStateUpdate { current ->
                    if (current.holdStartedAtMillis == startedAt) current.completeHold(nowMillis) else current
                }
                break
            }
        }
    }

    fun startAccessibleCountdown(): Boolean {
        scope.launch {
            val nowMillis = withFrameMillis { it }
            onStateUpdate { it.beginAccessibleCountdown(nowMillis) }
        }
        return true
    }

    val progress = state.progress(frameTimeMillis)
    val elapsedSeconds = (progress * 3).toInt().coerceIn(0, 3)
    val action = if (state.isLocked) "unlock" else "lock"
    val baseState = if (state.isLocked) "Locked" else "Unlocked"
    val announcedState = if (state.holdStartedAtMillis != null) {
        "$baseState. $action countdown, $elapsedSeconds of 3 seconds"
    } else {
        baseState
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (state.isLocked) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (state.isLocked) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .widthIn(min = 160.dp)
            .heightIn(min = 72.dp)
            .testTag("child-lock-control")
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "Child handoff lock control. Hold for 3 seconds to $action, or activate to start the accessible 3-second countdown."
                stateDescription = announcedState
                progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f)
                liveRegion = LiveRegionMode.Polite
                onClick(label = "Start 3-second countdown to $action child handoff lock") {
                    startAccessibleCountdown()
                }
            }
            .onKeyEvent { event ->
                val activationKey = event.key == Key.Enter ||
                    event.key == Key.NumPadEnter ||
                    event.key == Key.Spacebar
                if (event.type == KeyEventType.KeyUp && activationKey) {
                    startAccessibleCountdown()
                } else {
                    false
                }
            }
            .focusable()
            .pointerInput(state.isLocked) {
                detectTapGestures(
                    onPress = {
                        val nowMillis = withFrameMillis { it }
                        onStateUpdate { it.beginHold(nowMillis) }
                        tryAwaitRelease()
                        onStateUpdate { it.cancelPointerHold() }
                    },
                )
            },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                if (state.isLocked) "LOCKED • Hold 3s to unlock" else "Hold 3s for child handoff",
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                if (state.holdStartedAtMillis != null) "$elapsedSeconds of 3 seconds" else "In-app lock only",
                modifier = Modifier.testTag("child-lock-progress"),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
