package com.kinplay.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun KinPlayApp() {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF4F6F52),
            secondary = Color(0xFFF0B84F),
            surface = Color(0xFFFFFBF0),
            background = Color(0xFFFFFBF0),
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
                composable(Routes.QuickPlay) { QuickPlayScreen(contentPack, navController) }
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFF0C2)),
            )
        },
    ) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text("Guided family play", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Short, safe, parent-led activities for kids ages 2–8. Offline-first, no accounts, no ads, and no data collection in the MVP.")
            SeedCard(contentPack)
            HomeButton("Quick Play", "Start a short no-prep activity") { navController.navigate(Routes.QuickPlay) }
            HomeButton("Pick a Game", "Browse activity cards") { navController.navigate(Routes.PickGame) }
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
            .background(Color(0xFFFFFBF0))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content,
    )
}

@Composable
fun HomeButton(title: String, subtitle: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SeedCard(contentPack: ContentPack) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4EA)), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Local seed pack", fontWeight = FontWeight.Bold)
            Text(contentPack.title)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("${contentPack.activeItems().size} active")
                Text("${contentPack.activities().size} activities")
                Text("${contentPack.madLibs().size} Mad Libs")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPlayScreen(contentPack: ContentPack, navController: NavController) {
    val quickPick = remember(contentPack.items) { contentPack.quickPlayPick() }
    Scaffold(topBar = { TopAppBar(title = { Text("Quick Play") }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text("Quick Play", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("A short, safe, local-content activity selected without network access.")
            if (quickPick == null) {
                Text("No eligible Quick Play item found yet.")
            } else {
                ContentCard(quickPick, navController)
                Button(onClick = { navController.navigate(Routes.detail(quickPick.id)) }) { Text("Start this activity") }
            }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentListScreen(title: String, items: List<KinPlayItem>, navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            if (items.isEmpty()) {
                Text("No matching local content found.")
            }
            items.forEach { item -> ContentCard(item, navController) }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
    }
}

@Composable
fun ContentCard(item: KinPlayItem, navController: NavController) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0C2)), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(item.title, fontWeight = FontWeight.Bold)
            Text(item.summary)
            Text("Ages ${item.minAge}–${item.maxAge} • ${item.durationMinutes} min • ${item.energyLevel}")
            Text("Materials: ${if (item.materials.isEmpty()) "none" else item.materials.joinToString()}")
            if (item.type == "mad_libs") {
                Text("Mad Libs fields: ${item.madLibsFields.size}")
            } else {
                Button(onClick = { navController.navigate(Routes.detail(item.id)) }) { Text("Open") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(item: KinPlayItem?, navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text(item?.title ?: "Activity") }) }) { innerPadding ->
        PageColumn(Modifier.padding(innerPadding)) {
            if (item == null) {
                Text("Activity not found.")
            } else {
                Text(item.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(item.summary)
                Text("Ages ${item.minAge}–${item.maxAge} • ${item.durationMinutes} min • ${item.energyLevel}")
                Text("Materials", fontWeight = FontWeight.Bold)
                Text(if (item.materials.isEmpty()) "No materials needed." else item.materials.joinToString())
                SectionList("Setup", item.setupSteps)
                SectionList("Steps", item.playSteps)
                if (item.parentNotes.isNotBlank()) {
                    Text("Parent note", fontWeight = FontWeight.Bold)
                    Text(item.parentNotes)
                }
                SectionList("Replay variations", item.variations)
                Text("Safety tags: ${item.safetyTags.joinToString()}")
            }
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Back") }
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
            Text("No accounts, analytics, ads, purchases, camera, microphone, contacts, or location permission are requested.")
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
    fun activeItems() = items.filter { it.status == "active" }
    fun activities() = activeItems().filter { it.type == "activity" }
    fun madLibs() = activeItems().filter { it.type == "mad_libs" }
    fun pickGameItems() = activeItems().filter { "pick_a_game" in it.modes && it.type != "mad_libs" }
    fun calmDownItems() = activeItems().filter { "calm_down" in it.modes || "calming" in it.safetyTags }
    fun quickPlayPick(): KinPlayItem? = activeItems()
        .filter { "quick_play" in it.modes && it.type != "mad_libs" }
        .sortedWith(compareBy<KinPlayItem> { it.materials.isNotEmpty() }.thenBy { it.durationMinutes }.thenBy { it.title })
        .firstOrNull()

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
    val materials: List<String>,
    val safetyTags: List<String>,
    val setupSteps: List<String>,
    val playSteps: List<String>,
    val parentNotes: String,
    val variations: List<String>,
    val promptText: String,
    val followUps: List<String>,
    val madLibsFields: List<MadLibField>,
    val madLibsTemplate: String,
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

fun JSONObject.stringList(name: String): List<String> {
    val array = optJSONArray(name) ?: return emptyList()
    return List(array.length()) { index -> array.getString(index) }
}
