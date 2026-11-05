package com.Tonkapa7.Piwpul.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.model.Catch
import com.Tonkapa7.Piwpul.domain.model.Gear
import com.Tonkapa7.Piwpul.domain.model.SessionSummary
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import com.Tonkapa7.Piwpul.domain.usecase.RollCatchUseCase
import com.Tonkapa7.Piwpul.domain.usecase.SaveSessionUseCase
import com.Tonkapa7.Piwpul.domain.usecase.UnlockGearUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed interface GameEvent {
    data class Finished(val summary: SessionSummary) : GameEvent
}

class GameViewModel(
    private val anglerRepository: AnglerRepository,
    private val settingsRepository: SettingsRepository,
    private val rollCatch: RollCatchUseCase,
    private val saveSession: SaveSessionUseCase,
    private val unlockGear: UnlockGearUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var sessionToken = 0L
    private var loop: Job? = null
    private var phaseStart = 0L
    private var biteDelay = 0L
    private var pausedElapsed = 0L
    private var autoDrop = false
    private val caught = mutableListOf<Catch>()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { it.copy(metric = settings.metric) }
            }
        }
    }

    fun startSession(token: Long) {
        if (token == sessionToken) return
        sessionToken = token
        loop?.cancel()
        caught.clear()
        autoDrop = false
        pausedElapsed = 0L
        val profile = anglerRepository.profile.value
        val spot = SampleData.spots[(profile.totalTrips % SampleData.spots.size)]
        val extra = if (profile.ownedGear.contains(Gear.THERMAL_TENT)) GameConfig.TENT_BONUS_CASTS else 0
        _state.value = GameUiState(
            phase = GamePhase.IDLE,
            cast = 1,
            totalCasts = GameConfig.CASTS_PER_SESSION + extra,
            spotId = spot.id,
            spotDepthLabel = spot.depthLabel,
            bestPoints = profile.bestSessionPoints,
            ownedGear = profile.ownedGear,
            equippedGear = profile.equippedGear,
            metric = settingsRepository.settings.value.metric
        )
        phaseStart = now()
        startLoop()
    }

    fun onPrimaryAction() {
        when (_state.value.phase) {
            GamePhase.IDLE -> {
                autoDrop = true
                enterSinking()
            }
            GamePhase.SINKING, GamePhase.WAITING -> enterMissed()
            GamePhase.BITE -> resolveHook()
            GamePhase.MISSED, GamePhase.REVEAL -> advance()
            GamePhase.FINISHED -> Unit
        }
    }

    fun equipGear(gearId: Int) {
        if (!unlockGear.equip(gearId)) return
        _state.update { it.copy(equippedGear = anglerRepository.profile.value.equippedGear) }
    }

    fun pauseSession() {
        if (_state.value.paused || _state.value.phase == GamePhase.FINISHED) return
        pausedElapsed = now() - phaseStart
        loop?.cancel()
        loop = null
        _state.update { it.copy(paused = true) }
    }

    fun resumeSession() {
        if (!_state.value.paused) return
        phaseStart = now() - pausedElapsed
        _state.update { it.copy(paused = false) }
        startLoop()
    }

    override fun onCleared() {
        loop?.cancel()
        super.onCleared()
    }

    private fun startLoop() {
        loop?.cancel()
        loop = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.TICK_MS)
                tick(now())
            }
        }
    }

    private fun tick(moment: Long) {
        val current = _state.value
        if (current.paused) return
        val elapsed = moment - phaseStart
        when (current.phase) {
            GamePhase.IDLE -> {
                if (autoDrop && elapsed >= GameConfig.AUTO_NEXT_MS) enterSinking()
            }
            GamePhase.SINKING -> {
                val progress = (elapsed.toFloat() / GameConfig.LINE_DROP_MS).coerceIn(0f, 1f)
                _state.update { it.copy(bobProgress = progress) }
                if (elapsed >= GameConfig.LINE_DROP_MS) enterWaiting()
            }
            GamePhase.WAITING -> {
                val shimmer = 0.06f + 0.04f * ((elapsed % 1200L).toFloat() / 1200f)
                _state.update { it.copy(meterProgress = shimmer) }
                if (elapsed >= biteDelay) enterBite()
            }
            GamePhase.BITE -> {
                val window = hookWindow()
                val progress = (elapsed.toFloat() / window).coerceIn(0f, 1f)
                _state.update { it.copy(meterProgress = progress) }
                if (elapsed >= window) enterMissed()
            }
            GamePhase.MISSED -> {
                if (elapsed >= GameConfig.MISS_MS) advance()
            }
            GamePhase.REVEAL -> {
                if (elapsed >= GameConfig.REVEAL_MS) advance()
            }
            GamePhase.FINISHED -> Unit
        }
    }

    private fun enterSinking() {
        phaseStart = now()
        _state.update {
            it.copy(
                phase = GamePhase.SINKING,
                bobProgress = 0f,
                meterProgress = 0f,
                revealSpeciesId = 0
            )
        }
    }

    private fun enterWaiting() {
        phaseStart = now()
        biteDelay = Random.nextLong(GameConfig.BITE_DELAY_MIN_MS, GameConfig.BITE_DELAY_MAX_MS)
        _state.update { it.copy(phase = GamePhase.WAITING, bobProgress = 1f) }
    }

    private fun enterBite() {
        phaseStart = now()
        _state.update { it.copy(phase = GamePhase.BITE, meterProgress = 0f) }
    }

    private fun enterMissed() {
        phaseStart = now()
        _state.update { it.copy(phase = GamePhase.MISSED, meterProgress = 1f) }
    }

    private fun resolveHook() {
        val sonar = _state.value.equippedGear == Gear.SONAR_UNIT
        val result = rollCatch(sonar)
        caught.add(result)
        phaseStart = now()
        _state.update {
            it.copy(
                phase = GamePhase.REVEAL,
                catches = it.catches + 1,
                points = it.points + result.points,
                revealSpeciesId = result.speciesId,
                revealPoints = result.points,
                revealLengthCm = result.lengthCm,
                revealRarity = result.rarity,
                meterProgress = 1f
            )
        }
    }

    private fun advance() {
        val current = _state.value
        if (current.cast >= current.totalCasts) {
            finishSession(current)
            return
        }
        phaseStart = now()
        _state.update {
            it.copy(
                phase = GamePhase.IDLE,
                cast = it.cast + 1,
                bobProgress = 0f,
                meterProgress = 0f,
                revealSpeciesId = 0
            )
        }
    }

    private fun finishSession(current: GameUiState) {
        loop?.cancel()
        loop = null
        _state.update { it.copy(phase = GamePhase.FINISHED, meterProgress = 0f) }
        val summary = saveSession(
            spotId = current.spotId,
            casts = current.totalCasts,
            catches = caught.toList(),
            now = now()
        )
        viewModelScope.launch { eventChannel.send(GameEvent.Finished(summary)) }
    }

    private fun hookWindow(): Long {
        val bonus = if (_state.value.equippedGear == Gear.TUNGSTEN_JIG) {
            GameConfig.JIG_WINDOW_BONUS_MS
        } else {
            0L
        }
        return GameConfig.HOOK_WINDOW_MS + bonus
    }

    private fun now(): Long = System.currentTimeMillis()
}