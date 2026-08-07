package com.kinplay.wheellab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinplay.wheel.SpinnerWheel
import com.kinplay.wheel.SpinnerWheelOption

class WheelLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WheelLabApp() }
    }
}

data class WheelLabDemo(
    val id: String,
    val title: String,
    val description: String,
    val options: List<SpinnerWheelOption>,
)

val WHEEL_LAB_DEMOS = listOf(
    WheelLabDemo(
        id = "animals",
        title = "Animal moves",
        description = "The KinPlay production dataset with a compact result instruction.",
        options = listOf(
            SpinnerWheelOption("kangaroo", "Kangaroo", "Gentle hops or a walking pose."),
            SpinnerWheelOption("cheetah", "Cheetah", "Quick small steps without sprinting."),
            SpinnerWheelOption("rabbit", "Rabbit", "Small hops or small steps."),
            SpinnerWheelOption("frog", "Frog", "Squat and rise without leaping."),
            SpinnerWheelOption("turtle", "Turtle", "Slow, steady steps."),
            SpinnerWheelOption("penguin", "Penguin", "Waddle with small steps."),
        ),
    ),
    WheelLabDemo(
        id = "colors",
        title = "Color choices",
        description = "An eight-sector wheel for checking dense wedge spacing and contrast.",
        options = listOf(
            SpinnerWheelOption("red", "Red", "Find something red."),
            SpinnerWheelOption("orange", "Orange", "Find something orange."),
            SpinnerWheelOption("yellow", "Yellow", "Find something yellow."),
            SpinnerWheelOption("green", "Green", "Find something green."),
            SpinnerWheelOption("blue", "Blue", "Find something blue."),
            SpinnerWheelOption("purple", "Purple", "Find something purple."),
            SpinnerWheelOption("pink", "Pink", "Find something pink."),
            SpinnerWheelOption("white", "White", "Find something white."),
        ),
    ),
    WheelLabDemo(
        id = "long_labels",
        title = "Long labels",
        description = "A text-fitting and accessibility page for labels that need wrapping.",
        options = listOf(
            SpinnerWheelOption("pillow", "Build a pillow path", "Make a safe path with flat pillows."),
            SpinnerWheelOption("quiet", "Tell a quiet story", "Take turns adding one gentle sentence."),
            SpinnerWheelOption("animal", "Copy an animal movement", "Choose a safe movement and copy it."),
            SpinnerWheelOption("color", "Find a family color", "Point to a color everyone can see."),
            SpinnerWheelOption("clap", "Create a clap pattern", "Use a short clap-and-pause pattern."),
            SpinnerWheelOption("stretch", "Do a gentle stretch", "Reach slowly and stop if anything hurts."),
        ),
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelLabApp() {
    var activeDemoId by rememberSaveable { mutableStateOf(WHEEL_LAB_DEMOS.first().id) }
    val activeDemo = WHEEL_LAB_DEMOS.firstOrNull { it.id == activeDemoId } ?: WHEEL_LAB_DEMOS.first()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Wheel Lab") })
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .testTag("wheel-lab-page-${activeDemo.id}"),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text("Interactive spinner playground", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Offline test surface: tap the wheel, press Spin, press Next, and compare datasets before changing KinPlay production UI.")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .testTag("wheel-lab-demo-picker"),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        WHEEL_LAB_DEMOS.forEach { demo ->
                            FilterChip(
                                selected = demo.id == activeDemo.id,
                                onClick = { activeDemoId = demo.id },
                                label = { Text(demo.title) },
                                modifier = Modifier.testTag("wheel-lab-demo-${demo.id}"),
                            )
                        }
                    }
                    WheelLabDemoPage(activeDemo)
                }
            }
        }
    }
}

@Composable
private fun WheelLabDemoPage(demo: WheelLabDemo) {
    var selectedLabel by rememberSaveable(demo.id) { mutableStateOf(demo.options.first().label) }
    var selectedDetail by rememberSaveable(demo.id) { mutableStateOf(demo.options.first().detail) }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("wheel-lab-card-${demo.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(demo.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(demo.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            SpinnerWheel(
                options = demo.options,
                testTag = "wheel-lab-wheel-${demo.id}",
                spinButtonTestTag = "wheel-lab-spin-${demo.id}",
                nextButtonTestTag = "wheel-lab-next-${demo.id}",
                showNextButton = true,
                onSelectionChanged = { selected ->
                    selectedLabel = selected.label
                    selectedDetail = selected.detail
                },
            )
            Text(
                "Current choice: $selectedLabel",
                modifier = Modifier.testTag("wheel-lab-choice-${demo.id}"),
                fontWeight = FontWeight.Bold,
            )
            Text(selectedDetail, modifier = Modifier.testTag("wheel-lab-detail-${demo.id}"))
        }
    }
}
