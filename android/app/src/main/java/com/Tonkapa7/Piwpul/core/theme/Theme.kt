package com.Tonkapa7.Piwpul.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val FrozenColorScheme: ColorScheme = lightColorScheme(
    primary = LakeBlue,
    onPrimary = SnowWhite,
    primaryContainer = IceSurfaceAlt,
    onPrimaryContainer = DeepNavy,
    secondary = FrostAccent,
    onSecondary = DeepNavy,
    secondaryContainer = IceSecondary,
    onSecondaryContainer = DeepNavy,
    tertiary = SuccessTeal,
    onTertiary = SnowWhite,
    background = IceBackground,
    onBackground = DeepNavy,
    surface = IceSurface,
    onSurface = DeepNavy,
    surfaceVariant = IceSurfaceAlt,
    onSurfaceVariant = SlateInk,
    outline = MutedInk,
    outlineVariant = IceSecondary,
    error = MissCoral,
    onError = SnowWhite
)

@Composable
fun AppTheme(
    typography: Typography = AppTypography,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FrozenColorScheme,
        typography = typography,
        content = content
    )
}
