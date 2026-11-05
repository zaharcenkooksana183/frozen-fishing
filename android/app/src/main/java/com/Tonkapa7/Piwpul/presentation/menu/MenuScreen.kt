package com.Tonkapa7.Piwpul.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.FrostAccent
import com.Tonkapa7.Piwpul.core.theme.IceSecondary
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SnowWhite
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.theme.WarnAmber
import com.Tonkapa7.Piwpul.core.ui.FrostCard
import com.Tonkapa7.Piwpul.core.ui.PrimaryButton
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.Snowfall
import com.Tonkapa7.Piwpul.core.ui.SpeciesStrip
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun MenuScreen(
    onStart: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onStart = onStart,
        onDaily = onDaily,
        onProgress = onProgress,
        onCollection = onCollection,
        onRewards = onRewards,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onStart: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    ScreenBackground(overlay = ScreenOverlay.menu) {
        if (state.snowfall) {
            Snowfall(
                modifier = Modifier.fillMaxSize(),
                flakes = 10,
                color = SnowWhite,
                durationMillis = 11000
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            MenuHeader(onSettings = onSettings)
            Spacer(modifier = Modifier.height(10.dp))
            AnglerCard(state = state)
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                StartTile(
                    tripsToday = state.tripsToday,
                    onClick = onStart,
                    modifier = Modifier.weight(1.6f).fillMaxHeight()
                )
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    BentoTile(
                        icon = Icons.Default.Star,
                        accent = WarnAmber,
                        title = stringResource(R.string.menu_tile_daily),
                        caption = stringResource(
                            R.string.menu_daily_progress_format,
                            state.dailyProgress,
                            state.dailyTarget
                        ),
                        onClick = onDaily,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    BentoTile(
                        icon = Icons.Default.DateRange,
                        accent = LakeBlue,
                        title = stringResource(R.string.menu_tile_progress),
                        caption = stringResource(
                            R.string.menu_catches_format,
                            state.totalCatches
                        ),
                        onClick = onProgress,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(104.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CollectionTile(
                    state = state,
                    onClick = onCollection,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                BentoTile(
                    icon = Icons.Default.Build,
                    accent = if (state.gearAffordable) SuccessTeal else SlateInk,
                    title = stringResource(R.string.menu_tile_rewards),
                    caption = if (state.lockedGear > 0) {
                        stringResource(R.string.menu_gear_locked_format, state.lockedGear)
                    } else {
                        stringResource(R.string.menu_gear_ready)
                    },
                    onClick = onRewards,
                    showDot = state.gearAffordable,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelSmall,
                color = SlateInk,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            PrimaryButton(
                text = stringResource(R.string.cta_start),
                onClick = onStart,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MenuHeader(onSettings: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleMedium,
                color = DeepNavy
            )
            Text(
                text = stringResource(R.string.menu_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = SlateInk
            )
        }
        IconButton(onClick = onSettings, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_title),
                tint = LakeBlueDeep,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun AnglerCard(state: MenuUiState) {
    FrostCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(listOf(LakeBlue, LakeBlueDeep)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.level.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = SnowWhite
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(
                        R.string.menu_level_format,
                        state.level,
                        stringResource(SpeciesVisuals.rank(state.level))
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepNavy,
                    maxLines = 1
                )
                Text(
                    text = if (state.hasTrips) {
                        stringResource(
                            R.string.menu_xp_format,
                            state.xpInLevel,
                            state.xpForLevel
                        )
                    } else {
                        stringResource(R.string.menu_angler_empty)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .background(FrostAccent.copy(alpha = 0.22f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = LakeBlueDeep,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.menu_points_format, state.balance),
                    style = MaterialTheme.typography.labelSmall,
                    color = LakeBlueDeep
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { state.xpProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = LakeBlue,
            trackColor = IceSecondary,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
    }
}

@Composable
private fun StartTile(
    tripsToday: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostCard(
        modifier = modifier,
        containerColor = Color.Transparent,
        borderColor = Color.Transparent,
        elevation = 8.dp,
        contentPadding = 0.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(LakeBlue, LakeBlueDeep)))
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = SnowWhite,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = stringResource(R.string.menu_tile_start),
                        style = MaterialTheme.typography.headlineMedium,
                        color = SnowWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.menu_trips_format, tripsToday),
                        style = MaterialTheme.typography.labelSmall,
                        color = SnowWhite.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BentoTile(
    icon: ImageVector,
    accent: Color,
    title: String,
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDot: Boolean = false
) {
    FrostCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = 12.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = DeepNavy,
                maxLines = 1
            )
            if (showDot) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(WarnAmber, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = caption,
            style = MaterialTheme.typography.titleMedium,
            color = accent,
            maxLines = 1
        )
    }
}

@Composable
private fun CollectionTile(
    state: MenuUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = 12.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                tint = SuccessTeal,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.menu_tile_collection),
                style = MaterialTheme.typography.labelSmall,
                color = DeepNavy,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        if (state.discoveredIds.isEmpty()) {
            SpeciesStrip(
                speciesIds = listOf(1, 2, 3, 4, 5),
                modifier = Modifier.fillMaxWidth(),
                spriteSize = 22.dp
            )
        } else {
            SpeciesStrip(
                speciesIds = state.discoveredIds,
                modifier = Modifier.fillMaxWidth(),
                spriteSize = 26.dp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(
                R.string.menu_species_format,
                state.discoveredCount,
                state.speciesTotal
            ),
            style = MaterialTheme.typography.labelSmall,
            color = if (state.discoveredCount > 0) SuccessTeal else MutedInk,
            maxLines = 1
        )
    }
}
