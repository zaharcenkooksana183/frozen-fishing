package com.Tonkapa7.Piwpul.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameOverUiState(
    val noteDialogOpen: Boolean = false,
    val noteTripId: Long = 0L,
    val note: String = ""
)

class GameOverViewModel(private val anglerRepository: AnglerRepository) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    fun openNoteDialog() {
        _state.update { it.copy(noteDialogOpen = true) }
    }

    fun dismissNoteDialog() {
        _state.update { it.copy(noteDialogOpen = false) }
    }

    fun saveNote(tripId: Long, note: String) {
        val trimmed = note.trim()
        anglerRepository.updateTripNote(tripId, trimmed)
        _state.update {
            it.copy(noteDialogOpen = false, noteTripId = tripId, note = trimmed)
        }
    }
}
