package com.kinplay.app.settings

import android.content.Context

private const val SETTINGS_PREFERENCES = "kinplay_settings"

class SharedPreferencesSettingsKeyValueStore(context: Context) : SettingsKeyValueStore {
    private val preferences = context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

    override fun read(key: String): String? = preferences.getString(key, null)

    override fun write(values: Map<String, String>) {
        preferences.edit().apply {
            values.forEach(::putString)
        }.apply()
    }
}
