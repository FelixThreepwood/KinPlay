package com.kinplay.app.session

import com.kinplay.app.settings.SessionConfiguration

enum class TimedSessionStatus {
    ACTIVE,
    COMPLETE,
}

data class TimedSessionProgress(
    val round: Int,
    val remainingSeconds: Int,
    val status: TimedSessionStatus = TimedSessionStatus.ACTIVE,
) {
    val isActive: Boolean
        get() = status == TimedSessionStatus.ACTIVE

    val isComplete: Boolean
        get() = status == TimedSessionStatus.COMPLETE

    fun tick(configuration: SessionConfiguration): TimedSessionProgress {
        if (isComplete) return this
        if (remainingSeconds > 1) return copy(remainingSeconds = remainingSeconds - 1)
        return completeRound(configuration)
    }

    fun completeRound(configuration: SessionConfiguration): TimedSessionProgress {
        if (round >= configuration.rounds.count) {
            return copy(remainingSeconds = 0, status = TimedSessionStatus.COMPLETE)
        }
        return copy(
            round = round + 1,
            remainingSeconds = configuration.duration.minutes * 60,
            status = TimedSessionStatus.ACTIVE,
        )
    }

    companion object {
        fun initial(configuration: SessionConfiguration): TimedSessionProgress =
            TimedSessionProgress(
                round = 1,
                remainingSeconds = configuration.duration.minutes * 60,
            )
    }
}

fun TimedSessionProgress.remainingTimeLabel(): String {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}