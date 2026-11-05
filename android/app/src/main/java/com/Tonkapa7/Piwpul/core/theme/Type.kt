package com.Tonkapa7.Piwpul.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun appTypography(scale: Float = 1f): Typography = Typography(
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = (34 * scale).sp,
        lineHeight = (40 * scale).sp,
        letterSpacing = 1.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = (24 * scale).sp,
        lineHeight = (30 * scale).sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = (18 * scale).sp,
        lineHeight = (24 * scale).sp,
        letterSpacing = 0.3.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = (16 * scale).sp,
        lineHeight = (22 * scale).sp,
        letterSpacing = 0.1.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = (14 * scale).sp,
        lineHeight = (20 * scale).sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = (15 * scale).sp,
        lineHeight = (20 * scale).sp,
        letterSpacing = 1.0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = (11 * scale).sp,
        lineHeight = (15 * scale).sp,
        letterSpacing = 1.2.sp
    )
)

val AppTypography: Typography = appTypography()
