package com.Tonkapa7.Piwpul.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tonkapa7.Piwpul.domain.model.LevelMath
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class TripRow(
    val id: Long,
    val spotId: Int,
    val dateLabel: String,
    val catches: Int,
    val points: Int,
    val note: String
)

data class ProgressUiState(
    val level: Int = 1,
    val xpInLevel: Int = 0,
    val xpForLevel: Int = 200,
    val xpProgress: Float = 0f,
    val totalTrips: Int = 0,
    val totalCatches: Int = 0,
    val bestSession: Int = 0,
    val rarestSpeciesId: Int = 0,
    val trips: List<TripRow> = emptyList()
)

class ProgressViewModel(anglerRepository: AnglerRepository) : ViewModel() {

    private val formatter = SimpleDateFormat("dd MMM HH:mm", Locale.US)

    val state: StateFlow<ProgressUiState> = combine(
        anglerRepository.profile,
        anglerRepository.trips
    ) { profile, trips ->
        val level = profile.level
        val floor = LevelMath.floorXp(level)
        val ceiling = LevelMath.ceilingXp(level)
        ProgressUiState(
            level = level,
            xpInLevel = profile.xp - floor,
            xpForLevel = (ceiling - floor).coerceAtLeast(1),
            xpProgress = LevelMath.progress(profile.xp),
            totalTrips = profile.totalTrips,
            totalCatches = profile.totalCatches,
            bestSession = profile.bestSessionPoints,
            rarestSpeciesId = profile.rarestSpeciesId,
            trips = trips.map { entry ->
                TripRow(
                    id = entry.id,
                    spotId = entry.spotId,
                    dateLabel = formatter.format(Date(entry.timestamp)),
                    catches = entry.catches,
                    points = entry.points,
                    note = entry.note
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ProgressUiState()
    )
}
