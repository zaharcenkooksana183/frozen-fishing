package com.Tonkapa7.Piwpul.presentation.daily

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.Tonkapa7.Piwpul.core.theme.IceSecondary
import com.Tonkapa7.Piwpul.core.theme.IceSurfaceAlt
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SnowWhite
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.theme.WarnAmber
import com.Tonkapa7.Piwpul.core.ui.FrostCard
import com.Tonkapa7.Piwpul.core.ui.PrimaryButton
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenHeader
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SectionHeader
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.domain.model.ChallengeKind
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun DailyChallengeScreen(
    onHome: () -> Unit,
    onStart: () -> Unit
) {
    val viewModel: DailyChallengeViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    DailyContent(
        state = state,
        onHome = onHome,
        onStart = onStart,
        onClaim = viewModel::claimReward
    )
}

@Composable
private fun DailyContent(
    state: DailyUiState,
    onHome: () -> Unit,
    onStart: () -> Unit,
    onClaim: () -> Unit
) {
    val goal = when (state.kind) {
        ChallengeKind.CATCH_SPECIES -> stringResource(
            R.string.daily_goal_species_format,
            state.target,
            stringResource(SpeciesVisuals.name(state.speciesId))
        )
        ChallengeKind.SCORE_POINTS -> stringResource(
            R.string.daily_goal_score_format,
            state.target
        )
        ChallengeKind.CATCH_RARE -> stringResource(
            R.string.daily_goal_rare_format,
            state.target
        )
        ChallengeKind.TOTAL_CATCHES -> stringResource(
            R.string.daily_goal_total_format,
            state.target
        )
    }
    ScreenBackground(overlay = ScreenOverlay.hub) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.daily_title),
                subtitle = stringResource(R.string.daily_reward_format, state.reward),
                onHome = onHome
            )
            Spacer(modifier = Modifier.height(10.dp))
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(SpeciesVisuals.sprite(speciesForKind(state))),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = goal,
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.daily_progress_format,
                                state.progress,
                                state.target
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (state.complete) SuccessTeal else SlateInk
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { state.ratio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (state.complete) SuccessTeal else LakeBlue,
                            trackColor = IceSecondary,
                            gapSize = 0.dp,
                            drawStopIndicator = {}
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = stringResource(R.string.daily_streak_title),
                trailing = stringResource(R.string.menu_points_format, state.reward)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.weekDone.forEachIndexed { index, done ->
                    StreakCell(
                        done = done,
                        today = index == state.weekDone.lastIndex,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WarnAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.stat_streak),
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateInk
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = state.streak.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = WarnAmber
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (state.claimed) {
                Text(
                    text = stringResource(R.string.daily_tomorrow),
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                )
                PrimaryButton(
                    text = stringResource(R.string.cta_claimed),
                    onClick = onClaim,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (state.complete) {
                Text(
                    text = stringResource(R.string.daily_complete),
                    style = MaterialTheme.typography.labelSmall,
                    color = SuccessTeal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                )
                PrimaryButton(
                    text = stringResource(R.string.cta_claim),
                    onClick = onClaim,
                    icon = Icons.Default.Check,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
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
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StreakCell(done: Boolean, today: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(36.dp)
            .background(
                if (done) SuccessTeal else IceSurfaceAlt,
                RoundedCornerShape(10.dp)
            )
            .border(
                width = if (today) 2.dp else 0.dp,
                color = if (today) LakeBlue else IceSurfaceAlt,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (done) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = SnowWhite,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(MutedInk, RoundedCornerShape(3.dp))
            )
        }
    }
}

private fun speciesForKind(state: DailyUiState): Int = when (state.kind) {
    ChallengeKind.CATCH_SPECIES -> state.speciesId
    ChallengeKind.CATCH_RARE -> 4
    ChallengeKind.SCORE_POINTS -> 5
    ChallengeKind.TOTAL_CATCHES -> 2
}
