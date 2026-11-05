package com.Tonkapa7.Piwpul.domain.repository

import com.Tonkapa7.Piwpul.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>

    fun update(settings: AppSettings)
}
