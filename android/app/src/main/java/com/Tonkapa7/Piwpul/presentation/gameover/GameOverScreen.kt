package com.Tonkapa7.Piwpul.presentation.gameover

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.theme.WarnAmber
import com.Tonkapa7.Piwpul.core.ui.EmptyState
import com.Tonkapa7.Piwpul.core.ui.FrostCard
import com.Tonkapa7.Piwpul.core.ui.PrimaryButton
import com.Tonkapa7.Piwpul.core.ui.RarityChip
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SecondaryButton
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.core.ui.StatEntry
import com.Tonkapa7.Piwpul.core.ui.StatRow
import com.Tonkapa7.Piwpul.domain.model.SessionSummary
import com.Tonkapa7.Piwpul.presentation.dialog.SpotNoteDialog
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun GameOverScreen(
    summary: SessionSummary,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val note = if (state.noteTripId == summary.tripId) state.note else summary.note

    GameOverContent(
        summary = summary,
        note = note,
        onAddNote = viewModel::openNoteDialog,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )

    if (state.noteDialogOpen) {
        SpotNoteDialog(
            initialNote = note,
            onDismiss = viewModel::dismissNoteDialog,
            onSave = { text -> viewModel.saveNote(summary.tripId, text) }
        )
    }
}

@Composable
private fun GameOverContent(
    summary: SessionSummary,
    note: String,
    onAddNote: () -> Unit,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val best = summary.catches.maxByOrNull { it.points }
    ScreenBackground(overlay = ScreenOverlay.report) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.report_title),
                style = MaterialTheme.typography.displaySmall,
                color = DeepNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.report_subtitle_format,
                    stringResource(SpeciesVisuals.spotName(summary.spotId)),
                    summary.casts
                ),
                style = MaterialTheme.typography.labelSmall,
                color = SlateInk
            )
            Spacer(modifier = Modifier.height(16.dp))
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                if (best == null) {
                    EmptyState(
                        icon = Icons.Default.Search,
                        title = stringResource(R.string.report_empty_title),
                        subtitle = stringResource(R.string.report_empty_subtitle)
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            SuccessTeal.copy(alpha = 0.22f),
                                            Color.Transparent
                                        )
                                    ),
                                    CircleShape
                                )
                        )
                        Image(
                            painter = painterResource(SpeciesVisuals.sprite(best.speciesId)),
                            contentDescription = stringResource(
                                SpeciesVisuals.name(best.speciesId)
                            ),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(140.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(SpeciesVisuals.name(best.speciesId)),
                        style = MaterialTheme.typography.headlineMedium,
                        color = DeepNavy,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RarityChip(
                            rarity = best.rarity,
                            label = stringResource(SpeciesVisuals.rarityLabel(best.rarity))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                R.string.game_length_cm_format,
                                best.lengthCm
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateInk
                        )
                    }
                }
            }
            if (summary.newRecord || summary.newSpecies) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (summary.newRecord) {
                        Ribbon(
                            text = stringResource(R.string.report_new_record),
                            tint = WarnAmber
                        )
                    }
                    if (summary.newSpecies) {
                        Ribbon(
                            text = stringResource(R.string.report_new_species),
                            tint = SuccessTeal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            StatRow(
                entries = listOf(
                    StatEntry(
                        value = summary.catchCount.toString(),
                        label = stringResource(R.string.stat_catches),
                        accent = SuccessTeal,
                        visible = summary.catchCount > 0
                    ),
                    StatEntry(
                        value = stringResource(R.string.report_points_format, summary.points),
                        label = stringResource(R.string.stat_points),
                        accent = LakeBlue,
                        visible = summary.points > 0
                    ),
                    StatEntry(
                        value = summary.bestPoints.toString(),
                        label = stringResource(R.string.stat_best),
                        accent = WarnAmber,
                        visible = summary.bestPoints > 0
                    )
                )
            )
            if (note.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.report_note_title),
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateInk
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepNavy
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            SecondaryButton(
                text = stringResource(R.string.cta_add_note),
                onClick = onAddNote,
                icon = Icons.Default.Edit,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.cta_again),
                onClick = onPlayAgain,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun Ribbon(text: String, tint: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = tint,
        modifier = Modifier
            .background(tint.copy(alpha = 0.14f), RoundedCornerShape(12.dp))
            .border(1.dp, tint.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
