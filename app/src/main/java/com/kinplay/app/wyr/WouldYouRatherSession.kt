package com.kinplay.app.wyr

/**
 * Small application service used by the Compose surface. Every draw is saved
 * before it is exposed to the UI, so process death cannot replay the visible
 * prompt on the next launch.
 */
class WouldYouRatherSession(
    library: WouldYouRatherLibrary,
    private val store: WouldYouRatherStore,
    seed: Long,
) {
    val categories: List<WouldYouRatherCategory> = library.categories.sortedBy { it.order }
    private val deck = WouldYouRatherDeck(library, seed, store.load())

    fun nextPrompt(categoryId: String): WouldYouRatherPrompt {
        val prompt = deck.draw(categoryId)
        store.save(deck.snapshot())
        return prompt
    }
}
