package com.kinplay.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.abs

val RACE_ANIMAL_CHOICES = listOf("Kangaroo", "Cheetah", "Rabbit", "Frog", "Turtle", "Penguin")

@Composable
fun RaceAnimalWheel(enabled: Boolean = true) {
    val choices = RACE_ANIMAL_CHOICES
    val repeatedChoices = remember(choices) { List(240) { choices[it % choices.size] } }
    val initialIndex = repeatedChoices.size / 2 - (repeatedChoices.size / 2) % choices.size
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val scope = rememberCoroutineScope()
    var selectedAnimal by rememberSaveable { mutableStateOf(choices.first()) }

    LaunchedEffect(listState) {
        snapshotCenteredAnimal(listState, choices).collectLatest { selectedAnimal = it }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth().testTag("race-animal-wheel-card"),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Choose an animal", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text("Flick the wheel. It settles on the animal for the next round.", style = MaterialTheme.typography.bodySmall)
            Text(
                "Selected animal: $selectedAnimal",
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("race-animal-selected")
                    .semantics { contentDescription = "Selected animal: $selectedAnimal" },
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            )
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val cardWidth = (maxWidth * 0.62f).coerceIn(92.dp, 164.dp)
                LazyRow(
                    state = listState,
                    flingBehavior = flingBehavior,
                    contentPadding = PaddingValues(horizontal = maxWidth / 2 - cardWidth / 2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("race-animal-wheel")
                        .semantics { contentDescription = "Flickable animal selection wheel" },
                ) {
                    itemsIndexed(repeatedChoices) { index, animal ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth(0.62f)
                                .clickable(enabled = enabled) {
                                    scope.launch { listState.animateScrollToItem(index) }
                                }
                                .testTag("race-animal-option-$animal-$index"),
                            shape = RoundedCornerShape(18.dp),
                        ) {
                            Text(
                                animal,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            )
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        scope.launch {
                            val current = listState.firstVisibleItemIndex
                            listState.animateScrollToItem(current + choices.size * 3)
                        }
                    },
                    enabled = enabled,
                    modifier = Modifier.testTag("race-animal-spin-button"),
                ) { Text("Spin") }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val current = listState.firstVisibleItemIndex
                            listState.animateScrollToItem((current + choices.size).coerceAtMost(repeatedChoices.lastIndex))
                        }
                    },
                    enabled = enabled,
                    modifier = Modifier.testTag("race-animal-next-button"),
                ) { Text("Next") }
            }
        }
    }
}

private fun snapshotCenteredAnimal(
    state: androidx.compose.foundation.lazy.LazyListState,
    choices: List<String>,
) = snapshotFlow { state.isScrollInProgress }
    .distinctUntilChanged()
    .filter { !it }
    .map {
        val layout = state.layoutInfo
        val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2
        val centered = layout.visibleItemsInfo.minByOrNull { item ->
            abs((item.offset + item.size / 2) - center)
        }
        centered?.let { choices[it.index % choices.size] } ?: choices.first()
    }
