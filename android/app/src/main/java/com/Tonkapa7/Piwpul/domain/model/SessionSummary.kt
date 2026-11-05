package com.Tonkapa7.Piwpul.domain.model

data class SessionSummary(
    val tripId: Long,
    val spotId: Int,
    val casts: Int,
    val catches: List<Catch>,
    val points: Int,
    val bestSpeciesId: Int,
    val bestPoints: Int,
    val newRecord: Boolean,
    val newSpecies: Boolean,
    val note: String
) {
    val catchCount: Int
        get() = catches.size
}
