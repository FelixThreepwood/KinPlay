package com.kinplay.app.settings

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
    val knownKeys = setOf(GAME_TIMER_KEY, ACTIVITY_DURATION_KEY, COLOR_THEME_KEY, LAUNCHER_ICON_KEY)

    fun decode(values: Map<String, String?>): AppSettings = AppSettings(
        gameTimer = GameTimer.fromWireValue(values[GAME_TIMER_KEY]) ?: AppSettings.DEFAULT.gameTimer,
        activityDuration = ActivityDuration.fromWireValue(values[ACTIVITY_DURATION_KEY]) ?: AppSettings.DEFAULT.activityDuration,
        colorTheme = AppColorTheme.fromWireValue(values[COLOR_THEME_KEY]) ?: AppSettings.DEFAULT.colorTheme,
        launcherIcon = LauncherIconVariant.fromWireValue(values[LAUNCHER_ICON_KEY]),
    )

    fun encode(settings: AppSettings): Map<String, String> = mapOf(
        GAME_TIMER_KEY to settings.gameTimer.wireValue,
        ACTIVITY_DURATION_KEY to settings.activityDuration.wireValue,
        COLOR_THEME_KEY to settings.colorTheme.wireValue,
        LAUNCHER_ICON_KEY to settings.launcherIcon.wireValue,
    )
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
}

class InMemorySettingsKeyValueStore(
    val values: MutableMap<String, String> = mutableMapOf(),
) : SettingsKeyValueStore {
    override fun read(key: String): String? = values[key]

    override fun write(values: Map<String, String>) {
        this.values.putAll(values)
    }
}
