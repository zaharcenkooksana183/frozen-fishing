package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.ui.graphics.Color
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.theme.WarnAmber
import com.Tonkapa7.Piwpul.domain.model.Gear
import com.Tonkapa7.Piwpul.domain.model.Rarity

object SpeciesVisuals {

    fun sprite(speciesId: Int): Int = when (speciesId) {
        1 -> R.drawable.sprite_1
        2 -> R.drawable.sprite_2
        3 -> R.drawable.sprite_3
        4 -> R.drawable.sprite_4
        else -> R.drawable.sprite_5
    }

    fun name(speciesId: Int): Int = when (speciesId) {
        1 -> R.string.species_1
        2 -> R.string.species_2
        3 -> R.string.species_3
        4 -> R.string.species_4
        else -> R.string.species_5
    }

    fun rarityLabel(rarity: Rarity): Int = when (rarity) {
        Rarity.COMMON -> R.string.rarity_common
        Rarity.UNCOMMON -> R.string.rarity_uncommon
        Rarity.RARE -> R.string.rarity_rare
        Rarity.LEGENDARY -> R.string.rarity_legendary
    }

    fun rarityColor(rarity: Rarity): Color = when (rarity) {
        Rarity.COMMON -> MutedInk
        Rarity.UNCOMMON -> LakeBlue
        Rarity.RARE -> SuccessTeal
        Rarity.LEGENDARY -> WarnAmber
    }

    fun spotName(spotId: Int): Int = when (spotId) {
        1 -> R.string.spot_1
        2 -> R.string.spot_2
        3 -> R.string.spot_3
        4 -> R.string.spot_4
        else -> R.string.spot_5
    }

    fun spotHint(spotId: Int): Int = when (spotId) {
        1 -> R.string.spot_1_hint
        2 -> R.string.spot_2_hint
        3 -> R.string.spot_3_hint
        4 -> R.string.spot_4_hint
        else -> R.string.spot_5_hint
    }

    fun gearName(gearId: Int): Int = when (gearId) {
        Gear.LIGHT_ROD -> R.string.gear_1
        Gear.TUNGSTEN_JIG -> R.string.gear_2
        Gear.SONAR_UNIT -> R.string.gear_3
        else -> R.string.gear_4
    }

    fun gearShortName(gearId: Int): Int = when (gearId) {
        Gear.LIGHT_ROD -> R.string.game_gear_light_rod
        Gear.TUNGSTEN_JIG -> R.string.game_gear_jig
        else -> R.string.game_gear_sonar
    }

    fun gearEffect(gearId: Int): Int = when (gearId) {
        Gear.LIGHT_ROD -> R.string.gear_1_effect
        Gear.TUNGSTEN_JIG -> R.string.gear_2_effect
        Gear.SONAR_UNIT -> R.string.gear_3_effect
        else -> R.string.gear_4_effect
    }

    fun rank(level: Int): Int = when (level) {
        1 -> R.string.rank_1
        2 -> R.string.rank_2
        3 -> R.string.rank_3
        4 -> R.string.rank_4
        5 -> R.string.rank_5
        else -> R.string.rank_6
    }
}
