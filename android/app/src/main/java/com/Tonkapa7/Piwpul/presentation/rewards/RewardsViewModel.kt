package com.Tonkapa7.Piwpul.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.usecase.UnlockGearUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GearRow(
    val id: Int,
    val cost: Int,
    val owned: Boolean,
    val equipped: Boolean,
    val equippable: Boolean,
    val affordable: Boolean,
    val balance: Int
) {
    val ratio: Float
        get() = if (cost <= 0) 1f else (balance.toFloat() / cost).coerceIn(0f, 1f)
}

data class RewardsUiState(
    val balance: Int = 0,
    val gear: List<GearRow> = emptyList()
)

class RewardsViewModel(
    anglerRepository: AnglerRepository,
    private val unlockGear: UnlockGearUseCase
) : ViewModel() {

    val state: StateFlow<RewardsUiState> = anglerRepository.profile
        .map { profile ->
            RewardsUiState(
                balance = profile.balance,
                gear = SampleData.gear.map { gear ->
                    GearRow(
                        id = gear.id,
                        cost = gear.cost,
                        owned = profile.ownedGear.contains(gear.id),
                        equipped = profile.equippedGear == gear.id,
                        equippable = gear.equippable,
                        affordable = profile.balance >= gear.cost,
                        balance = profile.balance
                    )
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = RewardsUiState()
        )

    fun unlock(gearId: Int) {
        unlockGear(gearId)
    }

    fun equip(gearId: Int) {
        unlockGear.equip(gearId)
    }
}
