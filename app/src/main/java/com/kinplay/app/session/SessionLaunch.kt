package com.kinplay.app.session

import com.kinplay.app.KinPlayItem
import com.kinplay.app.settings.ActivityDuration
import com.kinplay.app.settings.AppSettingsRepository
import com.kinplay.app.settings.SessionConfiguration

/** Immutable input handed from a details page to the timed-session surface. */
data class TimedSession(
    val gameId: String,
    val configuration: SessionConfiguration,
)

/**
 * Timed sessions are currently for active, ordinary pick-a-game activities.
 * Prompt, story, draft, and calm-only content keeps its reading-oriented detail surface.
 */
fun KinPlayItem.isTimedSessionEligible(): Boolean =
    status == "active" && type == "activity" && "pick_a_game" in modes

/** The timer is essential only when the reviewed activity explicitly makes it central to play. */
private val ESSENTIAL_TIMED_SESSION_IDS = setOf(
    "timed_drawing_tiny_monster",
    "rainbow_sort_sprint",
    "cleanup_countdown_game",
    "backyard_micro_safari",
)

fun KinPlayItem.isTimedSessionEssential(): Boolean = id in ESSENTIAL_TIMED_SESSION_IDS

/** Reviewed activity-specific default; per-game overrides still take precedence. */
fun sessionDefaultDuration(item: KinPlayItem): ActivityDuration? =
    if (item.id == "backyard_micro_safari") ActivityDuration.THREE_MINUTES else null

/** Resolve and consume only this game's one-shot override; global settings are not changed. */
fun startTimedSession(
    gameId: String,
    repository: AppSettingsRepository,
    activityDefaultDuration: ActivityDuration? = null,
): TimedSession = TimedSession(
    gameId = gameId.also { require(it.isNotBlank()) { "A game ID is required to start a timed session" } },
    configuration = repository.consumeNextSessionConfiguration(gameId, activityDefaultDuration),
)
