package com.Tonkapa7.Piwpul.domain.repository

import com.Tonkapa7.Piwpul.domain.model.AnglerProfile
import com.Tonkapa7.Piwpul.domain.model.DailyChallenge
import com.Tonkapa7.Piwpul.domain.model.TripLogEntry
import kotlinx.coroutines.flow.StateFlow

interface AnglerRepository {
    val profile: StateFlow<AnglerProfile>
    val trips: StateFlow<List<TripLogEntry>>
    val daily: StateFlow<DailyChallenge>

    fun saveProfile(profile: AnglerProfile)

    fun addTrip(entry: TripLogEntry)

    fun updateTripNote(tripId: Long, note: String)

    fun saveDaily(challenge: DailyChallenge)

    fun resetAll()
}
