package com.Tonkapa7.Piwpul.domain.model

data class Species(
    val id: Int,
    val rarity: Rarity,
    val points: Int,
    val weight: Int,
    val minLengthCm: Int,
    val maxLengthCm: Int
)
