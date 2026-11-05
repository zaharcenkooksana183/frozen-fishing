package com.Tonkapa7.Piwpul.data.repository

import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.data.local.PreferencesStore
import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.model.AnglerProfile
import com.Tonkapa7.Piwpul.domain.model.DailyChallenge
import com.Tonkapa7.Piwpul.domain.model.Gear
import com.Tonkapa7.Piwpul.domain.model.SpeciesRecord
import com.Tonkapa7.Piwpul.domain.model.TripLogEntry
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import java.net.URLDecoder
import java.net.URLEncoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnglerRepositoryImpl(private val store: PreferencesStore) : AnglerRepository {

    private val _profile = MutableStateFlow(loadProfile())
    override val profile: StateFlow<AnglerProfile> = _profile.asStateFlow()

    private val _trips = MutableStateFlow(loadTrips())
    override val trips: StateFlow<List<TripLogEntry>> = _trips.asStateFlow()

    private val _daily = MutableStateFlow(loadDaily())
    override val daily: StateFlow<DailyChallenge> = _daily.asStateFlow()

    override fun saveProfile(profile: AnglerProfile) {
        store.writeInt(KEY_TOTAL_POINTS, profile.totalPoints)
        store.writeInt(KEY_SPENT_POINTS, profile.spentPoints)
        store.writeInt(KEY_TOTAL_CATCHES, profile.totalCatches)
        store.writeInt(KEY_TOTAL_TRIPS, profile.totalTrips)
        store.writeInt(KEY_TRIPS_TODAY, profile.tripsToday)
        store.writeLong(KEY_LAST_TRIP_DAY, profile.lastTripDay)
        store.writeInt(KEY_BEST_SESSION, profile.bestSessionPoints)
        store.writeString(KEY_RECORDS, encodeRecords(profile.records))
        store.writeString(KEY_OWNED_GEAR, profile.ownedGear.joinToString(ITEM_SEPARATOR))
        store.writeInt(KEY_EQUIPPED_GEAR, profile.equippedGear)
        _profile.value = profile
    }

    override fun addTrip(entry: TripLogEntry) {
        val updated = (listOf(entry) + _trips.value).take(GameConfig.TRIP_LOG_LIMIT)
        persistTrips(updated)
    }

    override fun updateTripNote(tripId: Long, note: String) {
        val updated = _trips.value.map { if (it.id == tripId) it.copy(note = note) else it }
        persistTrips(updated)
    }

    override fun saveDaily(challenge: DailyChallenge) {
        store.writeLong(KEY_DAILY_DAY, challenge.dayIndex)
        store.writeInt(KEY_DAILY_TEMPLATE, challenge.template.id)
        store.writeInt(KEY_DAILY_PROGRESS, challenge.progress)
        store.writeBoolean(KEY_DAILY_CLAIMED, challenge.claimed)
        store.writeInt(KEY_DAILY_STREAK, challenge.streak)
        store.writeString(KEY_DAILY_WEEK, encodeWeek(challenge.weekDone))
        _daily.value = challenge
    }

    override fun resetAll() {
        store.clearProgress(
            listOf(
                KEY_TOTAL_POINTS,
                KEY_SPENT_POINTS,
                KEY_TOTAL_CATCHES,
                KEY_TOTAL_TRIPS,
                KEY_TRIPS_TODAY,
                KEY_LAST_TRIP_DAY,
                KEY_BEST_SESSION,
                KEY_RECORDS,
                KEY_OWNED_GEAR,
                KEY_EQUIPPED_GEAR,
                KEY_TRIPS,
                KEY_DAILY_DAY,
                KEY_DAILY_TEMPLATE,
                KEY_DAILY_PROGRESS,
                KEY_DAILY_CLAIMED,
                KEY_DAILY_STREAK,
                KEY_DAILY_WEEK
            )
        )
        _profile.value = AnglerProfile()
        _trips.value = emptyList()
        _daily.value = emptyDaily()
    }

    private fun persistTrips(entries: List<TripLogEntry>) {
        store.writeString(KEY_TRIPS, encodeTrips(entries))
        _trips.value = entries
    }

    private fun loadProfile(): AnglerProfile = AnglerProfile(
        totalPoints = store.readInt(KEY_TOTAL_POINTS, 0),
        spentPoints = store.readInt(KEY_SPENT_POINTS, 0),
        totalCatches = store.readInt(KEY_TOTAL_CATCHES, 0),
        totalTrips = store.readInt(KEY_TOTAL_TRIPS, 0),
        tripsToday = store.readInt(KEY_TRIPS_TODAY, 0),
        lastTripDay = store.readLong(KEY_LAST_TRIP_DAY, 0L),
        bestSessionPoints = store.readInt(KEY_BEST_SESSION, 0),
        records = decodeRecords(store.readString(KEY_RECORDS, "")),
        ownedGear = decodeGear(store.readString(KEY_OWNED_GEAR, "")),
        equippedGear = store.readInt(KEY_EQUIPPED_GEAR, Gear.LIGHT_ROD)
    )

    private fun loadTrips(): List<TripLogEntry> = decodeTrips(store.readString(KEY_TRIPS, ""))

    private fun loadDaily(): DailyChallenge {
        val day = store.readLong(KEY_DAILY_DAY, -1L)
        if (day < 0L) return emptyDaily()
        val templateId = store.readInt(KEY_DAILY_TEMPLATE, 1)
        val template = SampleData.challenges.firstOrNull { it.id == templateId }
            ?: SampleData.challenges.first()
        val week = store.readString(KEY_DAILY_WEEK, "")
            .split(ITEM_SEPARATOR)
            .filter { it.isNotBlank() }
            .map { it == FLAG_TRUE }
        return DailyChallenge(
            dayIndex = day,
            template = template,
            progress = store.readInt(KEY_DAILY_PROGRESS, 0),
            claimed = store.readBoolean(KEY_DAILY_CLAIMED, false),
            streak = store.readInt(KEY_DAILY_STREAK, 0),
            weekDone = if (week.size == WEEK_LENGTH) week else List(WEEK_LENGTH) { false }
        )
    }

    private fun emptyDaily(): DailyChallenge = DailyChallenge(
        dayIndex = -1L,
        template = SampleData.challenges.first(),
        progress = 0,
        claimed = false,
        streak = 0,
        weekDone = List(WEEK_LENGTH) { false }
    )

    private fun encodeWeek(week: List<Boolean>): String =
        week.joinToString(ITEM_SEPARATOR) { if (it) FLAG_TRUE else FLAG_FALSE }

    private fun encodeRecords(records: Map<Int, SpeciesRecord>): String =
        records.values.joinToString(ITEM_SEPARATOR) { record ->
            listOf(
                record.speciesId.toString(),
                record.count.toString(),
                record.bestLengthCm.toString(),
                record.firstCaughtAt.toString()
            ).joinToString(FIELD_SEPARATOR)
        }

    private fun decodeRecords(raw: String): Map<Int, SpeciesRecord> {
        if (raw.isBlank()) return emptyMap()
        val result = mutableMapOf<Int, SpeciesRecord>()
        raw.split(ITEM_SEPARATOR).forEach { chunk ->
            val parts = chunk.split(FIELD_SEPARATOR)
            if (parts.size == 4) {
                val id = parts[0].toIntOrNull()
                val count = parts[1].toIntOrNull()
                val best = parts[2].toIntOrNull()
                val first = parts[3].toLongOrNull()
                if (id != null && count != null && best != null && first != null) {
                    result[id] = SpeciesRecord(id, count, best, first)
                }
            }
        }
        return result
    }

    private fun decodeGear(raw: String): Set<Int> {
        if (raw.isBlank()) return setOf(Gear.LIGHT_ROD)
        val parsed = raw.split(ITEM_SEPARATOR).mapNotNull { it.toIntOrNull() }.toSet()
        return if (parsed.isEmpty()) setOf(Gear.LIGHT_ROD) else parsed + Gear.LIGHT_ROD
    }

    private fun encodeTrips(entries: List<TripLogEntry>): String =
        entries.joinToString(ITEM_SEPARATOR) { entry ->
            listOf(
                entry.id.toString(),
                entry.spotId.toString(),
                entry.timestamp.toString(),
                entry.catches.toString(),
                entry.points.toString(),
                URLEncoder.encode(entry.note, CHARSET)
            ).joinToString(FIELD_SEPARATOR)
        }

    private fun decodeTrips(raw: String): List<TripLogEntry> {
        if (raw.isBlank()) return emptyList()
        return raw.split(ITEM_SEPARATOR).mapNotNull { chunk ->
            val parts = chunk.split(FIELD_SEPARATOR)
            if (parts.size != 6) return@mapNotNull null
            val id = parts[0].toLongOrNull() ?: return@mapNotNull null
            val spotId = parts[1].toIntOrNull() ?: return@mapNotNull null
            val timestamp = parts[2].toLongOrNull() ?: return@mapNotNull null
            val catches = parts[3].toIntOrNull() ?: return@mapNotNull null
            val points = parts[4].toIntOrNull() ?: return@mapNotNull null
            TripLogEntry(id, spotId, timestamp, catches, points, URLDecoder.decode(parts[5], CHARSET))
        }
    }

    private companion object {
        const val ITEM_SEPARATOR = ";"
        const val FIELD_SEPARATOR = ":"
        const val CHARSET = "UTF-8"
        const val FLAG_TRUE = "1"
        const val FLAG_FALSE = "0"
        const val WEEK_LENGTH = 7
        const val KEY_TOTAL_POINTS = "angler_total_points"
        const val KEY_SPENT_POINTS = "angler_spent_points"
        const val KEY_TOTAL_CATCHES = "angler_total_catches"
        const val KEY_TOTAL_TRIPS = "angler_total_trips"
        const val KEY_TRIPS_TODAY = "angler_trips_today"
        const val KEY_LAST_TRIP_DAY = "angler_last_trip_day"
        const val KEY_BEST_SESSION = "angler_best_session"
        const val KEY_RECORDS = "angler_records"
        const val KEY_OWNED_GEAR = "angler_owned_gear"
        const val KEY_EQUIPPED_GEAR = "angler_equipped_gear"
        const val KEY_TRIPS = "angler_trips"
        const val KEY_DAILY_DAY = "daily_day"
        const val KEY_DAILY_TEMPLATE = "daily_template"
        const val KEY_DAILY_PROGRESS = "daily_progress"
        const val KEY_DAILY_CLAIMED = "daily_claimed"
        const val KEY_DAILY_STREAK = "daily_streak"
        const val KEY_DAILY_WEEK = "daily_week"
    }
}
