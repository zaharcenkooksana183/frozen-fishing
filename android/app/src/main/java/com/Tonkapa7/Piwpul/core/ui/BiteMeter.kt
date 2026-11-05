package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.core.theme.FrostAccent
import com.Tonkapa7.Piwpul.core.theme.IceSecondary
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.MissCoral
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import kotlin.random.Random

@Composable
fun BiteMeter(
    progress: Float,
    active: Boolean,
    missed: Boolean,
    description: String,
    modifier: Modifier = Modifier
) {
    val fill = Brush.horizontalGradient(listOf(FrostAccent, LakeBlue))
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .semantics {
                stateDescription = description
                liveRegion = LiveRegionMode.Polite
            }
    ) {
        val radius = CornerRadius(size.height / 2f, size.height / 2f)
        drawRoundRect(color = IceSecondary, cornerRadius = radius)
        if (active) {
            drawRoundRect(
                color = SuccessTeal.copy(alpha = 0.35f),
                topLeft = Offset(size.width * 0.25f, 0f),
                size = Size(size.width * 0.6f, size.height),
                cornerRadius = radius
            )
        }
        val width = size.width * progress.coerceIn(0f, 1f)
        if (width > 0f) {
            if (missed) {
                drawRoundRect(
                    color = MissCoral,
                    size = Size(width, size.height),
                    cornerRadius = radius
                )
            } else {
                drawRoundRect(
                    brush = fill,
                    size = Size(width, size.height),
                    cornerRadius = radius,
                    alpha = if (active) 1f else 0.45f
                )
            }
        }
    }
}

@Composable
fun Snowfall(
    modifier: Modifier = Modifier,
    flakes: Int = 14,
    color: Color = Color.White,
    durationMillis: Int = 9000
) {
    val transition = rememberInfiniteTransition(label = "snowfall")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis, easing = LinearEasing)),
        label = "snowDrift"
    )
    val seeds = remember(flakes) {
        val random = Random(flakes * 31)
        List(flakes) {
            FlakeSeed(
                x = random.nextFloat(),
                phase = random.nextFloat(),
                speed = 0.5f + random.nextFloat(),
                radius = 1.5f + random.nextFloat() * 2.5f,
                alpha = 0.15f + random.nextFloat() * 0.25f
            )
        }
    }
    Canvas(modifier = modifier) {
        seeds.forEach { seed ->
            val travel = (drift * seed.speed + seed.phase) % 1f
            val sway = kotlin.math.sin((travel + seed.phase) * 6.28f) * 12f
            drawCircle(
                color = color.copy(alpha = seed.alpha),
                radius = seed.radius * density,
                center = Offset(seed.x * size.width + sway, travel * size.height)
            )
        }
    }
}

private data class FlakeSeed(
    val x: Float,
    val phase: Float,
    val speed: Float,
    val radius: Float,
    val alpha: Float
)
