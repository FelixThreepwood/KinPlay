package com.kinplay.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.json.JSONObject

const val CHARADES_ITEM_ID = "family_charades_animals"
val CHARADES_CATEGORIES = listOf("animals", "activities", "objects")

data class CharadesCard(
    val id: String,
    val category: String,
    val prompt: String,
)

data class CharadesDeck(val cards: List<CharadesCard>) {
    fun cardsIn(category: String): List<CharadesCard> = cards.filter { it.category == category }

    companion object {
        val EMPTY = CharadesDeck(emptyList())

        fun fromJson(json: JSONObject): CharadesDeck {
            val cards = json.getJSONArray("cards").let { array ->
                (0 until array.length()).map { index ->
                    val card = array.getJSONObject(index)
                    CharadesCard(
                        id = card.getString("id"),
                        category = card.getString("category"),
                        prompt = card.getString("prompt"),
                    )
                }
            }
            require(cards.size == 120) { "Charades library must contain 120 cards" }
            require(cards.map(CharadesCard::id).toSet().size == cards.size) { "Charades card IDs must be unique" }
            require(cards.groupingBy(CharadesCard::category).eachCount().values.all { it == 40 }) {
                "Charades library must contain 40 cards per category"
            }
            return CharadesDeck(cards)
        }
    }
}

@Composable
fun CharadesCardsPanel(enabled: Boolean) {
    val context = LocalContext.current
    var deck by remember { mutableStateOf(CharadesDeck.EMPTY) }
    var category by rememberSaveable { mutableStateOf(CHARADES_CATEGORIES.first()) }
    var cardIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        deck = runCatching {
            val json = context.assets.open("charades_v1.json").bufferedReader().use { it.readText() }
            CharadesDeck.fromJson(JSONObject(json))
        }.getOrElse { CharadesDeck.EMPTY }
    }

    val categoryCards = deck.cardsIn(category)
    val currentCard = categoryCards.getOrNull(cardIndex % categoryCards.size.coerceAtLeast(1))
    Card(
        modifier = Modifier.fillMaxWidth().testTag("charades-library"),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Charades cards", fontWeight = FontWeight.Bold)
            Text("120 reviewed cards: 40 animals, 40 activities, and 40 objects.")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CHARADES_CATEGORIES.forEach { option ->
                    FilterChip(
                        selected = option == category,
                        onClick = {
                            category = option
                            cardIndex = 0
                        },
                        enabled = enabled,
                        label = { Text(option.replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.testTag("charades-category-$option"),
                    )
                }
            }
            if (currentCard == null) {
                Text("Charades cards are loading.")
            } else {
                Text("${currentCard.prompt.replaceFirstChar(Char::uppercase)}", fontWeight = FontWeight.Bold, modifier = Modifier.testTag("charades-card-prompt"))
                Text("Card ${(cardIndex % categoryCards.size) + 1} of ${categoryCards.size}")
                Button(
                    onClick = { cardIndex = (cardIndex + 1) % categoryCards.size },
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth().testTag("charades-next-card"),
                ) { Text("Next card") }
            }
        }
    }
}
