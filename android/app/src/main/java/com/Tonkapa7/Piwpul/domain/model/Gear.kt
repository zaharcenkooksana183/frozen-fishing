package com.Tonkapa7.Piwpul.domain.model

data class Gear(
    val id: Int,
    val cost: Int,
    val equippable: Boolean
) {
    companion object {
        const val LIGHT_ROD = 1
        const val TUNGSTEN_JIG = 2
        const val SONAR_UNIT = 3
        const val THERMAL_TENT = 4
    }
}
