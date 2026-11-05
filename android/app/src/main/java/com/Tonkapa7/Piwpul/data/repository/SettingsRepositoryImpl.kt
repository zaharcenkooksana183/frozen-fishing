package com.Tonkapa7.Piwpul.data.repository

import com.Tonkapa7.Piwpul.data.local.PreferencesStore
import com.Tonkapa7.Piwpul.domain.model.AppSettings
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    private val _settings = MutableStateFlow(load())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun update(settings: AppSettings) {
        store.writeBoolean(KEY_SOUND, settings.sound)
        store.writeBoolean(KEY_HAPTICS, settings.haptics)
        store.writeBoolean(KEY_SNOWFALL, settings.snowfall)
        store.writeBoolean(KEY_LARGE_TEXT, settings.largeText)
        store.writeBoolean(KEY_METRIC, settings.metric)
        _settings.value = settings
    }

    private fun load(): AppSettings = AppSettings(
        sound = store.readBoolean(KEY_SOUND, true),
        haptics = store.readBoolean(KEY_HAPTICS, true),
        snowfall = store.readBoolean(KEY_SNOWFALL, true),
        largeText = store.readBoolean(KEY_LARGE_TEXT, false),
        metric = store.readBoolean(KEY_METRIC, true)
    )

    private companion object {
        const val KEY_SOUND = "settings_sound"
        const val KEY_HAPTICS = "settings_haptics"
        const val KEY_SNOWFALL = "settings_snowfall"
        const val KEY_LARGE_TEXT = "settings_large_text"
        const val KEY_METRIC = "settings_metric"
    }
}
