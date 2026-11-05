package com.Tonkapa7.Piwpul.presentation.game

import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.domain.model.Gear
import com.Tonkapa7.Piwpul.domain.model.Rarity

enum class GamePhase {
    IDLE,
    SINKING,
    WAITING,
    BITE,
    MISSED,
    REVEAL,
    FINISHED
}

data class GameUiState(
    val phase: GamePhase = GamePhase.IDLE,
    val cast: Int = 1,
    val totalCasts: Int = GameConfig.CASTS_PER_SESSION,
    val spotId: Int = 1,
    val spotDepthLabel: String = "",
    val catches: Int = 0,
    val points: Int = 0,
    val bestPoints: Int = 0,
    val bobProgress: Float = 0f,
    val meterProgress: Float = 0f,
    val revealSpeciesId: Int = 0,
    val revealPoints: Int = 0,
    val revealLengthCm: Int = 0,
    val revealRarity: Rarity = Rarity.COMMON,
    val ownedGear: Set<Int> = setOf(Gear.LIGHT_ROD),
    val equippedGear: Int = Gear.LIGHT_ROD,
    val paused: Boolean = false,
    val metric: Boolean = true
) {
    val biteActive: Boolean
        get() = phase == GamePhase.BITE

    val actionEnabled: Boolean
        get() = phase != GamePhase.REVEAL && phase != GamePhase.FINISHED
}
