package com.kinplay.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PaperAirplaneInstructions(item: KinPlayItem, enabled: Boolean) {
    if (item.paperAirplaneModels.isEmpty()) return
    var selectedModelId by rememberSaveable(item.id) { mutableStateOf(item.paperAirplaneModels.first().id) }
    val model = item.paperAirplaneModels.firstOrNull { it.id == selectedModelId } ?: item.paperAirplaneModels.first()
    val asset = item.visualAssets.firstOrNull { it.id == model.diagramAsset }
        ?: item.visualAssets.firstOrNull { it.resource == model.diagramAsset }
    Card(
        modifier = Modifier.fillMaxWidth().testTag("paper-airplane-instructions"),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Paper airplanes", fontWeight = FontWeight.Bold)
            Text("Choose one of the two reviewed models, then follow the diagram and the numbered steps.")
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item.paperAirplaneModels.forEach { option ->
                    FilterChip(
                        selected = option.id == model.id,
                        onClick = { selectedModelId = option.id },
                        enabled = enabled,
                        label = { Text(option.name) },
                        modifier = Modifier.testTag("paper-airplane-model-${option.id}"),
                    )
                }
            }
            Text(model.shapeDescription, fontWeight = FontWeight.Bold)
            if (asset != null) {
                val resourceId = paperAirplaneResource(asset.resource)
                if (resourceId != 0) {
                    Image(
                        painter = painterResource(resourceId),
                        contentDescription = asset.altText,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth().semantics { contentDescription = asset.altText },
                    )
                }
            }
            model.steps.forEachIndexed { index, step ->
                Text("${index + 1}. $step", modifier = Modifier.testTag("paper-airplane-step-${index + 1}"))
            }
        }
    }
}

private fun paperAirplaneResource(resource: String): Int = when (resource) {
    "paper_airplane_basic_classic_dart" -> R.drawable.paper_airplane_basic_classic_dart
    "paper_airplane_glide_trickster" -> R.drawable.paper_airplane_glide_trickster
    else -> 0
}

@Composable
fun BrainMovementInstructions(item: KinPlayItem, enabled: Boolean) {
    val asset = item.visualAssets.firstOrNull() ?: return
    val resourceId = when (asset.resource) {
        "brain_movement_activities" -> R.drawable.brain_movement_activities
        else -> 0
    }
    if (resourceId == 0) return
    Card(
        modifier = Modifier.fillMaxWidth().testTag("brain-movement-instructions"),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Picture guide", fontWeight = FontWeight.Bold)
            Image(
                painter = painterResource(resourceId),
                contentDescription = asset.altText,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = asset.altText },
            )
            Text("Use the seated or two-foot version whenever balance or space is uncertain.")
        }
    }
}
