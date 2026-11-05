package com.Tonkapa7.Piwpul.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.theme.FrostAccent
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.LakeBlueAbyss
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.MissCoral
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SnowWhite
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.theme.WarnAmber
import com.Tonkapa7.Piwpul.core.ui.BiteMeter
import com.Tonkapa7.Piwpul.core.ui.PauseButton
import com.Tonkapa7.Piwpul.core.ui.PrimaryButton
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenHeader
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.core.ui.StatEntry
import com.Tonkapa7.Piwpul.core.ui.StatRow
import com.Tonkapa7.Piwpul.domain.model.Gear
import com.Tonkapa7.Piwpul.domain.model.SessionSummary
import com.Tonkapa7.Piwpul.presentation.dialog.PauseDialog
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun GameScreen(
    token: Long,
    onExit: () -> Unit,
    onFinished: (SessionSummary) -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        viewModel.startSession(token)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.summary)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pauseSession() }
    }

    GameContent(
        state = state,
        onPrimaryAction = viewModel::onPrimaryAction,
        onPause = viewModel::pauseSession,
        onResume = viewModel::resumeSession,
        onEquip = viewModel::equipGear,
        onExit = onExit
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onPrimaryAction: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onEquip: (Int) -> Unit,
    onExit: () -> Unit
) {
    ScreenBackground(overlay = ScreenOverlay.game) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.game_cast_format, state.cast, state.totalCasts),
                subtitle = stringResource(
                    R.string.game_spot_format,
                    stringResource(SpeciesVisuals.spotName(state.spotId))
                ),
                onHome = onExit,
                modifier = Modifier.padding(horizontal = 8.dp),
                trailing = { PauseButton(onClick = onPause) }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IceHole(state = state)
            }
            ControlsPanel(
                state = state,
                onPrimaryAction = onPrimaryAction,
                onEquip = onEquip
            )
        }
        if (state.paused) {
            PauseDialog(onResume = onResume, onMenu = onExit)
        }
    }
}

@Composable
private fun IceHole(state: GameUiState) {
    val configuration = LocalConfiguration.current
    val holeSize = minOf(configuration.screenWidthDp - 72, 290).dp
    val transition = rememberInfiniteTransition(label = "hole")
    val ripple by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple"
    )
    val shake by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.MISSED) 1f else 0f,
        animationSpec = tween(220),
        label = "shake"
    )
    val revealRise by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.REVEAL) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "revealRise"
    )

    Box(
        modifier = Modifier.size(holeSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationX = shake * 6f }
        ) {
            val radius = size.minDimension / 2f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LakeBlueDeep, LakeBlueAbyss),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = radius
                ),
                radius = radius
            )
            drawCircle(
                color = FrostAccent.copy(alpha = 0.6f),
                radius = radius - 3f,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f * density)
            )
            if (state.phase == GamePhase.WAITING || state.phase == GamePhase.BITE) {
                listOf(0f, 0.33f, 0.66f).forEach { offset ->
                    val wave = ((ripple + offset) % 1f)
                    drawCircle(
                        color = FrostAccent.copy(alpha = (1f - wave) * 0.4f),
                        radius = radius * (0.35f + wave * 0.6f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f * density)
                    )
                }
            }
            val bobY = size.height * (0.16f + 0.58f * state.bobProgress)
            if (state.phase != GamePhase.IDLE) {
                drawLine(
                    color = FrostAccent.copy(alpha = 0.8f),
                    start = Offset(size.width / 2f, size.height * 0.12f),
                    end = Offset(size.width / 2f, bobY),
                    strokeWidth = 2f * density
                )
                drawCircle(
                    color = if (state.phase == GamePhase.BITE) WarnAmber else FrostAccent,
                    radius = 6f * density,
                    center = Offset(size.width / 2f, bobY)
                )
            }
        }
        if (state.phase == GamePhase.IDLE) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.game_hint_idle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = SnowWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.spot_depth_format,
                        state.spotDepthLabel
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = FrostAccent,
                    textAlign = TextAlign.Center
                )
            }
        }
        AnimatedVisibility(
            visible = state.phase == GamePhase.REVEAL && state.revealSpeciesId > 0,
            enter = fadeIn(tween(220)),
            exit = fadeOut(tween(180))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(SpeciesVisuals.sprite(state.revealSpeciesId)),
                    contentDescription = stringResource(SpeciesVisuals.name(state.revealSpeciesId)),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            translationY = (1f - revealRise) * 120f
                            scaleX = 0.7f + 0.3f * revealRise
                            scaleY = 0.7f + 0.3f * revealRise
                        }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(SpeciesVisuals.name(state.revealSpeciesId)),
                    style = MaterialTheme.typography.titleMedium,
                    color = SnowWhite
                )
                Text(
                    text = stringResource(R.string.game_reveal_points_format, state.revealPoints),
                    style = MaterialTheme.typography.labelLarge,
                    color = SuccessTeal
                )
                Text(
                    text = if (state.metric) {
                        stringResource(R.string.game_length_cm_format, state.revealLengthCm)
                    } else {
                        stringResource(
                            R.string.game_length_in_format,
                            (state.revealLengthCm / 2.54f).toInt()
                        )
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = FrostAccent
                )
            }
        }
        if (state.phase == GamePhase.MISSED) {
            Text(
                text = stringResource(R.string.game_hint_missed),
                style = MaterialTheme.typography.titleMedium,
                color = MissCoral,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ControlsPanel(
    state: GameUiState,
    onPrimaryAction: () -> Unit,
    onEquip: (Int) -> Unit
) {
    val pulseTransition = rememberInfiniteTransition(label = "cta")
    val pulse by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(520, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ctaPulse"
    )
    val hint = when (state.phase) {
        GamePhase.IDLE -> stringResource(SpeciesVisuals.spotHint(state.spotId))
        GamePhase.SINKING -> stringResource(R.string.game_hint_sinking)
        GamePhase.WAITING -> stringResource(R.string.game_hint_waiting)
        GamePhase.BITE -> stringResource(R.string.game_hint_bite)
        GamePhase.MISSED -> stringResource(R.string.game_hint_missed)
        GamePhase.REVEAL -> stringResource(R.string.game_hint_waiting)
        GamePhase.FINISHED -> stringResource(R.string.game_hint_finished)
    }
    val ctaText = when (state.phase) {
        GamePhase.BITE -> stringResource(R.string.cta_set_hook)
        GamePhase.IDLE -> if (state.cast > 1) {
            stringResource(R.string.cta_next_cast)
        } else {
            stringResource(R.string.cta_drop_line)
        }
        GamePhase.MISSED, GamePhase.REVEAL -> stringResource(R.string.cta_next_cast)
        else -> stringResource(R.string.cta_drop_line)
    }
    val meterDescription = if (state.biteActive) {
        stringResource(R.string.state_bite_open)
    } else {
        stringResource(R.string.state_bite_closed)
    }

    Surface(
        color = IceSurface.copy(alpha = 0.94f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            StatRow(
                entries = listOf(
                    StatEntry(
                        value = state.catches.toString(),
                        label = stringResource(R.string.stat_catches),
                        accent = SuccessTeal,
                        visible = state.catches > 0
                    ),
                    StatEntry(
                        value = state.points.toString(),
                        label = stringResource(R.string.stat_points),
                        accent = LakeBlue,
                        visible = state.points > 0
                    ),
                    StatEntry(
                        value = state.bestPoints.toString(),
                        label = stringResource(R.string.stat_best),
                        accent = WarnAmber,
                        visible = state.bestPoints > 0
                    )
                ),
                live = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            BiteMeter(
                progress = state.meterProgress,
                active = state.biteActive,
                missed = state.phase == GamePhase.MISSED,
                description = meterDescription
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val equippedLabel = stringResource(R.string.state_equipped)
                val lockedLabel = stringResource(R.string.state_locked)
                listOf(Gear.LIGHT_ROD, Gear.TUNGSTEN_JIG, Gear.SONAR_UNIT).forEach { gearId ->
                    val owned = state.ownedGear.contains(gearId)
                    val selected = state.equippedGear == gearId
                    val chipState = when {
                        !owned -> lockedLabel
                        selected -> equippedLabel
                        else -> stringResource(SpeciesVisuals.gearShortName(gearId))
                    }
                    FilterChip(
                        selected = selected,
                        enabled = owned,
                        onClick = { onEquip(gearId) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .semantics { stateDescription = chipState },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            labelColor = SlateInk,
                            selectedContainerColor = LakeBlue.copy(alpha = 0.16f),
                            selectedLabelColor = LakeBlueDeep,
                            disabledContainerColor = Color.Transparent,
                            disabledLabelColor = MutedInk
                        ),
                        leadingIcon = if (!owned) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = MutedInk,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            null
                        },
                        label = {
                            Text(
                                text = stringResource(SpeciesVisuals.gearShortName(gearId)),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = if (state.biteActive) SuccessTeal else SlateInk,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )
            PrimaryButton(
                text = ctaText,
                onClick = onPrimaryAction,
                enabled = state.actionEnabled,
                colors = if (state.biteActive) {
                    listOf(FrostAccent, LakeBlue)
                } else {
                    listOf(LakeBlue, LakeBlueDeep)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val factor = if (state.biteActive) pulse else 1f
                        scaleX = factor
                        scaleY = factor
                    }
            )
        }
    }
}
