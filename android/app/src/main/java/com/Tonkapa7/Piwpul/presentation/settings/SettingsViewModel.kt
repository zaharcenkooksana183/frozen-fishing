package com.Tonkapa7.Piwpul.presentation.settings

import androidx.lifecycle.ViewModel
import com.Tonkapa7.Piwpul.domain.model.AppSettings
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val anglerRepository: AnglerRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.settings

    private val _resetDialogOpen = MutableStateFlow(false)
    val resetDialogOpen: StateFlow<Boolean> = _resetDialogOpen.asStateFlow()

    fun setSound(value: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(sound = value))
    }

    fun setHaptics(value: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(haptics = value))
    }

    fun setSnowfall(value: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(snowfall = value))
    }

    fun setLargeText(value: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(largeText = value))
    }

    fun setMetric(value: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(metric = value))
    }

    fun askReset() {
        _resetDialogOpen.update { true }
    }

    fun dismissReset() {
        _resetDialogOpen.update { false }
    }

    fun confirmReset() {
        anglerRepository.resetAll()
        _resetDialogOpen.update { false }
    }
}
