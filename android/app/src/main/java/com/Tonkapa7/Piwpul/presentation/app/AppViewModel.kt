package com.Tonkapa7.Piwpul.presentation.app

import androidx.lifecycle.ViewModel
import com.Tonkapa7.Piwpul.core.navigation.Screen
import com.Tonkapa7.Piwpul.domain.model.AppSettings
import com.Tonkapa7.Piwpul.domain.model.SessionSummary
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(settingsRepository: SettingsRepository) : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepository.settings

    fun onSplashFinished() {
        if (_screen.value != Screen.Splash) return
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun open(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) backStack.add(current)
        _screen.value = target
        syncBack()
    }

    fun startSession() {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.Game(System.currentTimeMillis())
        syncBack()
    }

    fun showReport(summary: SessionSummary) {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.GameOver(summary)
        syncBack()
    }

    fun goMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
