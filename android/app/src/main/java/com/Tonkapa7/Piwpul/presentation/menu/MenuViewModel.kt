package com.Tonkapa7.Piwpul.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.model.AnglerProfile
import com.Tonkapa7.Piwpul.domain.model.AppSettings
import com.Tonkapa7.Piwpul.domain.model.DailyChallenge
import com.Tonkapa7.Piwpul.domain.model.LevelMath
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import com.Tonkapa7.Piwpul.domain.usecase.UpdateDailyChallengeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    anglerRepository: AnglerRepository,
    settingsRepository: SettingsRepository,
    private val updateDailyChallenge: UpdateDailyChallengeUseCase
) : ViewModel() {

    val state: StateFlow<MenuUiState> = combine(
        anglerRepository.profile,
        anglerRepository.daily,
        settingsRepository.settings
    ) { profile, daily, settings ->
        build(profile, daily, settings)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MenuUiState()
    )

    init {
        updateDailyChallenge.ensureToday(System.currentTimeMillis() / GameConfig.DAY_MS)
    }

    private fun build(
        profile: AnglerProfile,
        daily: DailyChallenge,
        settings: AppSettings
    ): MenuUiState {
        val level = profile.level
        val floor = LevelMath.floorXp(level)
        val ceiling = LevelMath.ceilingXp(level)
        val locked = SampleData.gear.count { !profile.ownedGear.contains(it.id) }
        val affordable = SampleData.gear.any {
            !profile.ownedGear.contains(it.id) && profile.balance >= it.cost
        }
        return MenuUiState(
            level = level,
            xpInLevel = profile.xp - floor,
            xpForLevel = (ceiling - floor).coerceAtLeast(1),
            xpProgress = LevelMath.progress(profile.xp),
            balance = profile.balance,
            tripsToday = profile.tripsToday,
            totalTrips = profile.totalTrips,
            totalCatches = profile.totalCatches,
            discoveredCount = profile.discoveredSpecies,
            discoveredIds = profile.records.keys.sorted().take(3),
            speciesTotal = GameConfig.SPECIES_COUNT,
            dailyProgress = daily.progress,
            dailyTarget = daily.template.target,
            lockedGear = locked,
            gearAffordable = affordable,
            snowfall = settings.snowfall
        )
    }
}
