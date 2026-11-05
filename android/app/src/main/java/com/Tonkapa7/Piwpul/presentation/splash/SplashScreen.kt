package com.Tonkapa7.Piwpul.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.theme.FrostAccent
import com.Tonkapa7.Piwpul.core.theme.SnowWhite
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.Snowfall

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    val transition = rememberInfiniteTransition(label = "splash")
    val logoScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )
    val haloAlpha by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloAlpha"
    )
    val titleGlow by transition.animateFloat(
        initialValue = 0.78f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "titleGlow"
    )

    ScreenBackground(overlay = ScreenOverlay.splash) {
        Snowfall(
            modifier = Modifier.fillMaxSize(),
            flakes = 14,
            color = SnowWhite
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = FrostAccent.copy(alpha = haloAlpha * 0.5f),
                        radius = size.minDimension / 2f
                    )
                    drawCircle(
                        color = FrostAccent.copy(alpha = haloAlpha),
                        radius = size.minDimension / 2.6f
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            scaleX = logoScale
                            scaleY = logoScale
                        }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displaySmall,
                color = SnowWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleGlow)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = SnowWhite.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(bottom = 32.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = SnowWhite.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = FrostAccent,
                trackColor = Color(0x4DFFFFFF)
            )
        }
    }
}
