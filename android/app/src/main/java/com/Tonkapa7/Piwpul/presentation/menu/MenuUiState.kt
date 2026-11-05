package com.Tonkapa7.Piwpul.presentation.menu

data class MenuUiState(
    val level: Int = 1,
    val xpInLevel: Int = 0,
    val xpForLevel: Int = 200,
    val xpProgress: Float = 0f,
    val balance: Int = 0,
    val tripsToday: Int = 0,
    val totalTrips: Int = 0,
    val totalCatches: Int = 0,
    val discoveredCount: Int = 0,
    val discoveredIds: List<Int> = emptyList(),
    val speciesTotal: Int = 5,
    val dailyProgress: Int = 0,
    val dailyTarget: Int = 1,
    val lockedGear: Int = 0,
    val gearAffordable: Boolean = false,
    val snowfall: Boolean = true
) {
    val hasTrips: Boolean
        get() = totalTrips > 0
}
