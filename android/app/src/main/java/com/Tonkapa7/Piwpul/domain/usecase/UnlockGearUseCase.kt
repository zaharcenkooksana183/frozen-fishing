package com.Tonkapa7.Piwpul.domain.usecase

import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository

class UnlockGearUseCase(private val repository: AnglerRepository) {

    operator fun invoke(gearId: Int): Boolean {
        val gear = SampleData.gear.firstOrNull { it.id == gearId } ?: return false
        val profile = repository.profile.value
        if (profile.ownedGear.contains(gearId)) return false
        if (profile.balance < gear.cost) return false
        repository.saveProfile(
            profile.copy(
                spentPoints = profile.spentPoints + gear.cost,
                ownedGear = profile.ownedGear + gearId,
                equippedGear = if (gear.equippable) gearId else profile.equippedGear
            )
        )
        return true
    }

    fun equip(gearId: Int): Boolean {
        val gear = SampleData.gear.firstOrNull { it.id == gearId } ?: return false
        if (!gear.equippable) return false
        val profile = repository.profile.value
        if (!profile.ownedGear.contains(gearId)) return false
        if (profile.equippedGear == gearId) return false
        repository.saveProfile(profile.copy(equippedGear = gearId))
        return true
    }
}
