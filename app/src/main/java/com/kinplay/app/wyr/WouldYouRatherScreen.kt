package com.kinplay.app.wyr

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinplay.app.lock.ChildHandoffLockContainer
import com.kinplay.app.orientation.LandscapeWhileVisible
import com.kinplay.app.settings.GameTimer

private const val STATE_PREFERENCES = "would_you_rather_progress"
private const val STATE_KEY = "deck_state_v1"
private const val SEED_KEY = "deck_seed_v1"
const val WOULD_YOU_RATHER_PROMPT_FADE_MILLIS = 450


/** Android persistence boundary for the pure shuffled-bag store. */
class SharedPreferencesWouldYouRatherStateStorage(context: Context) : WouldYouRatherStateStorage {
    private val preferences = context.getSharedPreferences(STATE_PREFERENCES, Context.MODE_PRIVATE)

    override fun read(): String? = preferences.getString(STATE_KEY, null)

    override fun write(value: String) {
        preferences.edit().putString(STATE_KEY, value).apply()
    }

    fun installationSeed(): Long {
        if (preferences.contains(SEED_KEY)) return preferences.getLong(SEED_KEY, 0L)
        val seed = System.currentTimeMillis() xor System.nanoTime()
        preferences.edit().putLong(SEED_KEY, seed).apply()
        return seed
    }
}

/** Loads the checked-in reviewed library and owns the process-local session. */
@Composable
fun WouldYouRatherRoute(
    gameTimer: GameTimer = GameTimer.ONE_MINUTE,
    showChildHandoffLock: Boolean = true,
    onExit: () -> Unit,
) {
    LandscapeWhileVisible {
            val context = LocalContext.current.applicationContext
            val loaded = remember(context) {
                runCatching {
                    val json = context.assets.open("would_you_rather_v1.json").bufferedReader().use { it.readText() }
                    val library = WouldYouRatherLibraryParser.parse(json)
                    val storage = SharedPreferencesWouldYouRatherStateStorage(context)
                    library to WouldYouRatherSession(
                        library = library,
                        store = WouldYouRatherStore(storage),
                        seed = storage.installationSeed(),
                    )
                }
            }
            val loadedSession = loaded.getOrNull()
            if (loadedSession == null) {
                WouldYouRatherLoadError(onExit)
            } else {
                val (_, session) = loadedSession
                var selectedCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
                var promptId by rememberSaveable { mutableStateOf<String?>(null) }
                val selectedCategory = session.categories.firstOrNull { it.id == selectedCategoryId }
                val prompt = selectedCategory?.prompts?.firstOrNull { it.id == promptId }

                WouldYouRatherPlayScreen(
                    categories = session.categories,
                    selectedCategory = selectedCategory,
                    prompt = prompt,
                    onSelectCategory = { category ->
                        selectedCategoryId = category.id
                        promptId = session.nextPrompt(category.id).id
                    },
                    onAdvance = {
                        selectedCategory?.let { category ->
                            promptId = session.nextPrompt(category.id).id
                        }
                    },
                    gameTimer = gameTimer,
                    showChildHandoffLock = showChildHandoffLock,
                    onExit = onExit,
                )
            }
        }
    }

/**
 * Distraction-minimized full-screen play surface. It is stateless so the exact
 * category and one-tap prompt behavior can be exercised by Compose UI tests.
 */
@Composable
fun WouldYouRatherPlayScreen(
    categories: List<WouldYouRatherCategory>,
    selectedCategory: WouldYouRatherCategory?,
    prompt: WouldYouRatherPrompt?,
    onSelectCategory: (WouldYouRatherCategory) -> Unit,
    onAdvance: () -> Unit,
    onExit: () -> Unit,
    gameTimer: GameTimer = GameTimer.ONE_MINUTE,
    showChildHandoffLock: Boolean = true,
) {
    if (showChildHandoffLock) {
        ChildHandoffLockContainer { isLocked ->
            WouldYouRatherSurface(
                categories = categories,
                selectedCategory = selectedCategory,
                prompt = prompt,
                onSelectCategory = onSelectCategory,
                onAdvance = onAdvance,
                onExit = onExit,
                gameTimer = gameTimer,
                isLocked = isLocked,
            )
        }
    } else {
        WouldYouRatherSurface(
            categories = categories,
            selectedCategory = selectedCategory,
            prompt = prompt,
            onSelectCategory = onSelectCategory,
            onAdvance = onAdvance,
            onExit = onExit,
            gameTimer = gameTimer,
            isLocked = false,
        )
    }
}

@Composable
private fun WouldYouRatherSurface(
    categories: List<WouldYouRatherCategory>,
    selectedCategory: WouldYouRatherCategory?,
    prompt: WouldYouRatherPrompt?,
    onSelectCategory: (WouldYouRatherCategory) -> Unit,
    onAdvance: () -> Unit,
    onExit: () -> Unit,
    gameTimer: GameTimer,
    isLocked: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        if (selectedCategory == null || prompt == null) {
            CategoryChoices(
                categories = categories,
                onSelectCategory = { if (!isLocked) onSelectCategory(it) },
            )
        } else {
            PromptSurface(
                category = selectedCategory,
                prompt = prompt,
                gameTimer = gameTimer,
                onAdvance = { if (!isLocked) onAdvance() },
            )
        }

        OutlinedButton(
            onClick = onExit,
            enabled = !isLocked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
        ) {
            Text("Exit", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CategoryChoices(
    categories: List<WouldYouRatherCategory>,
    onSelectCategory: (WouldYouRatherCategory) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 76.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Would You Rather",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 34.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Pick a category",
            modifier = Modifier.padding(top = 8.dp, bottom = 28.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp,
        )
        categories.sortedBy { it.order }.chunked(2).forEachIndexed { rowIndex, rowCategories ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                rowCategories.forEachIndexed { columnIndex, category ->
                    val layer = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer,
                        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer,
                        MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant,
                        MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer,
                    )[rowIndex * 2 + columnIndex]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = layer.first, contentColor = layer.second),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("wyr-category-choice")
                            .clickable { onSelectCategory(category) },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 30.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = category.title,
                                color = layer.second,
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PromptSurface(
    category: WouldYouRatherCategory,
    prompt: WouldYouRatherPrompt,
    gameTimer: GameTimer,
    onAdvance: () -> Unit,
) {
    var promptVisible by remember(prompt.id) { mutableStateOf(false) }
    LaunchedEffect(prompt.id) { promptVisible = true }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = category.title,
            modifier = Modifier.padding(top = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        AnimatedVisibility(
            visible = promptVisible,
            enter = fadeIn(animationSpec = tween(WOULD_YOU_RATHER_PROMPT_FADE_MILLIS)),
            modifier = Modifier.weight(1f),
        ) {
            AnimatedContent(
                targetState = prompt,
                transitionSpec = {
                    fadeIn(animationSpec = tween(WOULD_YOU_RATHER_PROMPT_FADE_MILLIS)) togetherWith
                        fadeOut(animationSpec = tween(WOULD_YOU_RATHER_PROMPT_FADE_MILLIS))
                },
                label = "Would You Rather prompt fade",
            ) { visiblePrompt ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("wyr-prompt")
                        .clickable(onClick = onAdvance)
                        .padding(horizontal = 28.dp, vertical = 36.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = visiblePrompt.text,
                        modifier = Modifier.testTag("wyr-prompt-text"),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 30.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Text(
            text = "Tap anywhere for the next one • ${gameTimer.label} suggested rounds",
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 104.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun WouldYouRatherLoadError(onExit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Would You Rather could not be loaded.",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        OutlinedButton(onClick = onExit, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}
