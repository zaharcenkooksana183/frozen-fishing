package com.Tonkapa7.Piwpul.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.navigation.Screen
import com.Tonkapa7.Piwpul.core.theme.IceBackground
import com.Tonkapa7.Piwpul.core.theme.appTypography
import com.Tonkapa7.Piwpul.presentation.collection.CollectionScreen
import com.Tonkapa7.Piwpul.presentation.daily.DailyChallengeScreen
import com.Tonkapa7.Piwpul.presentation.game.GameScreen
import com.Tonkapa7.Piwpul.presentation.gameover.GameOverScreen
import com.Tonkapa7.Piwpul.presentation.menu.MenuScreen
import com.Tonkapa7.Piwpul.presentation.progress.ProgressScreen
import com.Tonkapa7.Piwpul.presentation.rewards.RewardsScreen
import com.Tonkapa7.Piwpul.presentation.settings.SettingsScreen
import com.Tonkapa7.Piwpul.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()
    val settings by appViewModel.settings.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = appTypography(if (settings.largeText) 1.15f else 1f)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = IceBackground
        ) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(220)) },
                label = "screen"
            ) { current ->
                when (current) {
                    Screen.Splash -> SplashScreen(
                        onFinished = appViewModel::onSplashFinished
                    )
                    Screen.Menu -> MenuScreen(
                        onStart = appViewModel::startSession,
                        onDaily = { appViewModel.open(Screen.Daily) },
                        onProgress = { appViewModel.open(Screen.Progress) },
                        onCollection = { appViewModel.open(Screen.Collection) },
                        onRewards = { appViewModel.open(Screen.Rewards) },
                        onSettings = { appViewModel.open(Screen.Settings) }
                    )
                    is Screen.Game -> GameScreen(
                        token = current.token,
                        onExit = appViewModel::goMenu,
                        onFinished = appViewModel::showReport
                    )
                    is Screen.GameOver -> GameOverScreen(
                        summary = current.summary,
                        onPlayAgain = appViewModel::startSession,
                        onMenu = appViewModel::goMenu
                    )
                    Screen.Daily -> DailyChallengeScreen(
                        onHome = appViewModel::back,
                        onStart = appViewModel::startSession
                    )
                    Screen.Progress -> ProgressScreen(onHome = appViewModel::back)
                    Screen.Rewards -> RewardsScreen(onHome = appViewModel::back)
                    Screen.Collection -> CollectionScreen(onHome = appViewModel::back)
                    Screen.Settings -> SettingsScreen(onHome = appViewModel::back)
                }
            }
        }
    }
}
