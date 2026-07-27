package com.kinplay.app.lock

const val CHILD_HANDOFF_HOLD_MILLIS = 3_000L

enum class InAppAction {
    BACK,
    EXIT,
    GAME_CONTROL,
    LOCK_CONTROL,
}

enum class ChildHandoffActivation {
    POINTER_HOLD,
    ACCESSIBLE_COUNTDOWN,
}

data class ChildHandoffLockState(
    val isLocked: Boolean = false,
    val holdStartedAtMillis: Long? = null,
    val activation: ChildHandoffActivation? = null,
) {
    fun beginHold(
        nowMillis: Long,
        activation: ChildHandoffActivation = ChildHandoffActivation.POINTER_HOLD,
    ): ChildHandoffLockState = if (holdStartedAtMillis == null) {
        copy(holdStartedAtMillis = nowMillis, activation = activation)
    } else {
        this
    }

    fun beginAccessibleCountdown(nowMillis: Long): ChildHandoffLockState =
        beginHold(nowMillis, ChildHandoffActivation.ACCESSIBLE_COUNTDOWN)

    fun cancelHold(): ChildHandoffLockState = copy(holdStartedAtMillis = null, activation = null)

    fun cancelPointerHold(): ChildHandoffLockState =
        if (activation == ChildHandoffActivation.POINTER_HOLD) cancelHold() else this

    fun progress(nowMillis: Long): Float {
        val started = holdStartedAtMillis ?: return 0f
        return ((nowMillis - started).coerceAtLeast(0L).toFloat() / CHILD_HANDOFF_HOLD_MILLIS).coerceIn(0f, 1f)
    }

    fun shouldToggle(nowMillis: Long): Boolean = progress(nowMillis) >= 1f

    fun completeHold(nowMillis: Long): ChildHandoffLockState =
        if (shouldToggle(nowMillis)) {
            copy(isLocked = !isLocked, holdStartedAtMillis = null, activation = null)
        } else {
            cancelHold()
        }

    fun allows(action: InAppAction): Boolean = !isLocked || action == InAppAction.LOCK_CONTROL
}
