package com.kinplay.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

fun contentVisualResource(resource: String): Int = when (resource) {
    "tiny_monster_visual_guide" -> R.drawable.tiny_monster_visual_guide
    "brain_movement_activities" -> R.drawable.brain_movement_activities
    "paper_airplane_basic_classic_dart" -> R.drawable.paper_airplane_basic_classic_dart
    "paper_airplane_glide_trickster" -> R.drawable.paper_airplane_glide_trickster
    else -> 0
}

@Composable
fun TinyMonsterVisualGuide(item: KinPlayItem) {
    if (item.id != "timed_drawing_tiny_monster") return
    val asset = item.visualAssets.firstOrNull { it.resource == "tiny_monster_visual_guide" } ?: return
    val resourceId = contentVisualResource(asset.resource)
    if (resourceId == 0) return
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("tiny-monster-visual-guide"),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("Picture guide", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Image(
                painter = painterResource(resourceId),
                contentDescription = asset.altText,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = asset.altText },
            )
        }
    }
}
