package com.Tonkapa7.Piwpul.presentation.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.domain.model.ChallengeKind
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.usecase.UpdateDailyChallengeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DailyUiState(
    val kind: ChallengeKind = ChallengeKind.CATCH_SPECIES,
    val speciesId: Int = 1,
    val target: Int = 1,
    val progress: Int = 0,
    val complete: Boolean = false,
    val claimed: Boolean = false,
    val streak: Int = 0,
    val weekDone: List<Boolean> = List(7) { false },
    val reward: Int = GameConfig.DAILY_REWARD_POINTS
) {
    val ratio: Float
        get() = if (target <= 0) 0f else (progress.toFloat() / target).coerceIn(0f, 1f)
}

class DailyChallengeViewModel(
    private val anglerRepository: AnglerRepository,
    private val updateDailyChallenge: UpdateDailyChallengeUseCase
) : ViewModel() {

    val state: StateFlow<DailyUiState> = anglerRepository.daily
        .map { challenge ->
            DailyUiState(
                kind = challenge.template.kind,
                speciesId = challenge.template.speciesId,
                target = challenge.template.target,
                progress = challenge.progress,
                complete = challenge.complete,
                claimed = challenge.claimed,
                streak = challenge.streak,
                weekDone = challenge.weekDone
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DailyUiState()
        )

    init {
        updateDailyChallenge.ensureToday(today())
    }

    fun claimReward() {
        val reward = updateDailyChallenge.claim(today())
        if (reward <= 0) return
        val profile = anglerRepository.profile.value
        anglerRepository.saveProfile(profile.copy(totalPoints = profile.totalPoints + reward))
    }

    private fun today(): Long = System.currentTimeMillis() / GameConfig.DAY_MS
}
