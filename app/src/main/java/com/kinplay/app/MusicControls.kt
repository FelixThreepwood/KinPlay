package com.kinplay.app

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun BundledMusicControls(itemId: String, enabled: Boolean) {
    if (!supportsBundledMusic(itemId)) return

    val context = LocalContext.current
    var selectedTrackId by rememberSaveable(itemId) { mutableStateOf(KINPLAY_MUSIC_TRACKS.first().id) }
    var automaticPlayback by rememberSaveable(itemId) { mutableStateOf(false) }
    var isPlaying by rememberSaveable(itemId) { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var player by remember(itemId) { mutableStateOf<MediaPlayer?>(null) }
    val selectedTrack = KINPLAY_MUSIC_TRACKS.first { it.id == selectedTrackId }

    fun releasePlayer() {
        player?.runCatching { stop() }
        player?.release()
        player = null
        isPlaying = false
    }

    fun startSelectedTrack() {
        releasePlayer()
        player = MediaPlayer.create(context, selectedTrack.resourceId)?.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build(),
            )
            isLooping = automaticPlayback
            setOnCompletionListener { isPlaying = false }
            start()
        }
        isPlaying = player != null
    }

    DisposableEffect(itemId) {
        onDispose { releasePlayer() }
    }

    LaunchedEffect(selectedTrackId, automaticPlayback, enabled) {
        if (automaticPlayback && enabled) startSelectedTrack() else releasePlayer()
    }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("music-controls-$itemId"),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Music for movement games", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text("Choose a bundled instrumental loop. The parent controls playback; automatic playback repeats the selected track.")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { menuExpanded = true },
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth().semantics {
                            contentDescription = "Choose music track; selected ${selectedTrack.title}"
                            role = Role.Button
                        },
                    ) { Text(selectedTrack.title) }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        KINPLAY_MUSIC_TRACKS.forEach { track ->
                            DropdownMenuItem(
                                text = { Text(track.title) },
                                onClick = {
                                    selectedTrackId = track.id
                                    menuExpanded = false
                                },
                            )
                        }
                    }
                }
                Button(
                    onClick = { if (isPlaying) releasePlayer() else startSelectedTrack() },
                    enabled = enabled,
                    modifier = Modifier.testTag("music-play-pause"),
                ) { Text(if (isPlaying) "Pause" else "Play") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(
                    checked = automaticPlayback,
                    onCheckedChange = { automaticPlayback = it },
                    enabled = enabled,
                    modifier = Modifier.testTag("music-automatic-switch").semantics {
                        contentDescription = "Automatic music playback"
                    },
                )
                Column {
                    Text("Automatic playback")
                    Text("Loop this track until the parent pauses it.", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
