package com.kinplay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.json.JSONObject

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
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Routes.Home) {
                composable(Routes.Home) { HomeScreen(navController) }
                composable(Routes.QuickPlay) { PlaceholderScreen("Quick Play", "Selects an active local content card without network access.", navController) }
                composable(Routes.PickGame) { PlaceholderScreen("Pick a Game", "Browse by age, duration, energy, materials, and mode.", navController) }
                composable(Routes.MadLibs) { PlaceholderScreen("Mad Libs", "Collects required word prompts, then reveals a silly family-safe story.", navController) }
                composable(Routes.CalmDown) { PlaceholderScreen("Calm Down", "Quiet and calming activities for transitions or bedtime-adjacent moments.", navController) }
                composable(Routes.AboutSafety) { AboutSafetyScreen(navController) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val seedSummary = rememberSeedSummary()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KinPlay") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFF0C2)),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFFBF0))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("Guided family play", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Short, safe, parent-led activities for kids ages 2–8. Offline-first, no accounts, no ads, and no data collection in the MVP.")
            SeedCard(seedSummary)
            HomeButton("Quick Play", "Start a short no-prep activity") { navController.navigate(Routes.QuickPlay) }
            HomeButton("Pick a Game", "Browse activity cards") { navController.navigate(Routes.PickGame) }
            HomeButton("Mad Libs", "Fill prompts and reveal a silly story") { navController.navigate(Routes.MadLibs) }
            HomeButton("Calm Down", "Quiet activities for transitions") { navController.navigate(Routes.CalmDown) }
            HomeButton("About / Safety", "Parent-led safety and privacy notes") { navController.navigate(Routes.AboutSafety) }
        }
    }
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
fun SeedCard(summary: SeedSummary) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4EA)), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Local seed pack", fontWeight = FontWeight.Bold)
            Text(summary.title)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("${summary.activeItems} active")
                Text("${summary.activityCount} activities")
                Text("${summary.madLibCount} Mad Libs")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(title: String, description: String, navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(description)
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0C2))) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("MVP placeholder", fontWeight = FontWeight.Bold)
                    Text("Navigation is wired. The next implementation pass can connect this screen to content filtering, session selection, and form state.")
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutSafetyScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("About / Safety") }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Parent-led by design", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("KinPlay is for adults to guide short play sessions with children. Review the activity, clear the space, and supervise movement or materials.")
            Text("MVP privacy", fontWeight = FontWeight.Bold)
            Text("No accounts, analytics, ads, purchases, camera, microphone, contacts, or location permission are requested.")
            Text("Content source", fontWeight = FontWeight.Bold)
            Text("The app ships seed content as a local JSON asset and does not need network access for the placeholder flow.")
            Button(onClick = { navController.popBackStack() }) { Text("Back home") }
        }
    }
}

@Composable
fun rememberSeedSummary(): SeedSummary {
    val context = LocalContext.current
    var summary by remember { mutableStateOf(SeedSummary()) }
    LaunchedEffect(Unit) {
        summary = runCatching {
            val json = context.assets.open("kinplay_seed_v1.json").bufferedReader().use { it.readText() }
            val root = JSONObject(json)
            val items = root.getJSONArray("items")
            var active = 0
            var activities = 0
            var madLibs = 0
            for (index in 0 until items.length()) {
                val item = items.getJSONObject(index)
                if (item.optString("status") == "active") active += 1
                when (item.optString("type")) {
                    "activity" -> activities += 1
                    "mad_libs" -> madLibs += 1
                }
            }
            SeedSummary(
                title = root.optString("title", "KinPlay Seed Pack"),
                activeItems = active,
                activityCount = activities,
                madLibCount = madLibs,
            )
        }.getOrElse { SeedSummary(title = "Seed pack not loaded") }
    }
    return summary
}

data class SeedSummary(
    val title: String = "Loading seed pack...",
    val activeItems: Int = 0,
    val activityCount: Int = 0,
    val madLibCount: Int = 0,
)
