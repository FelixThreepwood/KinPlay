package com.kinplay.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.kinplay.app.settings.AppSettings
import com.kinplay.app.settings.AppSettingsRepository
import com.kinplay.app.settings.LauncherIconSwitchResult
import com.kinplay.app.settings.LauncherIconSwitcher
import com.kinplay.app.settings.SettingsScreen
import com.kinplay.app.settings.SharedPreferencesSettingsKeyValueStore
import com.kinplay.app.ui.KinPlayTheme
import com.kinplay.app.wyr.WouldYouRatherRoute
import org.json.JSONObject
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KinPlayApp() }
    }
}

const val CONTENT_CARD_DEFAULT_EXPANDED = false
const val HOME_DESCRIPTOR = "Ready-to-use family games and activities"
const val HOME_INSTRUCTION_SECTION_ENABLED = false
const val RANDOM_GAME_LABEL = "Random game"
const val ALL_GAMES_AND_ACTIVITIES_LABEL = "All games and activities"
const val MAD_LIBS_COLLECTION_ID = "mad_libs_collection"
const val WOULD_YOU_RATHER_ITEM_ID = "would_you_rather_silly_family"
const val WOULD_YOU_RATHER_ROUTE = "would_you_rather"
private val CONTENT_CARD_TWO_COLUMN_MIN_WIDTH = 280.dp
private const val CONTENT_CARD_STACKED_FONT_SCALE = 1.5f
fun isWouldYouRatherItem(itemId: String): Boolean = itemId == WOULD_YOU_RATHER_ITEM_ID

private val FAMILIAR_QUIET_TITLES = listOf("I Spy", "Charades", "Would You Rather", "Animal Guessing", "Alphabet Story")

fun contentListBackLabel(isMadLibsSubmenu: Boolean = false): String =
    if (isMadLibsSubmenu) "Back to Quiet Games" else "Back home"

private object Routes {
    const val Home = "home"
    const val QuickPlay = "quick_play"
    const val PickGame = "pick_game"
    const val CalmDown = "calm_down"
    const val AboutSafety = "about_safety"
    const val Settings = "settings"
    const val MadLibsCollection = "mad_libs_collection"
    const val WouldYouRather = WOULD_YOU_RATHER_ROUTE
    const val Category = "category/{categoryId}"
    const val Detail = "detail/{itemId}"
    fun category(categoryId: String) = "category/$categoryId"
    fun detail(itemId: String) = "detail/$itemId"
}

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
                    composable(Routes.PickGame) { ContentListScreen(ALL_GAMES_AND_ACTIVITIES_LABEL, contentPack.gameLibraryItems(), favoriteIds, navController) }
                    composable(Routes.CalmDown) { ContentListScreen("Calm Down", contentPack.calmDownItems(), favoriteIds, navController) }
                    composable(Routes.AboutSafety) { AboutSafetyScreen(navController) }
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
                    composable(Routes.WouldYouRather) {
                        WouldYouRatherRoute(
                            gameTimer = appSettings.gameTimer,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            QuickCategoryGrid { category -> navController.navigate(Routes.category(category.id)) }
            val favoriteItems = contentPack.favoriteItems(favoriteIds)
            val recentItems = contentPack.recentItems(recentIds)
            if (favoriteItems.isNotEmpty()) {
                SectionTitle("Favorites", "Saved picks for faster family starts")
                favoriteItems.take(3).forEach { item -> ContentCard(item, favoriteIds, navController) }
            }
            if (recentItems.isNotEmpty()) {
                SectionTitle("Recently played", "Return to what already worked")
                recentItems.take(3).forEach { item -> ContentCard(item, favoriteIds, navController) }
            }
            SectionTitle("More ways to start", "Offline, parent-led choices for ages 2–8")
            HomeButton(RANDOM_GAME_LABEL, "Choose a ready-to-use game or activity") { navController.navigate(Routes.QuickPlay) }
            HomeButton(ALL_GAMES_AND_ACTIVITIES_LABEL, "See every game and activity, including story activities") { navController.navigate(Routes.PickGame) }
            HomeButton("Settings", "Timers, activity duration, and color theme") { navController.navigate(Routes.Settings) }
            HomeButton("About / Safety", "Parent-led safety and privacy notes") { navController.navigate(Routes.AboutSafety) }
        }
    }
}

@Composable
fun QuickCategoryGrid(onSelect: (QuickCategory) -> Unit) {
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
                            .height(88.dp)
                            .clickable { onSelect(category) },
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
fun HomeButton(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth().clickable(
            onClickLabel = title,
            role = Role.Button,
            onClick = onClick,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
fun ContentListScreen(
    title: String,
    items: List<KinPlayItem>,
    favoriteIds: Set<String>,
    navController: NavController,
    backLabel: String = contentListBackLabel(),
) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            SectionTitle(title, "${items.size} offline local cards")
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
            Text(
                text = previewLine,
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
) {
    val context = LocalContext.current.applicationContext
    ChildHandoffLockContainer { isLocked ->
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = { TopAppBar(title = { Text(item?.title ?: "Activity") }) }) { innerPadding ->
                PageColumn(Modifier.padding(innerPadding)) {
                    if (item == null) {
                        Text("Activity not found.")
                    } else {
                        Text(item.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(item.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "Your play plan: ${settings.activityDuration.label} activity • ${settings.gameTimer.label} rounds",
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
                        Text("Safety tags: ${item.safetyTags.joinToString { it.displayTagLabel() }}")
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
fun AboutSafetyScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("About / Safety") }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text("Parent-led by design", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("KinPlay is for adults to guide short play sessions with children. Review the activity, clear the space, and supervise movement or materials.")
            Text("MVP privacy", fontWeight = FontWeight.Bold)
            Text("No accounts, analytics, ads, purchases, camera, microphone, contacts, location, or other sensitive Android permissions are requested.")
            Text("Child handoff lock limits", fontWeight = FontWeight.Bold)
            Text("The 3-second child handoff lock prevents accidental controls, Back, and exits inside KinPlay. It is not kiosk mode and cannot block Android system navigation, notifications, power controls, or another person leaving the app. KinPlay does not request device-owner, accessibility, or intrusive permissions.")
            Text("Content source", fontWeight = FontWeight.Bold)
            Text("The app ships seed content as a local JSON asset and does not need network access for the MVP flow.")
            Button(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
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
            return KinPlayItem(
                id = json.getString("id"),
                type = json.getString("type"),
                status = status,
                title = json.getString("title"),
                summary = json.getString("summary"),
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
    QUALITY_TIME("quality_time", "Quality Time", "Anywhere • 1:1 or group");

    companion object {
        val defaultGrid = entries.toList()
        fun fromId(id: String): QuickCategory? = entries.firstOrNull { it.id == id }
    }
}

fun List<KinPlayItem>.activeContent(): List<KinPlayItem> = filter { it.status == "active" }

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
    val firstStep = setupSteps.firstOrNull { it.isNotBlank() }?.trim() ?: return "${prefix}No setup needed"
    val availableCharacters = (maxCharacters - prefix.length).coerceAtLeast(1)
    if (firstStep.length <= availableCharacters) return prefix + firstStep
    if (availableCharacters == 1) return prefix + "…"

    val candidate = firstStep.take(availableCharacters - 1).trimEnd()
    val lastSpace = candidate.lastIndexOf(' ')
    val shortened = if (lastSpace > 0) candidate.substring(0, lastSpace) else candidate
    return prefix + shortened.trimEnd() + "…"
}

fun KinPlayItem.collapsedCardPreviewLines(): List<String> =
    listOf(summary, setupBurdenLabel(), setupPreviewLabel())

fun KinPlayItem.isMadLibsCollection(): Boolean = id == MAD_LIBS_COLLECTION_ID

private fun madLibsCollectionItem(stories: List<KinPlayItem>) = KinPlayItem(
    id = MAD_LIBS_COLLECTION_ID,
    type = "collection",
    status = "active",
    title = "Mad Libs",
    summary = "Open all ${stories.size} ready-to-fill silly stories.",
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

fun KinPlayItem.detailSections(): List<DetailSection> = buildList {
    add(DetailSection("Materials", listOf(if (materials.isEmpty()) "No materials needed." else materials.joinToString())))
    if (setupSteps.isNotEmpty()) add(DetailSection("Setup", setupSteps))
    if (playSteps.isNotEmpty()) add(DetailSection("Steps", playSteps))
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
