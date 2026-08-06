package com.kinplay.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kinplay.app.feedback.FeedbackCaptureContext
import com.kinplay.app.feedback.FeedbackOverlay
import com.kinplay.app.lock.ChildHandoffLockContainer
import com.kinplay.app.settings.AndroidLauncherIconGateway
import com.kinplay.app.settings.ActivityDuration
import com.kinplay.app.settings.AppSettings
import com.kinplay.app.settings.AppSettingsRepository
import com.kinplay.app.settings.LauncherIconSwitchResult
import com.kinplay.app.settings.LauncherIconSwitcher
import com.kinplay.app.settings.SessionConfigurationOverride
import com.kinplay.app.settings.SessionConfiguration
import com.kinplay.app.settings.SessionRounds
import com.kinplay.app.settings.SettingsScreen
import com.kinplay.app.settings.SharedPreferencesSettingsKeyValueStore
import com.kinplay.app.settings.resolveNextSessionConfiguration
import com.kinplay.app.settings.sessionDefaults
import com.kinplay.app.session.TimedSession
import com.kinplay.app.session.TimedSessionProgress
import com.kinplay.app.session.TimedSessionStatus
import com.kinplay.app.session.activeSessionSections
import com.kinplay.app.session.isTimedSessionEligible
import com.kinplay.app.session.remainingTimeLabel
import com.kinplay.app.session.startTimedSession
import com.kinplay.app.ui.KinPlayTheme
import com.kinplay.app.wyr.WouldYouRatherRoute
import kotlinx.coroutines.delay
import org.json.JSONObject
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KinPlayApp() }
    }
}

const val CONTENT_CARD_DEFAULT_EXPANDED = false
const val HOME_DESCRIPTOR = "Family play"
const val HOME_INSTRUCTION_SECTION_ENABLED = false
const val RANDOM_GAME_LABEL = "Random game"
const val ALL_GAMES_AND_ACTIVITIES_LABEL = "All games and activities"
const val MAD_LIBS_COLLECTION_ID = "mad_libs_collection"
const val WOULD_YOU_RATHER_ITEM_ID = "would_you_rather_silly_family"
const val WOULD_YOU_RATHER_ROUTE = "would_you_rather"
private val CONTENT_CARD_TWO_COLUMN_MIN_WIDTH = 280.dp
private const val CONTENT_CARD_STACKED_FONT_SCALE = 1.5f
fun isWouldYouRatherItem(itemId: String): Boolean = itemId == WOULD_YOU_RATHER_ITEM_ID

private val FAMILIAR_QUIET_TITLES = listOf("I Spy", "Charades", "Would You Rather", "Animal Detective", "Alphabet Story")

fun contentListBackLabel(isMadLibsSubmenu: Boolean = false): String =
    if (isMadLibsSubmenu) "Back to Quiet Games" else "Back home"

private object Routes {
    const val Home = "home"
    const val QuickPlay = "quick_play"
    const val PickGame = "pick_game"
    const val GameType = "game_type/{groupId}"
    const val CalmDown = "calm_down"
    const val Account = "account"
    const val AboutApp = "about_app"
    const val SafetyPrivacy = "safety_privacy"
    const val Settings = "settings"
    const val TimedSession = "timed_session/{gameId}/{duration}/{rounds}"
    const val MadLibsCollection = "mad_libs_collection"
    const val WouldYouRather = WOULD_YOU_RATHER_ROUTE
    const val Category = "category/{categoryId}"
    const val Detail = "detail/{itemId}"
    fun category(categoryId: String) = "category/$categoryId"
    fun gameType(groupId: String) = "game_type/$groupId"
    fun detail(itemId: String) = "detail/$itemId"
    fun timedSession(session: TimedSession) =
        "timed_session/${session.gameId}/${session.configuration.duration.wireValue}/${session.configuration.rounds.wireValue}"
}

data class HomeShortcut(
    val icon: String,
    val title: String,
    val description: String,
    val route: String,
    val tag: String,
)

val HOME_SHORTCUTS = listOf(
    HomeShortcut("refresh", RANDOM_GAME_LABEL, "Choose a ready-to-use game or activity", Routes.QuickPlay, "random_game"),
    HomeShortcut("grid_view", ALL_GAMES_AND_ACTIVITIES_LABEL, "Choose a game type, then an activity", Routes.PickGame, "all_games_and_activities"),
)

@Composable
fun KinPlayApp() {
    val context = LocalContext.current.applicationContext
    val settingsRepository = remember(context) {
        AppSettingsRepository(SharedPreferencesSettingsKeyValueStore(context))
    }
    val launcherIconSwitcher = remember(context) {
        LauncherIconSwitcher(AndroidLauncherIconGateway(context))
    }
    var appSettings by remember { mutableStateOf(settingsRepository.load()) }
    fun persistSettings(changed: AppSettings) {
        appSettings = changed
        settingsRepository.save(changed)
    }
    LaunchedEffect(launcherIconSwitcher, appSettings.launcherIcon) {
        launcherIconSwitcher.switchTo(appSettings.launcherIcon)
    }

    KinPlayTheme(appSettings.colorTheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val contentPack = rememberContentPack()
            var favoriteIds by remember { mutableStateOf(loadIdSet(context, "favorite_ids")) }
            var recentIds by remember { mutableStateOf(loadIdList(context, "recent_ids")) }
            fun persistSessionOverride(gameId: String, override: SessionConfigurationOverride?) {
                if (override == null) {
                    settingsRepository.clearNextSessionOverride(gameId)
                } else {
                    settingsRepository.saveNextSessionOverride(gameId, override)
                }
                appSettings = settingsRepository.load()
            }
            fun startSession(gameId: String): TimedSession? {
                val item = contentPack.activeItemById(gameId) ?: return null
                if (!item.isTimedSessionEligible()) return null
                val session = startTimedSession(gameId, settingsRepository)
                appSettings = settingsRepository.load()
                return session
            }
            fun persistFavorites(ids: Set<String>) {
                favoriteIds = ids
                saveIdSet(context, "favorite_ids", ids)
            }
            fun persistRecent(ids: List<String>) {
                recentIds = ids
                saveIdList(context, "recent_ids", ids)
            }
            val navController = rememberNavController()
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(navController = navController, startDestination = Routes.Home) {
                    composable(Routes.Home) { HomeScreen(contentPack, favoriteIds, recentIds, navController) }
                    composable(Routes.QuickPlay) { QuickPlayScreen(contentPack, favoriteIds, recentIds, navController) }
                    composable(Routes.PickGame) { GameTypeListScreen(navController) }
                    composable(
                        Routes.GameType,
                        arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
                    ) { entry ->
                        val groupId = entry.arguments?.getString("groupId").orEmpty()
                        GameTypeDetailScreen(
                            group = GameTypeGroup.fromId(groupId),
                            items = contentPack.itemsForGameType(groupId),
                            favoriteIds = favoriteIds,
                            navController = navController,
                        )
                    }
                    composable(Routes.CalmDown) { ContentListScreen("Calm Down", contentPack.calmDownItems(), favoriteIds, navController) }
                    composable(Routes.Account) { AccountScreen(navController) }
                    composable(Routes.AboutApp) { AboutAppScreen(navController) }
                    composable(Routes.SafetyPrivacy) { SafetyPrivacyScreen(contentPack, navController) }
                    composable(Routes.Settings) {
                        SettingsScreen(
                            settings = appSettings,
                            onSettingsChange = ::persistSettings,
                            onLauncherIconChange = { launcherIcon ->
                                launcherIconSwitcher.switchTo(launcherIcon).also { result ->
                                    if (result != LauncherIconSwitchResult.FAILED_SAFE) {
                                        persistSettings(appSettings.copy(launcherIcon = launcherIcon))
                                    }
                                }
                            },
                            onBack = { navController.popBackStack() },
                        )
                    }
                    composable(
                        Routes.TimedSession,
                        arguments = listOf(
                            navArgument("gameId") { type = NavType.StringType },
                            navArgument("duration") { type = NavType.StringType },
                            navArgument("rounds") { type = NavType.StringType },
                        ),
                    ) { entry ->
                        val gameId = entry.arguments?.getString("gameId").orEmpty()
                        val duration = ActivityDuration.fromWireValue(entry.arguments?.getString("duration"))
                        val rounds = SessionRounds.fromWireValue(entry.arguments?.getString("rounds"))
                        val item = contentPack.activeItemById(gameId)
                        if (item == null || duration == null || rounds == null || !item.isTimedSessionEligible()) {
                            TimedSessionLoadError(onExit = { navController.popBackStack() })
                        } else {
                            TimedSessionScreen(
                                item = item,
                                session = TimedSession(
                                    gameId = gameId,
                                    configuration = SessionConfiguration(duration, rounds),
                                ),
                                onExit = { navController.popBackStack() },
                            )
                        }
                    }
                    composable(Routes.WouldYouRather) {
                        WouldYouRatherRoute(
                            gameTimer = appSettings.gameTimer,
                            showChildHandoffLock = shouldShowChildHandoffLock(
                                contentPack.activeItemById(WOULD_YOU_RATHER_ITEM_ID),
                            ),
                            onExit = { navController.popBackStack() },
                        )
                    }
                    composable(Routes.MadLibsCollection) {
                        ContentListScreen(
                            title = "Mad Libs",
                            items = contentPack.madLibs(),
                            favoriteIds = favoriteIds,
                            navController = navController,
                            backLabel = contentListBackLabel(isMadLibsSubmenu = true),
                        )
                    }
                    composable(
                        Routes.Category,
                        arguments = listOf(navArgument("categoryId") { type = NavType.StringType }),
                    ) { entry ->
                        val categoryId = entry.arguments?.getString("categoryId").orEmpty()
                        val category = QuickCategory.fromId(categoryId)
                        ContentListScreen(
                            title = category?.label ?: ALL_GAMES_AND_ACTIVITIES_LABEL,
                            items = contentPack.itemsForQuickCategory(categoryId),
                            favoriteIds = favoriteIds,
                            navController = navController,
                        )
                    }
                    composable(
                        Routes.Detail,
                        arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
                    ) { entry ->
                        val itemId = entry.arguments?.getString("itemId").orEmpty()
                        ActivityDetailScreen(
                            itemId = itemId,
                            item = contentPack.activeItemById(itemId),
                            isFavorite = itemId in favoriteIds,
                            onToggleFavorite = { persistFavorites(favoriteIds.toggleFavorite(itemId)) },
                            onMarkPlayed = { persistRecent(recentIds.withRecentFirst(itemId)) },
                            settings = appSettings,
                            navController = navController,
                            onSaveSessionOverride = ::persistSessionOverride,
                            onStartSession = ::startSession,
                            onOpenTimedSession = { session -> navController.navigate(Routes.timedSession(session)) },
                        )
                    }
                }
                if (BuildConfig.FEEDBACK_ENABLED) {
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    // Keep game/play lanes free of controls that must not bypass child handoff lock.
                    if (
                        backStackEntry?.destination?.route != Routes.WouldYouRather &&
                        backStackEntry?.destination?.route != Routes.Detail
                    ) {
                        val itemId = backStackEntry?.arguments?.getString("itemId")
                        val categoryId = backStackEntry?.arguments?.getString("categoryId")
                        val currentItem = itemId?.let(contentPack::activeItemById)
                        val route = when {
                            itemId != null -> "detail/$itemId"
                            categoryId != null -> "category/$categoryId"
                            else -> backStackEntry?.destination?.route ?: Routes.Home
                        }
                        FeedbackOverlay(
                            context = context,
                            screen = route,
                            contentId = currentItem?.id,
                            contentTitle = currentItem?.title,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberContentPack(): ContentPack {
    val context = LocalContext.current
    var pack by remember { mutableStateOf(ContentPack()) }
    LaunchedEffect(Unit) {
        pack = runCatching {
            val json = context.assets.open("kinplay_seed_v1.json").bufferedReader().use { it.readText() }
            ContentPack.fromJson(JSONObject(json))
        }.getOrElse { ContentPack(title = "Seed pack not loaded") }
    }
    return pack
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(contentPack: ContentPack, favoriteIds: Set<String>, recentIds: List<String>, navController: NavController) {
    var appMenuExpanded by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text("KinPlay", fontWeight = FontWeight.Bold)
                        Text(HOME_DESCRIPTOR, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                    }
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = { appMenuExpanded = true },
                            modifier = Modifier.testTag("app-menu-button"),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open app menu",
                                modifier = Modifier.semantics { contentDescription = "Open app menu" },
                            )
                        }
                        DropdownMenu(
                            expanded = appMenuExpanded,
                            onDismissRequest = { appMenuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                text = { Text("Settings") },
                                onClick = {
                                    appMenuExpanded = false
                                    navController.navigate(Routes.Settings)
                                },
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                text = { Text("Account") },
                                onClick = {
                                    appMenuExpanded = false
                                    navController.navigate(Routes.Account)
                                },
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                                text = { Text("About the app") },
                                onClick = {
                                    appMenuExpanded = false
                                    navController.navigate(Routes.AboutApp)
                                },
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                text = { Text("Safety and privacy") },
                                onClick = {
                                    appMenuExpanded = false
                                    navController.navigate(Routes.SafetyPrivacy)
                                },
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding).testTag("home-viewport")) {
            QuickCategoryGrid { category -> navController.navigate(Routes.category(category.id)) }
            val favoriteItems = contentPack.favoriteItems(favoriteIds)
            val recentItems = contentPack.recentItems(recentIds)
            if (favoriteItems.isNotEmpty()) {
                SectionTitle("Favorites")
                favoriteItems.take(3).forEach { item -> ContentCard(item, favoriteIds, navController) }
            }
            if (recentItems.isNotEmpty()) {
                SectionTitle("Recently played")
                recentItems.take(3).forEach { item -> ContentCard(item, favoriteIds, navController) }
            }
            HOME_SHORTCUTS.forEach { shortcut ->
                HomeButton(shortcut, "home-action-${shortcut.tag}") { navController.navigate(shortcut.route) }
            }
        }
    }
}

@Preview(name = "Home compact phone", widthDp = 320, heightDp = 640, showBackground = true)
@Preview(name = "Home representative wide", widthDp = 600, heightDp = 640, showBackground = true)
@Preview(name = "Home large text", widthDp = 320, heightDp = 640, fontScale = 1.5f, showBackground = true)
@Composable
private fun HomePreview() {
    KinPlayTheme(com.kinplay.app.settings.AppColorTheme.FOREST) {
        HomeScreen(
            contentPack = ContentPack(),
            favoriteIds = emptySet(),
            recentIds = emptyList(),
            navController = rememberNavController(),
        )
    }
}

@Composable
fun QuickCategoryGrid(onSelect: (QuickCategory) -> Unit) {
    BoxWithConstraints {
        val fontScale = LocalDensity.current.fontScale
        val cardHeight = when {
            fontScale >= 1.5f -> 136.dp
            maxWidth < 360.dp -> 108.dp
            else -> 88.dp
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickCategory.defaultGrid.chunked(2).forEach { categoryRow ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    categoryRow.forEach { category ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = cardHeight)
                                .testTag("home-category-${category.id}")
                                .clickable(
                                    onClickLabel = category.label,
                                    role = Role.Button,
                                    onClick = { onSelect(category) },
                                ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(category.label, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("${category.placeCue}  ›", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PageColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
fun HeroPanel(contentPack: ContentPack) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("KinPlay", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            Text(
                "Professional, offline-first family play for parent-led moments: quick games and activities, calm resets, creative prompts, and read-aloud silliness.",
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatPill("${contentPack.gameLibraryItems().size}", "games and activities")
                StatPill("${QuickCategory.defaultGrid.size}", "quick lists")
                StatPill("100%", "offline")
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun StatPill(value: String, label: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun DetailPill(text: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), shape = RoundedCornerShape(14.dp)) {
        Text(text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun HomeShortcutIcon(shortcut: HomeShortcut, testTag: String) {
    val icon = when (shortcut.icon) {
        "refresh" -> Icons.Default.Refresh
        "grid_view" -> Icons.Default.GridView
        else -> Icons.Default.Info
    }
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.testTag("${testTag}-icon"),
    )
}

@Composable
fun HomeButton(shortcut: HomeShortcut, testTag: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .testTag(testTag)
            .clickable(
                onClickLabel = "${shortcut.title}: ${shortcut.description}",
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HomeShortcutIcon(shortcut, testTag)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(shortcut.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Text("›", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SeedCard(contentPack: ContentPack) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Local seed pack", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(contentPack.title, color = MaterialTheme.colorScheme.onSurface)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailPill("${contentPack.gameLibraryItems().size} games and activities")
                DetailPill("${QuickCategory.defaultGrid.size} quick lists")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPlayScreen(contentPack: ContentPack, favoriteIds: Set<String>, recentIds: List<String>, navController: NavController) {
    val quickPick = remember(contentPack.items, recentIds) {
        contentPack.items.pickForModeAvoidingRecent("quick_play", recentIds)
    }
    Scaffold(topBar = { TopAppBar(title = { Text(RANDOM_GAME_LABEL) }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text(RANDOM_GAME_LABEL, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("A short, safe, local-content activity selected without network access.")
            if (quickPick == null) {
                Text("No eligible game or activity found yet.")
            } else {
                ContentCard(quickPick, favoriteIds, navController)
                Button(onClick = { navController.openItem(quickPick) }) { Text("Start this game or activity") }
            }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTypeListScreen(navController: NavController) {
    DestinationScreen(title = ALL_GAMES_AND_ACTIVITIES_LABEL, navController = navController) {
        Text(
            "Choose a game type first, then pick an activity.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        GameTypeGroup.entries.forEach { group ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("game-type-${group.id}")
                    .clickable(
                        onClickLabel = "Open ${group.label}",
                        role = Role.Button,
                        onClick = { navController.navigate(Routes.gameType(group.id)) },
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(group.label, fontWeight = FontWeight.Bold)
                    Text(group.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTypeDetailScreen(
    group: GameTypeGroup?,
    items: List<KinPlayItem>,
    favoriteIds: Set<String>,
    navController: NavController,
) {
    DestinationScreen(title = group?.label ?: ALL_GAMES_AND_ACTIVITIES_LABEL, navController = navController) {
        if (group == null) {
            Text("This game type is not available.")
        } else {
            Text(group.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (items.isEmpty()) {
                Text("No matching local content found.")
            }
            items.forEach { item -> ContentCard(item, favoriteIds, navController) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentListScreen(
    title: String,
    items: List<KinPlayItem>,
    favoriteIds: Set<String>,
    navController: NavController,
    backLabel: String = contentListBackLabel(),
) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            if (items.isEmpty()) {
                Text("No matching local content found.")
            }
            items.forEach { item -> ContentCard(item, favoriteIds, navController) }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text(backLabel) }
        }
    }
}

@Composable
fun ContentCard(
    item: KinPlayItem,
    favoriteIds: Set<String>,
    navController: NavController,
) {
    val title = "${if (item.id in favoriteIds) "★ " else ""}${item.title}"
    var expanded by rememberSaveable(item.id) { mutableStateOf(CONTENT_CARD_DEFAULT_EXPANDED) }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
    ) {
        CompactCardDetails(
            item = item,
            title = title,
            expanded = expanded,
            navController = navController,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
        )
    }
}

@Composable
fun CompactCardDetails(
    item: KinPlayItem,
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = item.title,
    expanded: Boolean = true,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val useStackedFallback =
            maxWidth < CONTENT_CARD_TWO_COLUMN_MIN_WIDTH ||
                LocalDensity.current.fontScale >= CONTENT_CARD_STACKED_FONT_SCALE

        if (useStackedFallback) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CompactCardPrimaryContent(item = item, title = title, expanded = expanded)
                CompactCardTrailingContent(
                    item = item,
                    expanded = expanded,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                CompactCardPrimaryContent(
                    item = item,
                    title = title,
                    expanded = expanded,
                    modifier = Modifier.weight(1f),
                )
                CompactCardTrailingContent(
                    item = item,
                    expanded = expanded,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(0.42f),
                )
            }
        }
    }
}

@Composable
private fun CompactCardPrimaryContent(
    item: KinPlayItem,
    title: String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
            )
            Text(
                text = if (expanded) "⌃" else "⌄",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
        item.collapsedCardPreviewLines().forEachIndexed { index, previewLine ->
            val previewText = if (index == 0) item.collapsedCardDescriptionAnnotated() else AnnotatedString(previewLine)
            Text(
                text = previewText,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = if (index == 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
            )
        }
        if (expanded && item.type == "mad_libs") {
            Text(
                text = "Mad Libs fields: ${item.madLibsFields.size}",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
private fun CompactCardTrailingContent(
    item: KinPlayItem,
    expanded: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        item.participantFitLabel()?.let { CompactCardDescriptor(it) }
        CompactCardDescriptor("${item.durationMinutes} min")
        CompactCardDescriptor(item.displayAgeRange())
        if (expanded) {
            Text(
                text = item.energyLevel,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = { navController.openItem(item) }) { Text("Open") }
        }
    }
}

@Composable
private fun CompactCardDescriptor(label: String) {
    Text(
        text = label,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End,
    )
}

fun activityDetailFeedbackCapture(
    feedbackEnabled: Boolean,
    isLocked: Boolean,
    itemId: String,
    item: KinPlayItem?,
): FeedbackCaptureContext? {
    if (!feedbackEnabled || isLocked) return null
    val resolvedItemId = itemId.ifBlank { item?.id.orEmpty() }
    return FeedbackCaptureContext(
        screen = "detail/$resolvedItemId",
        contentId = resolvedItemId.ifBlank { null },
        contentTitle = item?.title,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    item: KinPlayItem?,
    itemId: String = item?.id.orEmpty(),
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onMarkPlayed: () -> Unit,
    settings: AppSettings,
    navController: NavController,
    onSaveSessionOverride: (String, SessionConfigurationOverride?) -> Unit = { _, _ -> },
    onStartSession: (String) -> TimedSession? = { null },
    onOpenTimedSession: (TimedSession) -> Unit = {},
) {
    if (shouldShowChildHandoffLock(item)) {
        ChildHandoffLockContainer { isLocked ->
            ActivityDetailSurface(
                item = item,
                itemId = itemId,
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite,
                onMarkPlayed = onMarkPlayed,
                settings = settings,
                navController = navController,
                isLocked = isLocked,
                onSaveSessionOverride = onSaveSessionOverride,
                onStartSession = onStartSession,
                onOpenTimedSession = onOpenTimedSession,
            )
        }
    } else {
        ActivityDetailSurface(
            item = item,
            itemId = itemId,
            isFavorite = isFavorite,
            onToggleFavorite = onToggleFavorite,
            onMarkPlayed = onMarkPlayed,
            settings = settings,
            navController = navController,
            isLocked = false,
            onSaveSessionOverride = onSaveSessionOverride,
            onStartSession = onStartSession,
            onOpenTimedSession = onOpenTimedSession,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityDetailSurface(
    item: KinPlayItem?,
    itemId: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onMarkPlayed: () -> Unit,
    settings: AppSettings,
    navController: NavController,
    isLocked: Boolean,
    onSaveSessionOverride: (String, SessionConfigurationOverride?) -> Unit,
    onStartSession: (String) -> TimedSession?,
    onOpenTimedSession: (TimedSession) -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val resolvedSession = item
        ?.takeIf(KinPlayItem::isTimedSessionEligible)
        ?.let { settings.resolveNextSessionConfiguration(it.id) }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = { TopAppBar(title = { Text(item?.title ?: "Activity") }) }) { innerPadding ->
            PageColumn(Modifier.padding(innerPadding)) {
                if (item == null) {
                    Text("Activity not found.")
                } else {
                    Text(item.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    BundledMusicControls(item.id, enabled = !isLocked)
                    PaperAirplaneInstructions(item, enabled = !isLocked)
                    if (item.id == "bilateral_mirror_moves" || item.id == "cross_body_move_mix") {
                        BrainMovementInstructions(item, enabled = !isLocked)
                    }
                    if (item.id == CHARADES_ITEM_ID) {
                        CharadesCardsPanel(enabled = !isLocked)
                    }
                    if (item.isTimedSessionEligible()) {
                        SessionConfigurationControls(
                            itemId = item.id,
                            settings = settings,
                            enabled = !isLocked,
                            onSaveSessionOverride = onSaveSessionOverride,
                            onStartSession = { gameId ->
                                onStartSession(gameId)?.let { session ->
                                    onMarkPlayed()
                                    onOpenTimedSession(session)
                                }
                            },
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val playPlan = resolvedSession ?: settings.sessionDefaults()
                        Text(
                            "Your play plan: ${playPlan.duration.label} activity • ${playPlan.rounds.label} • ${settings.gameTimer.label} per turn",
                            modifier = Modifier.padding(14.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailPill(item.displayAgeRange())
                        DetailPill("Typical: ${item.durationMinutes} min")
                        DetailPill(item.energyLevel)
                    }
                    item.participantFitLabel()?.let { DetailPill(it) }
                    item.detailSections().forEach { section ->
                        SectionList(section.title, section.lines)
                    }
                    if (item.parentNotes.isNotBlank()) {
                        Text("Parent note", fontWeight = FontWeight.Bold)
                        Text(item.parentNotes)
                    }
                    if (item.type == "mad_libs") {
                        MadLibPlayPanel(item, enabled = !isLocked)
                    }
                    Button(onClick = onMarkPlayed, enabled = !isLocked) { Text("Mark played") }
                    OutlinedButton(onClick = onToggleFavorite, enabled = !isLocked) { Text(if (isFavorite) "Remove favorite" else "Add favorite") }
                }
                OutlinedButton(onClick = { navController.popBackStack() }, enabled = !isLocked) { Text("Back") }
            }
        }
        activityDetailFeedbackCapture(
            feedbackEnabled = BuildConfig.FEEDBACK_ENABLED,
            isLocked = isLocked,
            itemId = itemId,
            item = item,
        )?.let { capture ->
            FeedbackOverlay(
                context = context,
                screen = capture.screen,
                contentId = capture.contentId,
                contentTitle = capture.contentTitle,
            )
        }
    }
}

@Composable
fun TimedSessionScreen(
    item: KinPlayItem,
    session: TimedSession,
    onExit: () -> Unit,
) {
    if (shouldShowChildHandoffLock(item)) {
        ChildHandoffLockContainer { isLocked ->
            TimedSessionSurface(item, session, isLocked, onExit)
        }
    } else {
        TimedSessionSurface(item, session, isLocked = false, onExit = onExit)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimedSessionSurface(
    item: KinPlayItem,
    session: TimedSession,
    isLocked: Boolean,
    onExit: () -> Unit,
) {
    val configuration = session.configuration
    val initialProgress = remember(session.gameId, configuration) {
        TimedSessionProgress.initial(configuration)
    }
    var round by rememberSaveable(session.gameId, configuration.rounds.wireValue) {
        mutableStateOf(initialProgress.round)
    }
    var remainingSeconds by rememberSaveable(session.gameId, configuration.duration.wireValue) {
        mutableStateOf(initialProgress.remainingSeconds)
    }
    var statusName by rememberSaveable(session.gameId, configuration.rounds.wireValue) {
        mutableStateOf(initialProgress.status.name)
    }
    val isComplete = statusName == TimedSessionStatus.COMPLETE.name

    LaunchedEffect(session.gameId, configuration) {
        while (statusName == TimedSessionStatus.ACTIVE.name) {
            delay(1_000L)
            val next = TimedSessionProgress(
                round = round,
                remainingSeconds = remainingSeconds,
                status = TimedSessionStatus.valueOf(statusName),
            ).tick(configuration)
            round = next.round
            remainingSeconds = next.remainingSeconds
            statusName = next.status.name
        }
    }

    fun finishRound() {
        val next = TimedSessionProgress(
            round = round,
            remainingSeconds = remainingSeconds,
            status = TimedSessionStatus.valueOf(statusName),
        ).completeRound(configuration)
        round = next.round
        remainingSeconds = next.remainingSeconds
        statusName = next.status.name
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Timed session") }) },
    ) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding).testTag("timed-session-surface")) {
            Text(item.title, fontWeight = FontWeight.Bold)
            BundledMusicControls(item.id, enabled = !isLocked)
            PaperAirplaneInstructions(item, enabled = !isLocked)
            if (item.id == "bilateral_mirror_moves" || item.id == "cross_body_move_mix") {
                BrainMovementInstructions(item, enabled = !isLocked)
            }
            if (item.id == CHARADES_ITEM_ID) {
                CharadesCardsPanel(enabled = !isLocked)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "Round $round of ${configuration.rounds.count}",
                        modifier = Modifier.testTag("timed-session-round"),
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        TimedSessionProgress(round, remainingSeconds, TimedSessionStatus.valueOf(statusName))
                            .remainingTimeLabel(),
                        modifier = Modifier.testTag("timed-session-timer"),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("${configuration.duration.label} per round")
                }
            }
            item.activeSessionSections().forEach { section ->
                SectionList(section.title, section.lines)
            }
            if (isComplete) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth().testTag("timed-session-complete"),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Session complete", fontWeight = FontWeight.Bold)
                        Text("You finished all ${configuration.rounds.count} rounds.")
                        Button(onClick = onExit, enabled = !isLocked) {
                            Text("Back to details")
                        }
                    }
                }
            } else {
                Button(
                    onClick = ::finishRound,
                    enabled = !isLocked,
                    modifier = Modifier.fillMaxWidth().testTag("timed-session-finish-round"),
                ) {
                    Text("Finish round")
                }
                OutlinedButton(
                    onClick = onExit,
                    enabled = !isLocked,
                    modifier = Modifier.fillMaxWidth().testTag("timed-session-exit"),
                ) {
                    Text("Exit session")
                }
            }
        }
    }
}

@Composable
private fun TimedSessionLoadError(onExit: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("This timed session is not available.", textAlign = TextAlign.Center)
        OutlinedButton(onClick = onExit, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun SessionConfigurationControls(
    itemId: String,
    settings: AppSettings,
    enabled: Boolean = true,
    onSaveSessionOverride: (String, SessionConfigurationOverride?) -> Unit,
    onStartSession: (String) -> Unit,
) {
    val currentOverride = settings.nextSessionOverrides[itemId]
    val resolved = settings.resolveNextSessionConfiguration(itemId)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("session-configuration"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(18.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("Timed session", fontWeight = FontWeight.Bold)
            Text(
                "Choose values for this next session. Settings defaults stay unchanged.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                "Applied session: ${resolved.duration.label} • ${resolved.rounds.label}",
                modifier = Modifier.testTag("session-applied"),
                fontWeight = FontWeight.Bold,
            )
            Text("Session duration", fontWeight = FontWeight.Bold)
            SessionChoiceStrip(
                options = ActivityDuration.entries,
                selected = resolved.duration,
                testTagPrefix = "session-duration",
                label = { it.label },
                wireValue = { it.wireValue },
                enabled = enabled,
                onClick = { option ->
                    onSaveSessionOverride(
                        itemId,
                        SessionConfigurationOverride(duration = option, rounds = currentOverride?.rounds),
                    )
                },
            )
            Text("Session rounds", fontWeight = FontWeight.Bold)
            SessionChoiceStrip(
                options = SessionRounds.entries,
                selected = resolved.rounds,
                testTagPrefix = "session-rounds",
                label = { it.label },
                wireValue = { it.wireValue },
                enabled = enabled,
                onClick = { option ->
                    onSaveSessionOverride(
                        itemId,
                        SessionConfigurationOverride(duration = currentOverride?.duration, rounds = option),
                    )
                },
            )
            if (currentOverride != null) {
                OutlinedButton(
                    onClick = { onSaveSessionOverride(itemId, null) },
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("session-reset-button"),
                ) {
                    Text("Use Settings defaults")
                }
            }
            Button(
                onClick = { onStartSession(itemId) },
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("session-start-button"),
            ) {
                Text("Start session")
            }
        }
    }
}

@Composable
private fun <T> SessionChoiceStrip(
    options: List<T>,
    selected: T,
    testTagPrefix: String,
    label: (T) -> String,
    wireValue: (T) -> String,
    enabled: Boolean,
    onClick: (T) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .testTag("$testTagPrefix-strip"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onClick(option) },
                enabled = enabled,
                label = { Text(label(option)) },
                modifier = Modifier.testTag("$testTagPrefix-${wireValue(option)}"),
            )
        }
    }
}

@Composable
fun InfoPanel(title: String, body: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectionList(title: String, values: List<String>) {
    if (values.isNotEmpty()) {
        Text(title, fontWeight = FontWeight.Bold)
        values.forEachIndexed { index, value -> Text("${index + 1}. $value") }
    }
}

@Composable
fun MadLibPlayPanel(story: KinPlayItem, enabled: Boolean = true) {
    val answers = story.madLibsFields.associate { field ->
        field.key to rememberSaveable(story.id, field.key) { mutableStateOf("") }
    }
    var revealed by rememberSaveable(story.id) { mutableStateOf(false) }
    InfoPanel("Story activity", "Fill in each prompt without reading the story first, then reveal the result.")
    story.madLibsFields.forEach { field ->
        OutlinedTextField(
            value = answers.getValue(field.key).value,
            onValueChange = {
                answers.getValue(field.key).value = it
                revealed = false
            },
            label = { Text(field.label) },
            placeholder = { Text(field.example) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = enabled,
        )
    }
    val answerValues = answers.mapValues { it.value.value }
    val allFilled = story.madLibsFields.all { answerValues[it.key].orEmpty().isNotBlank() }
    Button(onClick = { revealed = true }, enabled = allFilled && enabled) { Text("Reveal story") }
    if (revealed && allFilled) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Your story", fontWeight = FontWeight.Bold)
                Text(story.renderMadLib(answerValues))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    DestinationScreen(title = "Account", navController = navController) {
        Text("No account system is included in this MVP.", fontWeight = FontWeight.Bold)
        Text("Settings and feedback stay on this device. Account features are staged for a future product decision.")
    }
}

@Composable
fun AboutAppScreen(navController: NavController) {
    DestinationScreen(title = "About the app", navController = navController) {
        Text("KinPlay", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Offline-first family play for parent-led moments.")
        Text("Version ${BuildConfig.VERSION_NAME}")
    }
}

@Composable
fun SafetyPrivacyScreen(contentPack: ContentPack, navController: NavController) {
    DestinationScreen(title = "Safety and privacy", navController = navController) {
        Text("Parent-led by design", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("KinPlay is for adults to guide short play sessions with children. Review the activity, clear the space, and supervise movement or materials.")
            Text("MVP privacy", fontWeight = FontWeight.Bold)
            Text("No accounts, analytics, ads, purchases, camera, microphone, contacts, location, or other sensitive Android permissions are requested.")
            Text("Child handoff lock limits", fontWeight = FontWeight.Bold)
            Text("The 3-second child handoff lock prevents accidental controls, Back, and exits inside KinPlay. It is not kiosk mode and cannot block Android system navigation, notifications, power controls, or another person leaving the app. KinPlay does not request device-owner, accessibility, or intrusive permissions.")
            Text("Content source", fontWeight = FontWeight.Bold)
            Text("The app ships seed content as a local JSON asset and does not need network access for the MVP flow.")
            Text("Reviewed content safety", fontWeight = FontWeight.Bold)
            contentPack.activeItems()
                .map(::reviewedSafetyTagSummary)
                .distinct()
                .take(3)
                .forEach { summary -> Text(summary) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DestinationScreen(
    title: String,
    navController: NavController,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    Text(
                        text = "‹ Back",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(onClick = { navController.popBackStack() })
                            .padding(16.dp),
                    )
                },
            )
        },
    ) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding), content = content)
    }
}

data class ContentPack(
    val title: String = "Loading seed pack...",
    val items: List<KinPlayItem> = emptyList(),
) {
    fun activeItems() = items.activeContent()
    fun activeItemById(id: String) = activeItems().firstOrNull { it.id == id }
    fun favoriteItems(favoriteIds: Set<String>) = activeItems().filter { it.id in favoriteIds }
    fun recentItems(recentIds: List<String>) = recentIds.mapNotNull(::activeItemById)
    fun activities() = activeItems().filter { it.type == "activity" }
    fun madLibs() = activeItems().filter { it.type == "mad_libs" }
    fun gameLibraryItems() = activeItems()
    fun pickGameItems() = gameLibraryItems()
    fun itemsForQuickCategory(categoryId: String) =
        if (categoryId == QuickCategory.QUIET_GAMES.id) quietGamesDisplayItems() else items.itemsForQuickCategory(categoryId)
    fun quietGamesDisplayItems(): List<KinPlayItem> {
        val quietItems = items.itemsForQuickCategory(QuickCategory.QUIET_GAMES.id)
        val familiarByTitle = quietItems.filterNot { it.type == "mad_libs" }.associateBy { it.title }
        val familiar = FAMILIAR_QUIET_TITLES.mapNotNull(familiarByTitle::get)
        val remaining = quietItems.filterNot { it.type == "mad_libs" || it.title in FAMILIAR_QUIET_TITLES }
        val collection = madLibs().takeIf { it.isNotEmpty() }?.let(::madLibsCollectionItem)
        return familiar + listOfNotNull(collection) + remaining
    }
    fun calmDownItems() = activeItems().filter { "calm_down" in it.modes || "calming" in it.safetyTags }
    fun quickPlayPick(): KinPlayItem? = items.pickForMode("quick_play")

    companion object {
        fun fromJson(root: JSONObject): ContentPack {
            val array = root.getJSONArray("items")
            return ContentPack(
                title = root.optString("title", "KinPlay Seed Pack"),
                items = List(array.length()) { index -> KinPlayItem.fromJson(array.getJSONObject(index)) },
            )
        }
    }
}

data class KinPlayItem(
    val id: String,
    val type: String,
    val status: String,
    val title: String,
    val summary: String,
    val collapsedDescription: String = "",
    val collapsedEmphasis: List<String> = emptyList(),
    val visualAssets: List<ContentVisualAsset> = emptyList(),
    val paperAirplaneModels: List<PaperAirplaneModel> = emptyList(),
    val modes: List<String>,
    val minAge: Int,
    val maxAge: Int,
    val durationMinutes: Int,
    val energyLevel: String,
    val quickCategories: List<String> = emptyList(),
    val materials: List<String> = emptyList(),
    val safetyTags: List<String> = emptyList(),
    val setupSteps: List<String> = emptyList(),
    val playSteps: List<String> = emptyList(),
    val parentNotes: String = "",
    val variations: List<String> = emptyList(),
    val promptText: String = "",
    val followUps: List<String> = emptyList(),
    val madLibsFields: List<MadLibField> = emptyList(),
    val madLibsTemplate: String = "",
    val readAloudNote: String = "",
    val participantSuitability: ParticipantSuitability? = null,
    val childHandoffLockEligible: Boolean = false,
) {
    fun renderMadLib(answers: Map<String, String>): String {
        var result = madLibsTemplate
        madLibsFields.forEach { field -> result = result.replace("{${field.key}}", answers[field.key].orEmpty()) }
        return result
    }

    companion object {
        fun fromJson(json: JSONObject): KinPlayItem {
            val madLibs = json.optJSONObject("madLibs")
            val fields = madLibs?.optJSONArray("fields")
            val status = json.getString("status")
            val quickCategories = json.stringList("quickCategories")
            val participantSuitabilityWireValue = json.optString("participantSuitability", "")
            val participantSuitability = ParticipantSuitability.fromWireValue(participantSuitabilityWireValue)
            require(participantSuitabilityWireValue.isBlank() || participantSuitability != null) {
                "Unknown participantSuitability '$participantSuitabilityWireValue' for ${json.getString("id")}"
            }
            require((status != "active" && "quality_time" !in quickCategories) || participantSuitability != null) {
                "Active or Quality Time item ${json.getString("id")} requires participantSuitability"
            }
            val childHandoffLockEligible = json.optBoolean("childHandoffLockEligible", false)
            return KinPlayItem(
                id = json.getString("id"),
                type = json.getString("type"),
                status = status,
                title = json.getString("title"),
                summary = json.getString("summary"),
                collapsedDescription = json.optString("collapsedDescription", ""),
                collapsedEmphasis = json.stringList("collapsedEmphasis"),
                visualAssets = json.optJSONArray("visualAssets")?.let { array ->
                    (0 until array.length()).map { index ->
                        val asset = array.getJSONObject(index)
                        ContentVisualAsset(asset.getString("id"), asset.getString("resource"), asset.getString("altText"))
                    }
                } ?: emptyList(),
                paperAirplaneModels = json.optJSONArray("paperAirplaneModels")?.let { array ->
                    (0 until array.length()).map { index ->
                        val model = array.getJSONObject(index)
                        PaperAirplaneModel(
                            id = model.getString("id"),
                            name = model.getString("name"),
                            shapeDescription = model.getString("shapeDescription"),
                            diagramAsset = model.getString("diagramAsset"),
                            steps = model.stringList("steps"),
                        )
                    }
                } ?: emptyList(),
                modes = json.stringList("modes"),
                minAge = json.getInt("minAge"),
                maxAge = json.getInt("maxAge"),
                durationMinutes = json.getInt("durationMinutes"),
                energyLevel = json.getString("energyLevel"),
                quickCategories = quickCategories,
                materials = json.stringList("materials"),
                safetyTags = json.stringList("safetyTags"),
                setupSteps = json.stringList("setupSteps"),
                playSteps = json.stringList("playSteps"),
                parentNotes = json.optString("parentNotes", ""),
                variations = json.stringList("variations"),
                promptText = json.optString("promptText", ""),
                followUps = json.stringList("followUps"),
                madLibsFields = if (fields == null) emptyList() else List(fields.length()) { MadLibField.fromJson(fields.getJSONObject(it)) },
                madLibsTemplate = madLibs?.optString("template", "").orEmpty(),
                readAloudNote = madLibs?.optString("readAloudNote", "").orEmpty(),
                participantSuitability = participantSuitability,
                childHandoffLockEligible = childHandoffLockEligible,
            )
        }
    }
}

data class MadLibField(
    val key: String,
    val label: String,
    val kind: String,
    val example: String,
) {
    companion object {
        fun fromJson(json: JSONObject) = MadLibField(
            key = json.getString("key"),
            label = json.getString("label"),
            kind = json.getString("kind"),
            example = json.optString("example", ""),
        )
    }
}

data class DetailSection(
    val title: String,
    val lines: List<String>,
)

enum class ParticipantSuitability(val wireValue: String, val displayLabel: String) {
    ONE_ON_ONE("one_on_one", "Best for 1:1"),
    GROUP("group", "Best for a group"),
    BOTH("both", "Works 1:1 or with a group");

    companion object {
        fun fromWireValue(value: String): ParticipantSuitability? = entries.firstOrNull { it.wireValue == value }
    }
}

enum class QuickCategory(val id: String, val label: String, val placeCue: String) {
    QUIET_GAMES("quiet_games", "Quiet Games", "Waiting room • couch"),
    DINNER_TABLE("dinner_table", "At the Dinner Table", "Dining table • restaurant"),
    OUTDOOR_ADVENTURES("outdoor_adventures", "Outdoor Adventures", "Backyard • park"),
    GET_ENERGY_OUT("get_energy_out", "Get the Energy Out", "Living room • backyard"),
    BRAIN_GAMES("brain_games", "Brain Games", "Table • waiting room"),
    QUALITY_TIME("quality_time", "Quality Time", "Anywhere • 1:1 or group"),
    ARTS_AND_MAKING("arts_and_making", "Arts and making", "Table • craft space"),
    BRAIN_AND_MOVEMENT("brain_and_movement", "Brain & movement", "Living room • clear floor");

    companion object {
        val defaultGrid = listOf(
            QUIET_GAMES,
            DINNER_TABLE,
            OUTDOOR_ADVENTURES,
            GET_ENERGY_OUT,
            BRAIN_GAMES,
            QUALITY_TIME,
        )
        fun fromId(id: String): QuickCategory? = entries.firstOrNull { it.id == id }
    }
}

fun List<KinPlayItem>.activeContent(): List<KinPlayItem> = filter { it.status == "active" }

fun shouldShowChildHandoffLock(item: KinPlayItem?): Boolean = item?.childHandoffLockEligible == true

fun List<KinPlayItem>.itemsForMode(mode: String): List<KinPlayItem> = activeContent().filter { mode in it.modes }

fun List<KinPlayItem>.itemsForQuickCategory(categoryId: String): List<KinPlayItem> =
    activeContent().filter { categoryId in it.quickCategories }

fun List<KinPlayItem>.pickForMode(mode: String, seed: Long = System.currentTimeMillis()): KinPlayItem? {
    val eligible = itemsForMode(mode).filter { it.type != "mad_libs" }
    if (eligible.isEmpty()) return null
    return eligible[Random(seed).nextInt(eligible.size)]
}

fun List<KinPlayItem>.pickForModeAvoidingRecent(
    mode: String,
    recentIds: List<String>,
    seed: Long = System.currentTimeMillis(),
): KinPlayItem? {
    val eligible = itemsForMode(mode).filter { it.type != "mad_libs" }
    if (eligible.isEmpty()) return null
    val unplayed = eligible.filterNot { it.id in recentIds }
    val pool = if (unplayed.isNotEmpty()) unplayed else eligible
    return pool[Random(seed).nextInt(pool.size)]
}

fun KinPlayItem.displayAgeRange(): String =
    if (minAge == maxAge) "Age $minAge" else "Ages $minAge–$maxAge"

fun KinPlayItem.participantFitLabel(): String? = participantSuitability?.displayLabel

fun KinPlayItem.setupBurdenLabel(): String =
    if (materials.isEmpty()) "No materials" else "Needs: ${materials.joinToString()}"

fun KinPlayItem.setupPreviewLabel(maxCharacters: Int = 84): String {
    val prefix = "Setup: "
    if (id == "quiet_color_hunt") return "Clues and suggestions: Choose one safe object everyone can see."
    val firstStep = displaySetupSteps().firstOrNull { it.isNotBlank() }?.trim() ?: return "${prefix}No setup needed"
    val availableCharacters = (maxCharacters - prefix.length).coerceAtLeast(1)
    if (firstStep.length <= availableCharacters) return prefix + firstStep
    if (availableCharacters == 1) return prefix + "…"

    val candidate = firstStep.take(availableCharacters - 1).trimEnd()
    val lastSpace = candidate.lastIndexOf(' ')
    val shortened = if (lastSpace > 0) candidate.substring(0, lastSpace) else candidate
    return prefix + shortened.trimEnd() + "…"
}

fun KinPlayItem.collapsedCardDescriptionText(): String = collapsedDescription.ifBlank { summary }

fun KinPlayItem.collapsedCardDescriptionAnnotated(): AnnotatedString {
    val text = collapsedCardDescriptionText()
    val emphasis = collapsedEmphasis.filter { it.isNotBlank() }.sortedByDescending(String::length)
    return buildAnnotatedString {
        var cursor = 0
        while (cursor < text.length) {
            val match = emphasis
                .mapNotNull { phrase -> text.indexOf(phrase, startIndex = cursor, ignoreCase = true).takeIf { it >= 0 }?.let { it to phrase } }
                .minByOrNull { it.first }
            if (match == null) {
                append(text.substring(cursor))
                break
            }
            val (start, phrase) = match
            if (start > cursor) append(text.substring(cursor, start))
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(text.substring(start, start + phrase.length))
            }
            cursor = start + phrase.length
        }
    }
}

fun KinPlayItem.collapsedCardPreviewLines(): List<String> =
    listOf(collapsedCardDescriptionText(), setupBurdenLabel(), setupPreviewLabel())

fun KinPlayItem.displaySetupSteps(): List<String> =
    if (id == "quiet_color_hunt") listOf("Choose one safe object everyone can see.") else setupSteps

fun KinPlayItem.isMadLibsCollection(): Boolean = id == MAD_LIBS_COLLECTION_ID

fun madLibsCollectionItem(stories: List<KinPlayItem>) = KinPlayItem(
    id = MAD_LIBS_COLLECTION_ID,
    type = "collection",
    status = "active",
    title = "Mad Libs",
    summary = "Open all ${stories.size} ready-to-fill silly stories.",
    collapsedDescription = "Zany stories to fill in and read aloud.",
    collapsedEmphasis = listOf("Zany stories"),
    modes = listOf("mad_libs"),
    minAge = stories.minOf { it.minAge },
    maxAge = stories.maxOf { it.maxAge },
    durationMinutes = stories.minOf { it.durationMinutes },
    energyLevel = "calm",
    quickCategories = listOf(QuickCategory.QUIET_GAMES.id),
    safetyTags = listOf("quiet", "no_materials", "reading_help"),
    participantSuitability = ParticipantSuitability.BOTH,
)

fun itemDestination(item: KinPlayItem): String =
    when {
        item.isMadLibsCollection() -> Routes.MadLibsCollection
        isWouldYouRatherItem(item.id) -> Routes.WouldYouRather
        else -> Routes.detail(item.id)
    }

private fun NavController.openItem(item: KinPlayItem) {
    navigate(itemDestination(item))
}

fun String.displayTagLabel(): String = when (this) {
    "parent_supervision" -> "Parent supervision"
    "movement" -> "Movement"
    "quiet" -> "Quiet"
    "no_materials" -> "No materials"
    "small_objects" -> "Small objects"
    "food_optional" -> "Food optional"
    "outdoor_optional" -> "Outdoor optional"
    "reading_help" -> "Reading help"
    "sibling_friendly" -> "Sibling friendly"
    "calming" -> "Calming"
    else -> split('_')
        .filter { it.isNotBlank() }
        .joinToString(" ") { it.lowercase() }
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun reviewedSafetyTagSummary(item: KinPlayItem): String =
    "Safety tags: ${item.safetyTags.joinToString { it.displayTagLabel() }}"

fun KinPlayItem.detailSections(): List<DetailSection> = buildList {
    add(DetailSection("Materials", listOf(if (materials.isEmpty()) "No materials needed." else materials.joinToString())))
    if (displaySetupSteps().isNotEmpty()) add(DetailSection("Setup", displaySetupSteps()))
    if (id == "quiet_color_hunt") {
        if (playSteps.isNotEmpty()) add(DetailSection("Clues and suggestions", playSteps))
    } else if (playSteps.isNotEmpty()) {
        add(DetailSection("Steps", playSteps))
    }
    if (promptText.isNotBlank()) add(DetailSection("Prompt", listOf(promptText)))
    if (followUps.isNotEmpty()) add(DetailSection("Follow-up questions", followUps))
    if (readAloudNote.isNotBlank()) add(DetailSection("Read-aloud note", listOf(readAloudNote)))
    if (variations.isNotEmpty()) add(DetailSection("Replay variations", variations))
}

fun List<String>.withRecentFirst(id: String, limit: Int = 10): List<String> =
    (listOf(id) + filterNot { it == id }).take(limit)

fun Set<String>.toggleFavorite(id: String): Set<String> =
    if (id in this) this - id else this + id

private fun loadIdSet(context: Context, key: String): Set<String> =
    context.getSharedPreferences("kinplay", Context.MODE_PRIVATE).getStringSet(key, emptySet()).orEmpty()

private fun saveIdSet(context: Context, key: String, ids: Set<String>) {
    context.getSharedPreferences("kinplay", Context.MODE_PRIVATE).edit().putStringSet(key, ids).apply()
}

private fun loadIdList(context: Context, key: String): List<String> =
    context.getSharedPreferences("kinplay", Context.MODE_PRIVATE).getString(key, "").orEmpty().split(",").filter { it.isNotBlank() }

private fun saveIdList(context: Context, key: String, ids: List<String>) {
    context.getSharedPreferences("kinplay", Context.MODE_PRIVATE).edit().putString(key, ids.joinToString(",")).apply()
}

fun JSONObject.stringList(name: String): List<String> {
    val array = optJSONArray(name) ?: return emptyList()
    return List(array.length()) { index -> array.getString(index) }
}
