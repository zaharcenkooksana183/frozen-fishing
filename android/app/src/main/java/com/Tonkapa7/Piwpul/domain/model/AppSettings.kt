package com.Tonkapa7.Piwpul.domain.model

data class AppSettings(
    val sound: Boolean = true,
    val haptics: Boolean = true,
    val snowfall: Boolean = true,
    val largeText: Boolean = false,
    val metric: Boolean = true
)
