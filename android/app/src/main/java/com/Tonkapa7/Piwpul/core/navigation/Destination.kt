package com.Tonkapa7.Piwpul.core.navigation

import com.Tonkapa7.Piwpul.domain.model.SessionSummary

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data class Game(val token: Long) : Screen

    data class GameOver(val summary: SessionSummary) : Screen

    data object Daily : Screen

    data object Progress : Screen

    data object Rewards : Screen

    data object Collection : Screen

    data object Settings : Screen
}
