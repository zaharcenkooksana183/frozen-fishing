package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.IceBackground

object ScreenOverlay {

    val splash: Brush = Brush.verticalGradient(
        listOf(Color(0xE61B6FA8), Color(0xE62D9CDB), Color(0xF2E9F7FF))
    )

    val menu: Brush = Brush.verticalGradient(
        listOf(Color(0xE6E9F7FF), Color(0xCCD7EEF9), Color(0xF2E9F7FF))
    )

    val game: Brush = Brush.verticalGradient(
        listOf(Color(0xD9E9F7FF), Color(0xE0B8E0F2), Color(0xF0D7EEF9))
    )

    val report: Brush = Brush.verticalGradient(
        listOf(Color(0xE6D7EEF9), Color(0xEEE9F7FF), Color(0xF7E9F7FF))
    )

    val hub: Brush = Brush.verticalGradient(
        listOf(Color(0xF0E9F7FF), Color(0xE6D7EEF9), Color(0xF5E9F7FF))
    )
}

@Composable
fun ScreenBackground(
    overlay: Brush,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(IceBackground)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(overlay))
        content()
    }
}
