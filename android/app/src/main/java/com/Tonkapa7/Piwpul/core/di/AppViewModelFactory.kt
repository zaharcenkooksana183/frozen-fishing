package com.Tonkapa7.Piwpul.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Tonkapa7.Piwpul.presentation.app.AppViewModel
import com.Tonkapa7.Piwpul.presentation.collection.CollectionViewModel
import com.Tonkapa7.Piwpul.presentation.daily.DailyChallengeViewModel
import com.Tonkapa7.Piwpul.presentation.game.GameViewModel
import com.Tonkapa7.Piwpul.presentation.gameover.GameOverViewModel
import com.Tonkapa7.Piwpul.presentation.menu.MenuViewModel
import com.Tonkapa7.Piwpul.presentation.progress.ProgressViewModel
import com.Tonkapa7.Piwpul.presentation.rewards.RewardsViewModel
import com.Tonkapa7.Piwpul.presentation.settings.SettingsViewModel
import com.Tonkapa7.Piwpul.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when (modelClass) {
            AppViewModel::class.java -> AppViewModel(ServiceLocator.settingsRepository)
            SplashViewModel::class.java -> SplashViewModel()
            MenuViewModel::class.java -> MenuViewModel(
                ServiceLocator.anglerRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.updateDailyChallenge
            )
            GameViewModel::class.java -> GameViewModel(
                ServiceLocator.anglerRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.rollCatch,
                ServiceLocator.saveSession,
                ServiceLocator.unlockGear
            )
            GameOverViewModel::class.java -> GameOverViewModel(ServiceLocator.anglerRepository)
            DailyChallengeViewModel::class.java -> DailyChallengeViewModel(
                ServiceLocator.anglerRepository,
                ServiceLocator.updateDailyChallenge
            )
            ProgressViewModel::class.java -> ProgressViewModel(ServiceLocator.anglerRepository)
            RewardsViewModel::class.java -> RewardsViewModel(
                ServiceLocator.anglerRepository,
                ServiceLocator.unlockGear
            )
            CollectionViewModel::class.java -> CollectionViewModel(
                ServiceLocator.anglerRepository,
                ServiceLocator.settingsRepository
            )
            SettingsViewModel::class.java -> SettingsViewModel(
                ServiceLocator.anglerRepository,
                ServiceLocator.settingsRepository
            )
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
