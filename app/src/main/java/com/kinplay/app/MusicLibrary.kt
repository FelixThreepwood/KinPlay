package com.kinplay.app

import androidx.annotation.RawRes

/** Offline, reviewed instrumental loops bundled with the APK. */
data class MusicTrack(
    val id: String,
    val title: String,
    val description: String,
    @RawRes val resourceId: Int,
)

val KINPLAY_MUSIC_TRACKS = listOf(
    MusicTrack("sunshine_steps", "Sunshine Steps", "Bright and bouncy for dancing", R.raw.kinplay_music_sunshine),
    MusicTrack("bouncy_balloon", "Bouncy Balloon", "Playful rhythm for silly movement", R.raw.kinplay_music_bouncy),
    MusicTrack("twinkle_trail", "Twinkle Trail", "Light melody for gentle games", R.raw.kinplay_music_twinkle),
    MusicTrack("silly_sneak", "Silly Sneak", "A quiet groove for freeze-and-pose play", R.raw.kinplay_music_sneaky),
    MusicTrack("jungle_jamboree", "Jungle Jamboree", "Animal-movement adventure music", R.raw.kinplay_music_jungle),
    MusicTrack("dreamy_drift", "Dreamy Drift", "Soft instrumental reset", R.raw.kinplay_music_dreamy),
)

private val MUSIC_ACTIVITY_IDS = setOf("freeze_dance_statues", "kitchen_band_rehearsal")

fun supportsBundledMusic(itemId: String): Boolean = itemId in MUSIC_ACTIVITY_IDS
