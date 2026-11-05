package com.Tonkapa7.Piwpul.domain.usecase

import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.model.Catch
import com.Tonkapa7.Piwpul.domain.model.Species
import kotlin.random.Random

class RollCatchUseCase {

    operator fun invoke(sonarEquipped: Boolean, random: Random = Random.Default): Catch {
        val weights = weightsFor(sonarEquipped)
        val total = weights.values.sum()
        var roll = random.nextInt(total)
        var picked: Species = SampleData.species.first()
        for (entry in SampleData.species) {
            val weight = weights.getValue(entry.id)
            if (roll < weight) {
                picked = entry
                break
            }
            roll -= weight
        }
        val length = random.nextInt(picked.minLengthCm, picked.maxLengthCm + 1)
        return Catch(
            speciesId = picked.id,
            points = picked.points,
            lengthCm = length,
            rarity = picked.rarity
        )
    }

    private fun weightsFor(sonarEquipped: Boolean): Map<Int, Int> {
        val base = SampleData.species.associate { it.id to it.weight }.toMutableMap()
        if (!sonarEquipped) return base
        base[1] = (base.getValue(1) - 4).coerceAtLeast(1)
        base[2] = (base.getValue(2) - 2).coerceAtLeast(1)
        base[4] = base.getValue(4) + 4
        base[5] = base.getValue(5) + 2
        return base
    }
}
