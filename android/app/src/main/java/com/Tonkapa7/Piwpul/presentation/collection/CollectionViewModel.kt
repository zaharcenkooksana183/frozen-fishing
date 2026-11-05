package com.Tonkapa7.Piwpul.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.model.Rarity
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class SpeciesRow(
    val id: Int,
    val rarity: Rarity,
    val points: Int,
    val discovered: Boolean,
    val count: Int,
    val bestLengthCm: Int,
    val firstCaughtLabel: String
)

data class CollectionUiState(
    val entries: List<SpeciesRow> = emptyList(),
    val discoveredCount: Int = 0,
    val total: Int = GameConfig.SPECIES_COUNT,
    val metric: Boolean = true,
    val selectedId: Int = 0
) {
    val ratio: Float
        get() = if (total <= 0) 0f else discoveredCount.toFloat() / total
}

class CollectionViewModel(
    anglerRepository: AnglerRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    private val selected = MutableStateFlow(0)

    val state: StateFlow<CollectionUiState> = combine(
        anglerRepository.profile,
        settingsRepository.settings,
        selected
    ) { profile, settings, selectedId ->
        val entries = SampleData.species.map { species ->
            val record = profile.records[species.id]
            SpeciesRow(
                id = species.id,
                rarity = species.rarity,
                points = species.points,
                discovered = record != null,
                count = record?.count ?: 0,
                bestLengthCm = record?.bestLengthCm ?: 0,
                firstCaughtLabel = record?.let { formatter.format(Date(it.firstCaughtAt)) }.orEmpty()
            )
        }
        CollectionUiState(
            entries = entries,
            discoveredCount = entries.count { it.discovered },
            total = entries.size,
            metric = settings.metric,
            selectedId = selectedId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = CollectionUiState()
    )

    fun select(speciesId: Int) {
        selected.update { speciesId }
    }

    fun dismiss() {
        selected.update { 0 }
    }
}
