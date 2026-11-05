package com.Tonkapa7.Piwpul.domain.model

data class TripLogEntry(
    val id: Long,
    val spotId: Int,
    val timestamp: Long,
    val catches: Int,
    val points: Int,
    val note: String
)
