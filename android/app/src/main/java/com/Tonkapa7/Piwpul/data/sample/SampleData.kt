package com.Tonkapa7.Piwpul.data.sample

import com.Tonkapa7.Piwpul.domain.model.ChallengeKind
import com.Tonkapa7.Piwpul.domain.model.ChallengeTemplate
import com.Tonkapa7.Piwpul.domain.model.Gear
import com.Tonkapa7.Piwpul.domain.model.Rarity
import com.Tonkapa7.Piwpul.domain.model.Species
import com.Tonkapa7.Piwpul.domain.model.Spot

object SampleData {

    val species: List<Species> = listOf(
        Species(id = 1, rarity = Rarity.COMMON, points = 10, weight = 40, minLengthCm = 14, maxLengthCm = 24),
        Species(id = 2, rarity = Rarity.COMMON, points = 15, weight = 25, minLengthCm = 16, maxLengthCm = 28),
        Species(id = 3, rarity = Rarity.UNCOMMON, points = 30, weight = 20, minLengthCm = 28, maxLengthCm = 42),
        Species(id = 4, rarity = Rarity.RARE, points = 60, weight = 11, minLengthCm = 45, maxLengthCm = 72),
        Species(id = 5, rarity = Rarity.LEGENDARY, points = 120, weight = 4, minLengthCm = 38, maxLengthCm = 60)
    )

    val spots: List<Spot> = listOf(
        Spot(id = 1, depthDecimetres = 42),
        Spot(id = 2, depthDecimetres = 28),
        Spot(id = 3, depthDecimetres = 96),
        Spot(id = 4, depthDecimetres = 61),
        Spot(id = 5, depthDecimetres = 35)
    )

    val gear: List<Gear> = listOf(
        Gear(id = Gear.LIGHT_ROD, cost = 0, equippable = true),
        Gear(id = Gear.TUNGSTEN_JIG, cost = 300, equippable = true),
        Gear(id = Gear.SONAR_UNIT, cost = 900, equippable = true),
        Gear(id = Gear.THERMAL_TENT, cost = 1500, equippable = false)
    )

    val challenges: List<ChallengeTemplate> = listOf(
        ChallengeTemplate(id = 1, kind = ChallengeKind.CATCH_SPECIES, target = 3, speciesId = 3),
        ChallengeTemplate(id = 2, kind = ChallengeKind.SCORE_POINTS, target = 150, speciesId = 0),
        ChallengeTemplate(id = 3, kind = ChallengeKind.CATCH_RARE, target = 1, speciesId = 4),
        ChallengeTemplate(id = 4, kind = ChallengeKind.CATCH_SPECIES, target = 4, speciesId = 1),
        ChallengeTemplate(id = 5, kind = ChallengeKind.TOTAL_CATCHES, target = 5, speciesId = 0),
        ChallengeTemplate(id = 6, kind = ChallengeKind.CATCH_SPECIES, target = 2, speciesId = 2)
    )

    fun speciesById(id: Int): Species = species.firstOrNull { it.id == id } ?: species.first()

    fun spotById(id: Int): Spot = spots.firstOrNull { it.id == id } ?: spots.first()

    fun templateForDay(dayIndex: Long): ChallengeTemplate {
        val index = ((dayIndex % challenges.size) + challenges.size) % challenges.size
        return challenges[index.toInt()]
    }
}
