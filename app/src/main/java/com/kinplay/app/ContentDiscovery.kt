package com.kinplay.app

/** Level 1 navigation groups. Level 2 contains the reviewed activities in each group. */
enum class GameTypeGroup(
    val id: String,
    val label: String,
    val description: String,
) {
    WORD_GAMES("word_games", "Word games", "Build stories, words, and silly language together."),
    GUESSING_GAMES("guessing_games", "Guessing games", "Use clues, questions, and careful noticing to guess."),
    ARTS_AND_MAKING("arts_and_making", "Arts and making", "Draw, fold, sort, and make something together."),
    MOVE_AND_PLAY("move_and_play", "Move and play", "Use safe movement games to change the room's energy."),
    PRETEND_AND_STORIES("pretend_and_stories", "Pretend and stories", "Invent characters, scenes, and family stories together."),
    BRAIN_AND_MOVEMENT("brain_and_movement", "Brain and movement", "Try gentle coordination and cross-body movement activities."),
    ;

    companion object {
        fun fromId(id: String): GameTypeGroup? = entries.firstOrNull { it.id == id }
    }
}

const val GAME_TYPE_CARD_DEFAULT_EXPANDED = false

data class ContentFormatGroup(
    val id: String,
    val label: String,
    val description: String,
)

private val contentFormatGroups = listOf(
    ContentFormatGroup("story_circle", "Story circle", "One story format with character, news, and word variations."),
    ContentFormatGroup("notice_and_sort", "Notice and sort", "One observation format with color, shape, outdoor, and sorting variations."),
    ContentFormatGroup("animal_play", "Animal play", "One animal format with movement, guessing, whisper, and rescue variations."),
)

private val contentFormatGroupsById = contentFormatGroups.associateBy(ContentFormatGroup::id)

fun KinPlayItem.contentFormatGroup(): ContentFormatGroup? = formatGroupId?.let(contentFormatGroupsById::get)

fun List<KinPlayItem>.groupedByFormat(): List<Pair<ContentFormatGroup?, List<KinPlayItem>>> {
    val grouped = LinkedHashMap<String, MutableList<KinPlayItem>>()
    for (item in this) grouped.getOrPut(item.formatGroupId ?: "__standalone__") { mutableListOf() }.add(item)
    return grouped.map { (groupId, items) -> contentFormatGroupsById[groupId] to items }
}

fun ContentPack.discoveryItems(): List<KinPlayItem> {
    val visibleItems = activeItems().filterNot { it.type == "mad_libs" }
    val collection = madLibs().takeIf { it.isNotEmpty() }?.let(::madLibsCollectionItem)
    return visibleItems + listOfNotNull(collection)
}

fun ContentPack.itemsForGameType(groupId: String): List<KinPlayItem> {
    val group = GameTypeGroup.fromId(groupId) ?: return emptyList()
    return discoveryItems().filter { group.id in it.discoveryGroupIds() }
}

fun KinPlayItem.discoveryGroupIds(): Set<String> = buildSet {
    when {
        isMadLibsCollection() -> addAll(
            setOf(GameTypeGroup.WORD_GAMES.id, GameTypeGroup.PRETEND_AND_STORIES.id),
        )
        id in setOf("alphabet_story", "three_word_story", "copycat_clap_code") || type == "prompt" -> {
            add(GameTypeGroup.WORD_GAMES.id)
        }
        id in setOf(
            "quiet_color_hunt",
            "animal_guessing_yes_no",
            "family_charades_animals",
            "would_you_rather_silly_family",
        ) -> add(GameTypeGroup.GUESSING_GAMES.id)
        id in setOf(
            "paper_airplane_weather",
            "timed_drawing_tiny_monster",
            "rainbow_sort_sprint",
        ) -> add(GameTypeGroup.ARTS_AND_MAKING.id)
        id in setOf("bilateral_mirror_moves", "cross_body_move_mix") -> {
            add(GameTypeGroup.BRAIN_AND_MOVEMENT.id)
            add(GameTypeGroup.MOVE_AND_PLAY.id)
        }
        "movement" in safetyTags || energyLevel == "high" -> add(GameTypeGroup.MOVE_AND_PLAY.id)
        "brain_games" in quickCategories -> add(GameTypeGroup.BRAIN_AND_MOVEMENT.id)
        "quality_time" in quickCategories || "dinner_table" in quickCategories -> add(GameTypeGroup.PRETEND_AND_STORIES.id)
        else -> add(GameTypeGroup.PRETEND_AND_STORIES.id)
    }
}
