package com.Tonkapa7.Piwpul.presentation.progress

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.IceSecondary
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SnowWhite
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.theme.WarnAmber
import com.Tonkapa7.Piwpul.core.ui.EmptyState
import com.Tonkapa7.Piwpul.core.ui.FrostCard
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenHeader
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SectionHeader
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.core.ui.StatEntry
import com.Tonkapa7.Piwpul.core.ui.StatRow
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun ProgressScreen(onHome: () -> Unit) {
    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProgressContent(state = state, onHome = onHome)
}

@Composable
private fun ProgressContent(state: ProgressUiState, onHome: () -> Unit) {
    ScreenBackground(overlay = ScreenOverlay.hub) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.progress_title),
                subtitle = stringResource(
                    R.string.progress_next_format,
                    stringResource(SpeciesVisuals.rank(state.level + 1))
                ),
                onHome = onHome
            )
            Spacer(modifier = Modifier.height(10.dp))
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Brush.linearGradient(listOf(LakeBlue, LakeBlueDeep)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.level.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = SnowWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(SpeciesVisuals.rank(state.level)),
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(
                                R.string.menu_xp_format,
                                state.xpInLevel,
                                state.xpForLevel
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateInk
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { state.xpProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = LakeBlue,
                            trackColor = IceSecondary,
                            gapSize = 0.dp,
                            drawStopIndicator = {}
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            StatRow(
                entries = listOf(
                    StatEntry(
                        value = state.totalTrips.toString(),
                        label = stringResource(R.string.stat_trips),
                        accent = LakeBlue,
                        visible = state.totalTrips > 0
                    ),
                    StatEntry(
                        value = state.totalCatches.toString(),
                        label = stringResource(R.string.stat_catches),
                        accent = SuccessTeal,
                        visible = state.totalCatches > 0
                    ),
                    StatEntry(
                        value = state.bestSession.toString(),
                        label = stringResource(R.string.stat_best),
                        accent = WarnAmber,
                        visible = state.bestSession > 0
                    )
                )
            )
            if (state.rarestSpeciesId > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.stat_rarest),
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateInk
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(
                                SpeciesVisuals.name(state.rarestSpeciesId)
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepNavy
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = stringResource(R.string.progress_trip_log),
                trailing = if (state.trips.isEmpty()) {
                    stringResource(R.string.progress_none)
                } else {
                    null
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (state.trips.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.List,
                    title = stringResource(R.string.progress_empty_title),
                    subtitle = stringResource(R.string.progress_empty_subtitle)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 20.dp)
                ) {
                    items(state.trips, key = { it.id }) { trip ->
                        TripCard(trip = trip)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TripCard(trip: TripRow) {
    FrostCard(modifier = Modifier.fillMaxWidth(), contentPadding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(SpeciesVisuals.spotName(trip.spotId)),
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepNavy
                )
                Text(
                    text = trip.dateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk
                )
            }
            Text(
                text = stringResource(
                    R.string.progress_trip_format,
                    trip.catches,
                    trip.points
                ),
                style = MaterialTheme.typography.labelSmall,
                color = LakeBlueDeep
            )
        }
        if (trip.note.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = trip.note,
                style = MaterialTheme.typography.bodyMedium,
                color = SlateInk
            )
        }
    }
}
