package com.kinplay.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.json.JSONObject
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KinPlayApp() }
    }
}

private object Routes {
    const val Home = "home"
    const val QuickPlay = "quick_play"
    const val PickGame = "pick_game"
    const val MadLibs = "mad_libs"
    const val CalmDown = "calm_down"
    const val AboutSafety = "about_safety"
    const val Detail = "detail/{itemId}"
    fun detail(itemId: String) = "detail/$itemId"
}

private val Ink = Color(0xFF1F2A24)
private val MutedInk = Color(0xFF637067)
private val Canvas = Color(0xFFF7F2E8)
private val SurfaceWarm = Color(0xFFFFFCF4)
private val SurfaceLeaf = Color(0xFFE7F0E4)
private val Forest = Color(0xFF2F5D45)
private val ForestDark = Color(0xFF193A2C)
private val Gold = Color(0xFFE3A62F)

@Composable
fun KinPlayApp() {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Forest,
            onPrimary = Color.White,
            secondary = Gold,
            tertiary = ForestDark,
            surface = SurfaceWarm,
            background = Canvas,
            onSurface = Ink,
        ),
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val context = LocalContext.current
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
            NavHost(navController = navController, startDestination = Routes.Home) {
                composable(Routes.Home) { HomeScreen(contentPack, favoriteIds, recentIds, navController) }
                composable(Routes.QuickPlay) { QuickPlayScreen(contentPack, favoriteIds, recentIds, navController) }
                composable(Routes.PickGame) { ContentListScreen("Pick a Game", contentPack.pickGameItems(), favoriteIds, navController) }
                composable(Routes.MadLibs) { MadLibsScreen(contentPack.madLibs(), navController) }
                composable(Routes.CalmDown) { ContentListScreen("Calm Down", contentPack.calmDownItems(), favoriteIds, navController) }
                composable(Routes.AboutSafety) { AboutSafetyScreen(navController) }
                composable(
                    Routes.Detail,
                    arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
                ) { entry ->
                    val itemId = entry.arguments?.getString("itemId").orEmpty()
                    ActivityDetailScreen(
                        item = contentPack.items.firstOrNull { it.id == itemId },
                        isFavorite = itemId in favoriteIds,
                        onToggleFavorite = { persistFavorites(favoriteIds.toggleFavorite(itemId)) },
                        onMarkPlayed = { persistRecent(recentIds.withRecentFirst(itemId)) },
                        navController = navController,
                    )
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
                title = { Text("KinPlay") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Canvas, titleContentColor = Ink),
            )
        },
    ) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            HeroPanel(contentPack)
            val favoriteItems = contentPack.items.filter { it.id in favoriteIds }
            val recentItems = recentIds.mapNotNull { id -> contentPack.items.firstOrNull { it.id == id } }
            if (favoriteItems.isNotEmpty()) {
                SectionTitle("Favorites", "Saved picks for faster family starts")
                favoriteItems.take(3).forEach { item -> ContentCard(item, favoriteIds, navController) }
            }
            if (recentItems.isNotEmpty()) {
                SectionTitle("Recently played", "Return to what already worked")
                recentItems.take(3).forEach { item -> ContentCard(item, favoriteIds, navController) }
            }
            SectionTitle("Start playing", "Offline, parent-led choices for ages 2–8")
            HomeButton("Pick For Me", "Randomly choose a quick local activity") { navController.navigate(Routes.QuickPlay) }
            HomeButton("Pick a Game", "Browse the full activity library") { navController.navigate(Routes.PickGame) }
            HomeButton("Mad Libs", "Fill prompts and reveal a silly story") { navController.navigate(Routes.MadLibs) }
            HomeButton("Calm Down", "Quiet activities for transitions") { navController.navigate(Routes.CalmDown) }
            HomeButton("About / Safety", "Parent-led safety and privacy notes") { navController.navigate(Routes.AboutSafety) }
        }
    }
}

@Composable
fun PageColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Canvas)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
fun HeroPanel(contentPack: ContentPack) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ForestDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("KinPlay", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                "Professional, offline-first family play for parent-led moments: quick games, calm resets, creative prompts, and read-aloud silliness.",
                color = Color(0xFFE7EFE8),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatPill("${contentPack.activeItems().size}", "active")
                StatPill("${contentPack.activities().size}", "activities")
                StatPill("${contentPack.madLibs().size}", "stories")
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MutedInk)
        }
    }
}

@Composable
fun StatPill(value: String, label: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF406E55)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
            Text(label, color = Color(0xFFD7E7D9), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun DetailPill(text: String) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceLeaf), shape = RoundedCornerShape(14.dp)) {
        Text(text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = MaterialTheme.typography.bodySmall, color = ForestDark, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HomeButton(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWarm),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = Ink)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MutedInk)
            }
            Text("›", style = MaterialTheme.typography.headlineSmall, color = Forest, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SeedCard(contentPack: ContentPack) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceLeaf), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Local seed pack", fontWeight = FontWeight.Bold, color = ForestDark)
            Text(contentPack.title, color = Ink)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailPill("${contentPack.activeItems().size} active")
                DetailPill("${contentPack.activities().size} activities")
                DetailPill("${contentPack.madLibs().size} stories")
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
    Scaffold(topBar = { TopAppBar(title = { Text("Quick Play") }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text("Quick Play", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("A short, safe, local-content activity selected without network access.")
            if (quickPick == null) {
                Text("No eligible Quick Play item found yet.")
            } else {
                ContentCard(quickPick, favoriteIds, navController)
                Button(onClick = { navController.navigate(Routes.detail(quickPick.id)) }) { Text("Start this activity") }
            }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentListScreen(title: String, items: List<KinPlayItem>, favoriteIds: Set<String>, navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            SectionTitle(title, "${items.size} offline local cards")
            if (items.isEmpty()) {
                Text("No matching local content found.")
            }
            items.forEach { item -> ContentCard(item, favoriteIds, navController) }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back home") }
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
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWarm),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(title, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.weight(1f))
            }
            CompactCardDetails(item, navController)
        }
    }
}

@Composable
fun CompactCardDetails(item: KinPlayItem, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(item.summary, style = MaterialTheme.typography.bodySmall, color = Ink)
            Text(
                "Materials: ${if (item.materials.isEmpty()) "none" else item.materials.joinToString()}",
                style = MaterialTheme.typography.bodySmall,
            )
            if (item.type == "mad_libs") {
                Text("Mad Libs fields: ${item.madLibsFields.size}", style = MaterialTheme.typography.bodySmall)
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            DetailPill("${item.durationMinutes} min")
            DetailPill(item.displayAgeRange())
            Text(item.energyLevel, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End, color = MutedInk)
            if (item.type != "mad_libs") {
                Button(onClick = { navController.navigate(Routes.detail(item.id)) }) { Text("Open") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    item: KinPlayItem?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onMarkPlayed: () -> Unit,
    navController: NavController,
) {
    Scaffold(topBar = { TopAppBar(title = { Text(item?.title ?: "Activity") }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            if (item == null) {
                Text("Activity not found.")
            } else {
                Text(item.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
                Text(item.summary, color = MutedInk)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailPill(item.displayAgeRange())
                    DetailPill("${item.durationMinutes} min")
                    DetailPill(item.energyLevel)
                }
                item.detailSections().forEach { section ->
                    SectionList(section.title, section.lines)
                }
                if (item.parentNotes.isNotBlank()) {
                    Text("Parent note", fontWeight = FontWeight.Bold)
                    Text(item.parentNotes)
                }
                Button(onClick = onMarkPlayed) { Text("Mark played") }
                OutlinedButton(onClick = onToggleFavorite) { Text(if (isFavorite) "Remove favorite" else "Add favorite") }
                Text("Safety tags: ${item.safetyTags.joinToString { it.displayTagLabel() }}")
            }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back") }
        }
    }
}

@Composable
fun InfoPanel(title: String, body: String) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceWarm), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Ink)
            Text(body, color = MutedInk)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MadLibsScreen(items: List<KinPlayItem>, navController: NavController) {
    var selected by remember(items) { mutableStateOf(items.firstOrNull()) }
    val answers = remember(selected?.id) { mutableStateMapOf<String, String>() }
    var revealed by remember(selected?.id) { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("Mad Libs") }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text("Mad Libs", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            if (items.isEmpty()) {
                Text("No Mad Libs templates found.")
            } else {
                Text("Choose a story")
                items.forEach { item ->
                    OutlinedButton(onClick = {
                        selected = item
                        answers.clear()
                        revealed = false
                    }) { Text(item.title) }
                }
                val story = selected
                if (story != null) {
                    Text(story.title, fontWeight = FontWeight.Bold)
                    story.madLibsFields.forEach { field ->
                        OutlinedTextField(
                            value = answers[field.key].orEmpty(),
                            onValueChange = { answers[field.key] = it },
                            label = { Text(field.label) },
                            placeholder = { Text(field.example) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )
                    }
                    val allFilled = story.madLibsFields.all { answers[it.key].orEmpty().isNotBlank() }
                    Button(onClick = { revealed = true }, enabled = allFilled) { Text("Reveal story") }
                    if (revealed) {
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4EA)), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Your story", fontWeight = FontWeight.Bold)
                                Text(story.renderMadLib(answers))
                            }
                        }
                    }
                }
            }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back home") }
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
    fun activities() = activeItems().filter { it.type == "activity" }
    fun madLibs() = activeItems().filter { it.type == "mad_libs" }
    fun pickGameItems() = items.itemsForMode("pick_a_game").filter { it.type != "mad_libs" }
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
            return KinPlayItem(
                id = json.getString("id"),
                type = json.getString("type"),
                status = json.getString("status"),
                title = json.getString("title"),
                summary = json.getString("summary"),
                modes = json.stringList("modes"),
                minAge = json.getInt("minAge"),
                maxAge = json.getInt("maxAge"),
                durationMinutes = json.getInt("durationMinutes"),
                energyLevel = json.getString("energyLevel"),
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

fun List<KinPlayItem>.activeContent(): List<KinPlayItem> = filter { it.status == "active" }

fun List<KinPlayItem>.itemsForMode(mode: String): List<KinPlayItem> = activeContent().filter { mode in it.modes }

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

fun String.displayTagLabel(): String =
    split('_')
        .filter { it.isNotBlank() }
        .joinToString(" ") { it.lowercase() }
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun KinPlayItem.detailSections(): List<DetailSection> = buildList {
    add(DetailSection("Materials", listOf(if (materials.isEmpty()) "No materials needed." else materials.joinToString())))
    if (setupSteps.isNotEmpty()) add(DetailSection("Setup", setupSteps))
    if (playSteps.isNotEmpty()) add(DetailSection("Steps", playSteps))
    if (promptText.isNotBlank()) add(DetailSection("Prompt", listOf(promptText)))
    if (followUps.isNotEmpty()) add(DetailSection("Follow-up questions", followUps))
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
