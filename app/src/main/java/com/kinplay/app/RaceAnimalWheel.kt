package com.kinplay.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinplay.wheel.SpinnerWheel
import com.kinplay.wheel.SpinnerWheelOption

val RACE_ANIMAL_OPTIONS = listOf(
    SpinnerWheelOption("kangaroo", "Kangaroo", "Use gentle kangaroo hops, or walk while making the pose."),
    SpinnerWheelOption("cheetah", "Cheetah", "Take quick small steps without sprinting."),
    SpinnerWheelOption("rabbit", "Rabbit", "Make small rabbit hops, or use small steps."),
    SpinnerWheelOption("frog", "Frog", "Squat and rise like a frog without leaping forward."),
    SpinnerWheelOption("turtle", "Turtle", "Take slow, steady steps and keep the path clear."),
    SpinnerWheelOption("penguin", "Penguin", "Waddle with small steps and relaxed arms."),
)

/** Compatibility list for the reviewed content and existing unit contracts. */
val RACE_ANIMAL_CHOICES = RACE_ANIMAL_OPTIONS.map { it.label }

@Composable
fun RaceAnimalWheel(enabled: Boolean = true) {
    var selectedAnimal by rememberSaveable { mutableStateOf(RACE_ANIMAL_OPTIONS.first().label) }
    var selectedInstruction by rememberSaveable { mutableStateOf(RACE_ANIMAL_OPTIONS.first().detail) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth().testTag("race-animal-wheel-card"),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Choose an animal", fontWeight = FontWeight.Bold)
            Text(
                "Tap the wheel or press Spin. It settles on an animal movement for this activity.",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                "Selected animal: $selectedAnimal",
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("race-animal-selected")
                    .semantics {
                        contentDescription = "Selected animal: $selectedAnimal"
                    },
                fontWeight = FontWeight.Bold,
            )
            SpinnerWheel(
                options = RACE_ANIMAL_OPTIONS,
                enabled = enabled,
                testTag = "race-animal-wheel",
                spinButtonTestTag = "race-animal-spin-button",
                nextButtonTestTag = "race-animal-next-button",
                showNextButton = true,
                onSelectionChanged = { selected ->
                    selectedAnimal = selected.label
                    selectedInstruction = selected.detail
                },
            )
            Text(
                "Next move: $selectedInstruction",
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("race-animal-instruction"),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
