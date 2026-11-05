package com.Tonkapa7.Piwpul.domain.model

import com.Tonkapa7.Piwpul.core.config.GameConfig

data class AnglerProfile(
    val totalPoints: Int = 0,
    val spentPoints: Int = 0,
    val totalCatches: Int = 0,
    val totalTrips: Int = 0,
    val tripsToday: Int = 0,
    val lastTripDay: Long = 0L,
    val bestSessionPoints: Int = 0,
    val records: Map<Int, SpeciesRecord> = emptyMap(),
    val ownedGear: Set<Int> = setOf(Gear.LIGHT_ROD),
    val equippedGear: Int = Gear.LIGHT_ROD
) {
    val balance: Int
        get() = (totalPoints - spentPoints).coerceAtLeast(0)

    val xp: Int
        get() = totalPoints / GameConfig.XP_PER_POINT_DIVISOR

    val level: Int
        get() = LevelMath.levelFor(xp)

    val discoveredSpecies: Int
        get() = records.size

    val rarestSpeciesId: Int
        get() = records.keys.maxOrNull() ?: 0
}

object LevelMath {
    private const val MAX_LEVEL = 60
    private val base = intArrayOf(0, 200, 500, 900, 1400, 2000)

    fun thresholdFor(level: Int): Int {
        val index = level - 1
        if (index < base.size) return base[index.coerceAtLeast(0)]
        return base[base.size - 1] + (index - base.size + 1) * 700
    }

    fun levelFor(xp: Int): Int {
        var level = 1
        while (level < MAX_LEVEL && xp >= thresholdFor(level + 1)) {
            level++
        }
        return level
    }

    fun floorXp(level: Int): Int = thresholdFor(level)

    fun ceilingXp(level: Int): Int = thresholdFor(level + 1)

    fun progress(xp: Int): Float {
        val level = levelFor(xp)
        val floor = floorXp(level)
        val ceiling = ceilingXp(level)
        if (ceiling <= floor) return 1f
        return ((xp - floor).toFloat() / (ceiling - floor).toFloat()).coerceIn(0f, 1f)
    }
}
