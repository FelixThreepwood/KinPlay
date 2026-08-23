package com.devlab

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.devlab.feedback.FeedbackOverlay
import com.kinplay.wheel.SpinnerWheel
import com.kinplay.wheel.SpinnerWheelOption

class DevLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            show(WindowInsetsCompat.Type.systemBars())
        }
        setContent { DevLabApp() }
    }
}

data class DevLabDemo(
    val id: String,
    val title: String,
    val description: String,
    val options: List<SpinnerWheelOption>,
)

val DEV_LAB_DEMOS = listOf(
    DevLabDemo(
        id = "animals",
        title = "Animal moves",
        description = "A generated animal stream with a compact result instruction.",
        options = listOf(
            SpinnerWheelOption("kangaroo", "Kangaroo", "Gentle hops or a walking pose."),
            SpinnerWheelOption("cheetah", "Cheetah", "Quick small steps without sprinting."),
            SpinnerWheelOption("rabbit", "Rabbit", "Small hops or small steps."),
            SpinnerWheelOption("frog", "Frog", "Squat and rise without leaping."),
            SpinnerWheelOption("turtle", "Turtle", "Slow, steady steps."),
            SpinnerWheelOption("penguin", "Penguin", "Waddle with small steps."),
        ),
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevLabApp() {
    var activeDemoId by rememberSaveable { mutableStateOf(DEV_LAB_DEMOS.first().id) }
    val activeDemo = DEV_LAB_DEMOS.firstOrNull { it.id == activeDemoId } ?: DEV_LAB_DEMOS.first()
    val context = LocalContext.current

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(DEV_LAB_APP_NAME) })
                    },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .navigationBarsPadding()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                            .testTag("dev-lab-page-${activeDemo.id}"),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text("Interactive development playground", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("Offline test surface: tap the reel, press Spin, and compare datasets before changing production UI.")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .testTag("dev-lab-demo-picker"),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            DEV_LAB_DEMOS.forEach { demo ->
                                FilterChip(
                                    selected = demo.id == activeDemo.id,
                                    onClick = { activeDemoId = demo.id },
                                    label = { Text(demo.title) },
                                    modifier = Modifier.testTag("dev-lab-demo-${demo.id}"),
                                )
                            }
                        }
                        DevLabDemoPage(activeDemo)
                        DevLabAboutSection()
                    }
                }
                FeedbackOverlay(
                    context = context,
                    screen = "dev_lab/${activeDemo.id}",
                    contentId = activeDemo.id,
                    contentTitle = activeDemo.title,
                )
            }
        }
    }
}

@Composable
private fun DevLabAboutSection() {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("dev-lab-about"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("About Dev Lab", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Version ${BuildConfig.VERSION_NAME}")
            Text("Release notes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Feedback forms now preserve active demo context")
            Text("Feedback lists retain state during navigation")
            Text("0.2.3: Keep Animal moves and remove extra demos")
            Text("System navigation remains visible at launch")
        }
    }
}

@Composable
private fun DevLabDemoPage(demo: DevLabDemo) {
    var selectedLabel by rememberSaveable(demo.id) { mutableStateOf(demo.options.first().label) }
    var selectedDetail by rememberSaveable(demo.id) { mutableStateOf(demo.options.first().detail) }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("dev-lab-card-${demo.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(demo.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(demo.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val onSelectionChanged: (SpinnerWheelOption) -> Unit = { selected ->
                selectedLabel = selected.label
                selectedDetail = selected.detail
            }
            if (demo.id == "animals") {
                VerticalAnimalReel(
                    catalog = demo.options,
                    testTag = "dev-lab-animal-reel-${demo.id}",
                    spinButtonTestTag = "dev-lab-spin-${demo.id}",
                    onSelectionChanged = onSelectionChanged,
                )
            } else {
                SpinnerWheel(
                    options = demo.options,
                    testTag = "dev-lab-wheel-${demo.id}",
                    spinButtonTestTag = "dev-lab-spin-${demo.id}",
                    nextButtonTestTag = "dev-lab-next-${demo.id}",
                    showNextButton = true,
                    onSelectionChanged = onSelectionChanged,
                )
            }
            Text(
                "Current choice: $selectedLabel",
                modifier = Modifier.testTag("dev-lab-choice-${demo.id}"),
                fontWeight = FontWeight.Bold,
            )
            Text(selectedDetail, modifier = Modifier.testTag("dev-lab-detail-${demo.id}"))
        }
    }
}
