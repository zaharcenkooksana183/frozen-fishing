package com.Tonkapa7.Piwpul.presentation.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.theme.IceSecondary
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenHeader
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SpeciesTile
import com.Tonkapa7.Piwpul.presentation.dialog.SpeciesDetailDialog

@Composable
fun CollectionScreen(onHome: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectionContent(
        state = state,
        onHome = onHome,
        onSelect = viewModel::select
    )

    val selected = state.entries.firstOrNull { it.id == state.selectedId && it.discovered }
    if (selected != null) {
        SpeciesDetailDialog(
            speciesId = selected.id,
            rarity = selected.rarity,
            points = selected.points,
            count = selected.count,
            bestLengthCm = selected.bestLengthCm,
            firstCaughtLabel = selected.firstCaughtLabel,
            metric = state.metric,
            onDismiss = viewModel::dismiss
        )
    }
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onHome: () -> Unit,
    onSelect: (Int) -> Unit
) {
    ScreenBackground(overlay = ScreenOverlay.hub) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.collection_title),
                subtitle = stringResource(
                    R.string.collection_header_format,
                    state.discoveredCount,
                    state.total
                ),
                onHome = onHome
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { state.ratio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SuccessTeal,
                trackColor = IceSecondary,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
            Spacer(modifier = Modifier.height(14.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.entries, key = { it.id }) { entry ->
                    SpeciesTile(
                        speciesId = entry.id,
                        rarity = entry.rarity,
                        discovered = entry.discovered,
                        caption = if (entry.discovered) {
                            stringResource(R.string.collection_caught_format, entry.count)
                        } else {
                            stringResource(R.string.collection_locked_hint)
                        },
                        spriteSize = 80.dp,
                        onClick = { onSelect(entry.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (state.discoveredCount == 0) {
                    stringResource(R.string.collection_locked_hint)
                } else {
                    stringResource(
                        R.string.collection_header_format,
                        state.discoveredCount,
                        state.total
                    )
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (state.discoveredCount == 0) MutedInk else SlateInk,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }
    }
}
