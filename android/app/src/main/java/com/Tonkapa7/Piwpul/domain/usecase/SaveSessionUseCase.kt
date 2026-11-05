package com.Tonkapa7.Piwpul.domain.usecase

import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.domain.model.Catch
import com.Tonkapa7.Piwpul.domain.model.SessionSummary
import com.Tonkapa7.Piwpul.domain.model.SpeciesRecord
import com.Tonkapa7.Piwpul.domain.model.TripLogEntry
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository

class SaveSessionUseCase(
    private val repository: AnglerRepository,
    private val updateDailyChallenge: UpdateDailyChallengeUseCase
) {

    operator fun invoke(spotId: Int, casts: Int, catches: List<Catch>, now: Long): SessionSummary {
        val points = catches.sumOf { it.points }
        val best = catches.maxByOrNull { it.points }
        val profile = repository.profile.value
        val records = profile.records.toMutableMap()
        var newSpecies = false
        for (item in catches) {
            val existing = records[item.speciesId]
            if (existing == null) {
                newSpecies = true
                records[item.speciesId] = SpeciesRecord(
                    speciesId = item.speciesId,
                    count = 1,
                    bestLengthCm = item.lengthCm,
                    firstCaughtAt = now
                )
            } else {
                records[item.speciesId] = existing.copy(
                    count = existing.count + 1,
                    bestLengthCm = maxOf(existing.bestLengthCm, item.lengthCm)
                )
            }
        }
        val day = now / GameConfig.DAY_MS
        val newRecord = points > profile.bestSessionPoints && points > 0
        val updated = profile.copy(
            totalPoints = profile.totalPoints + points,
            totalCatches = profile.totalCatches + catches.size,
            totalTrips = profile.totalTrips + 1,
            tripsToday = if (profile.lastTripDay == day) profile.tripsToday + 1 else 1,
            lastTripDay = day,
            bestSessionPoints = maxOf(profile.bestSessionPoints, points),
            records = records
        )
        repository.saveProfile(updated)
        repository.addTrip(
            TripLogEntry(
                id = now,
                spotId = spotId,
                timestamp = now,
                catches = catches.size,
                points = points,
                note = ""
            )
        )
        updateDailyChallenge.applySession(day, catches, points)
        return SessionSummary(
            tripId = now,
            spotId = spotId,
            casts = casts,
            catches = catches,
            points = points,
            bestSpeciesId = best?.speciesId ?: 0,
            bestPoints = best?.points ?: 0,
            newRecord = newRecord,
            newSpecies = newSpecies,
            note = ""
        )
    }
}
