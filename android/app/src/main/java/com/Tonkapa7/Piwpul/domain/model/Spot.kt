package com.Tonkapa7.Piwpul.domain.model

data class Spot(
    val id: Int,
    val depthDecimetres: Int
) {
    val depthLabel: String
        get() = "${depthDecimetres / 10}.${depthDecimetres % 10}"
}
