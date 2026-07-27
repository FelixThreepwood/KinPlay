package com.kinplay.app.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onLauncherIconChange: (LauncherIconVariant) -> LauncherIconSwitchResult,
    onBack: () -> Unit,
) {
    var launcherIconMessage by remember { mutableStateOf<String?>(null) }
    Scaffold(
        modifier = Modifier.testTag("settings-screen"),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Text(
                        text = "‹ Back",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(16.dp),
                    )
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
                selected = settings.gameTimer,
                label = GameTimer::label,
                tag = { "setting-timer-${it.wireValue}" },
                onSelect = { onSettingsChange(settings.copy(gameTimer = it)) },
            )
            PreferenceSection(
                title = "Activity duration",
                summary = "Your target length for family activities",
                options = ActivityDuration.entries,
                selected = settings.activityDuration,
                label = ActivityDuration::label,
                tag = { "setting-duration-${it.wireValue}" },
                onSelect = { onSettingsChange(settings.copy(activityDuration = it)) },
            )
            PreferenceSection(
                title = "App color theme",
                summary = "Accessible native colors for backgrounds, cards, and controls",
                options = AppColorTheme.entries,
                selected = settings.colorTheme,
                label = { "${it.label} — ${it.description}" },
                tag = { "setting-theme-${it.wireValue}" },
                onSelect = { onSettingsChange(settings.copy(colorTheme = it)) },
            )
            PreferenceSection(
                title = "Launcher icon",
                summary = "Choose Teal or Sunshine. Home-screen refresh timing depends on your launcher.",
                options = LauncherIconVariant.entries,
                selected = settings.launcherIcon,
                label = { "${it.label} — ${it.description}" },
                tag = { "setting-launcher-icon-${it.wireValue}" },
                onSelect = { launcherIcon ->
                    launcherIconMessage = when (onLauncherIconChange(launcherIcon)) {
                        LauncherIconSwitchResult.APPLIED ->
                            "${launcherIcon.label} selected. Your launcher may take time to refresh."
                        LauncherIconSwitchResult.ALREADY_APPLIED ->
                            "${launcherIcon.label} is already selected."
                        LauncherIconSwitchResult.FAILED_SAFE ->
                            "The launcher icon could not be changed. Your previous icon remains available."
                    }
                },
            )
            launcherIconMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("launcher-icon-status"),
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Current plan: ${settings.gameTimer.label} rounds • ${settings.activityDuration.label} activities • ${settings.colorTheme.label} theme",
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
    onSelect: (T) -> Unit,
) {
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
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag(option))
                        .selectable(
                            selected = option == selected,
                            role = Role.RadioButton,
                            onClick = { onSelect(option) },
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = option == selected, onClick = null)
                    Text(label(option), modifier = Modifier.padding(start = 6.dp))
                }
            }
        }
    }
}
