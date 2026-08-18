package com.kinplay.app.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics

import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.role
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onBack: () -> Unit,
) {
    var localSettings by remember(settings) { mutableStateOf(settings) }

    fun updateSettings(changed: AppSettings) {
        localSettings = changed
        onSettingsChange(changed)
    }

    Scaffold(
        modifier = Modifier.testTag("settings-screen"),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                actions = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("top-back-button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                "Choose a simple play plan. Changes save on this device and apply immediately.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PreferenceSection(
                title = "Game timer",
                summary = "Suggested length for a round or turn",
                options = GameTimer.entries,
                selected = localSettings.gameTimer,
                label = GameTimer::label,
                tag = { "setting-timer-${it.wireValue}" },
                onSelect = { updateSettings(localSettings.copy(gameTimer = it)) },
            )
            PreferenceSection(
                title = "Activity duration",
                summary = "Your target length for family activities",
                options = ActivityDuration.entries,
                selected = localSettings.activityDuration,
                label = ActivityDuration::label,
                tag = { "setting-duration-${it.wireValue}" },
                onSelect = { updateSettings(localSettings.copy(activityDuration = it)) },
            )
            PreferenceSection(
                title = "Default rounds",
                summary = "How many rounds a new timed session starts with",
                options = SessionRounds.entries,
                selected = localSettings.defaultRounds,
                label = SessionRounds::label,
                tag = { "setting-rounds-${it.wireValue}" },
                onSelect = { updateSettings(localSettings.copy(defaultRounds = it)) },
            )
            PreferenceSection(
                title = "App color theme",
                summary = "Choose a theme by name",
                options = AppColorTheme.entries,
                selected = localSettings.colorTheme,
                label = AppColorTheme::label,
                tag = { "setting-theme-${it.wireValue}" },
                gridColumns = 3,
                onSelect = { updateSettings(localSettings.copy(colorTheme = it)) },
            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Current plan: ${localSettings.defaultRounds.label} • ${localSettings.gameTimer.label} per turn • ${localSettings.activityDuration.label} activities • ${localSettings.colorTheme.label} theme",
                    modifier = Modifier.padding(16.dp).testTag("settings-current-plan"),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun <T> PreferenceSection(
    title: String,
    summary: String,
    options: List<T>,
    selected: T,
    label: (T) -> String,
    tag: (T) -> String,
    verticalOptions: Boolean = false,
    gridColumns: Int? = null,
    onSelect: (T) -> Unit,
) {
    val choices = options.map { option ->
        PreferenceChoice(option, label(option), tag(option))
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp).selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(title, modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold)
            Text(
                summary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                if (gridColumns != null) {
                    choices.chunked(gridColumns).forEach { rowChoices ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            rowChoices.forEach { choice ->
                                key(choice.tag) {
                                    PreferenceOption(
                                        choice = choice,
                                        compact = true,
                                        selected = selected,
                                        onSelect = onSelect,
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                            repeat((gridColumns - rowChoices.size).coerceAtLeast(0)) {
                                androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    val useHorizontalOptions = !verticalOptions && maxWidth >= 360.dp && LocalDensity.current.fontScale < 1.5f
                    if (useHorizontalOptions) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        choices.forEach { choice ->
                            key(choice.tag) {
                                PreferenceOption(choice, true, selected, onSelect)
                            }
                        }
                    }
                    } else {
                        Column {
                            choices.forEach { choice ->
                                key(choice.tag) {
                                    PreferenceOption(choice, false, selected, onSelect)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class PreferenceChoice<T>(
    val option: T,
    val label: String,
    val tag: String,
)

@Composable
private fun <T> PreferenceOption(
    choice: PreferenceChoice<T>,
    compact: Boolean,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val option = remember(choice.tag) { choice.option }
    val latestOnSelect by rememberUpdatedState(onSelect)
    Row(
        modifier = modifier.then(if (compact) Modifier else Modifier.fillMaxWidth())
            .clickable { latestOnSelect(option) }
            .semantics {
                role = Role.RadioButton
                this.selected = option == selected
            }
            .testTag(choice.tag)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = option == selected,
            onClick = null,
        )
        Text(choice.label, modifier = Modifier.padding(start = 6.dp), maxLines = 1)
    }
}
