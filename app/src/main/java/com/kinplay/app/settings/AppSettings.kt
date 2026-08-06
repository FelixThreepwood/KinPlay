package com.kinplay.app.settings

import org.json.JSONObject

enum class GameTimer(val wireValue: String, val seconds: Int, val label: String) {
    THIRTY_SECONDS("30_seconds", 30, "30 seconds"),
    ONE_MINUTE("60_seconds", 60, "1 minute"),
    NINETY_SECONDS("90_seconds", 90, "90 seconds");

    companion object {
        fun fromWireValue(value: String?): GameTimer? = entries.firstOrNull { it.wireValue == value }
    }
}

enum class ActivityDuration(val wireValue: String, val minutes: Int, val label: String) {
    FIVE_MINUTES("5_minutes", 5, "5 minutes"),
    TEN_MINUTES("10_minutes", 10, "10 minutes"),
    TWENTY_MINUTES("20_minutes", 20, "20 minutes");

    companion object {
        fun fromWireValue(value: String?): ActivityDuration? = entries.firstOrNull { it.wireValue == value }
    }
}

enum class SessionRounds(val wireValue: String, val count: Int, val label: String) {
    THREE("3", 3, "3 rounds"),
    FIVE("5", 5, "5 rounds"),
    SEVEN("7", 7, "7 rounds"),
    TEN("10", 10, "10 rounds"),
    FIFTEEN("15", 15, "15 rounds");

    companion object {
        fun fromWireValue(value: String?): SessionRounds? = entries.firstOrNull { it.wireValue == value }
    }
}

data class SessionConfiguration(
    val duration: ActivityDuration,
    val rounds: SessionRounds,
)

data class SessionConfigurationOverride(
    val duration: ActivityDuration? = null,
    val rounds: SessionRounds? = null,
) {
    init {
        require(duration != null || rounds != null) { "A session override must change duration or rounds" }
    }

    fun applyTo(defaults: SessionConfiguration): SessionConfiguration = SessionConfiguration(
        duration = duration ?: defaults.duration,
        rounds = rounds ?: defaults.rounds,
    )
}

enum class AppColorTheme(val wireValue: String, val label: String, val description: String) {
    FOREST("forest", "Forest", "Warm cream and deep green"),
    OCEAN("ocean", "Ocean", "Cool blue and sea glass"),
    BERRY("berry", "Berry", "Soft rose and berry"),
    ;

    companion object {
        fun fromWireValue(value: String?): AppColorTheme? = entries.firstOrNull { it.wireValue == value }
    }
}

data class AppSettings(
    val gameTimer: GameTimer = GameTimer.ONE_MINUTE,
    val activityDuration: ActivityDuration = ActivityDuration.TEN_MINUTES,
    val colorTheme: AppColorTheme = AppColorTheme.FOREST,
    val launcherIcon: LauncherIconVariant = LauncherIconVariant.TEAL,
    val defaultRounds: SessionRounds = SessionRounds.THREE,
    val nextSessionOverrides: Map<String, SessionConfigurationOverride> = emptyMap(),
) {
    companion object {
        val DEFAULT = AppSettings()
    }
}

object AppSettingsCodec {
    const val GAME_TIMER_KEY = "settings_game_timer_v1"
    const val ACTIVITY_DURATION_KEY = "settings_activity_duration_v1"
    const val COLOR_THEME_KEY = "settings_color_theme_v1"
    const val LAUNCHER_ICON_KEY = "settings_launcher_icon_v1"
    const val DEFAULT_ROUNDS_KEY = "settings_default_rounds_v1"
    const val SESSION_OVERRIDES_KEY = "settings_session_overrides_v1"
    val knownKeys = setOf(
        GAME_TIMER_KEY,
        ACTIVITY_DURATION_KEY,
        COLOR_THEME_KEY,
        LAUNCHER_ICON_KEY,
        DEFAULT_ROUNDS_KEY,
        SESSION_OVERRIDES_KEY,
    )

    fun decode(values: Map<String, String?>): AppSettings = AppSettings(
        gameTimer = GameTimer.fromWireValue(values[GAME_TIMER_KEY]) ?: AppSettings.DEFAULT.gameTimer,
        activityDuration = ActivityDuration.fromWireValue(values[ACTIVITY_DURATION_KEY]) ?: AppSettings.DEFAULT.activityDuration,
        colorTheme = AppColorTheme.fromWireValue(values[COLOR_THEME_KEY]) ?: AppSettings.DEFAULT.colorTheme,
        launcherIcon = LauncherIconVariant.fromWireValue(values[LAUNCHER_ICON_KEY]),
        defaultRounds = SessionRounds.fromWireValue(values[DEFAULT_ROUNDS_KEY]) ?: AppSettings.DEFAULT.defaultRounds,
        nextSessionOverrides = decodeOverrides(values[SESSION_OVERRIDES_KEY]),
    )

    fun encode(settings: AppSettings): Map<String, String> = mapOf(
        GAME_TIMER_KEY to settings.gameTimer.wireValue,
        ACTIVITY_DURATION_KEY to settings.activityDuration.wireValue,
        COLOR_THEME_KEY to settings.colorTheme.wireValue,
        LAUNCHER_ICON_KEY to settings.launcherIcon.wireValue,
        DEFAULT_ROUNDS_KEY to settings.defaultRounds.wireValue,
        SESSION_OVERRIDES_KEY to encodeOverrides(settings.nextSessionOverrides),
    )

    private fun decodeOverrides(value: String?): Map<String, SessionConfigurationOverride> {
        if (value.isNullOrBlank()) return emptyMap()
        return runCatching {
            val root = JSONObject(value)
            root.keys().asSequence().mapNotNull { gameId ->
                val override = root.optJSONObject(gameId) ?: return@mapNotNull null
                val duration = ActivityDuration.fromWireValue(override.optString("duration").takeIf(String::isNotBlank))
                val rounds = SessionRounds.fromWireValue(override.optString("rounds").takeIf(String::isNotBlank))
                if (duration == null && rounds == null) {
                    null
                } else {
                    gameId to SessionConfigurationOverride(duration = duration, rounds = rounds)
                }
            }.toMap()
        }.getOrDefault(emptyMap())
    }

    private fun encodeOverrides(overrides: Map<String, SessionConfigurationOverride>): String {
        val root = JSONObject()
        overrides.toSortedMap().forEach { (gameId, override) ->
            root.put(
                gameId,
                JSONObject().apply {
                    override.duration?.let { put("duration", it.wireValue) }
                    override.rounds?.let { put("rounds", it.wireValue) }
                },
            )
        }
        return root.toString()
    }
}

/** Small replaceable boundary so future finite preferences (for example launcher variants) do not alter navigation. */
interface SettingsKeyValueStore {
    fun read(key: String): String?
    fun write(values: Map<String, String>)
}

class AppSettingsRepository(private val storage: SettingsKeyValueStore) {
    fun load(): AppSettings = AppSettingsCodec.decode(AppSettingsCodec.knownKeys.associateWith(storage::read))

    fun save(settings: AppSettings) {
        storage.write(AppSettingsCodec.encode(settings))
    }

    fun saveNextSessionOverride(gameId: String, override: SessionConfigurationOverride) {
        require(gameId.isNotBlank()) { "A game ID is required for a session override" }
        val current = load()
        save(current.copy(nextSessionOverrides = current.nextSessionOverrides + (gameId to override)))
    }

    fun clearNextSessionOverride(gameId: String) {
        val current = load()
        if (gameId in current.nextSessionOverrides) {
            save(current.copy(nextSessionOverrides = current.nextSessionOverrides - gameId))
        }
    }

    fun peekNextSessionConfiguration(gameId: String): SessionConfiguration =
        load().resolveNextSessionConfiguration(gameId)

    fun consumeNextSessionConfiguration(gameId: String): SessionConfiguration {
        val current = load()
        val resolved = current.resolveNextSessionConfiguration(gameId)
        if (gameId in current.nextSessionOverrides) {
            save(current.copy(nextSessionOverrides = current.nextSessionOverrides - gameId))
        }
        return resolved
    }
}

fun AppSettings.sessionDefaults(): SessionConfiguration =
    SessionConfiguration(duration = activityDuration, rounds = defaultRounds)

fun AppSettings.resolveNextSessionConfiguration(gameId: String): SessionConfiguration =
    nextSessionOverrides[gameId]?.applyTo(sessionDefaults()) ?: sessionDefaults()

class InMemorySettingsKeyValueStore(
    val values: MutableMap<String, String> = mutableMapOf(),
) : SettingsKeyValueStore {
    override fun read(key: String): String? = values[key]

    override fun write(values: Map<String, String>) {
        this.values.putAll(values)
    }
}
