package com.Tonkapa7.Piwpul.presentation.rewards

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SuccessTeal
import com.Tonkapa7.Piwpul.core.ui.FrostCard
import com.Tonkapa7.Piwpul.core.ui.PrimaryButton
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenHeader
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SecondaryButton
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun RewardsScreen(onHome: () -> Unit) {
    val viewModel: RewardsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    RewardsContent(
        state = state,
        onHome = onHome,
        onUnlock = viewModel::unlock,
        onEquip = viewModel::equip
    )
}

@Composable
private fun RewardsContent(
    state: RewardsUiState,
    onHome: () -> Unit,
    onUnlock: (Int) -> Unit,
    onEquip: (Int) -> Unit
) {
    ScreenBackground(overlay = ScreenOverlay.hub) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.rewards_title),
                subtitle = stringResource(R.string.rewards_hint),
                onHome = onHome,
                trailing = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = LakeBlueDeep,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(
                                R.string.rewards_balance_format,
                                state.balance
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = LakeBlueDeep
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(108.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.gear, key = { it.id }) { gear ->
                    GearCard(
                        gear = gear,
                        onUnlock = { onUnlock(gear.id) },
                        onEquip = { onEquip(gear.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GearCard(
    gear: GearRow,
    onUnlock: () -> Unit,
    onEquip: () -> Unit
) {
    FrostCard(modifier = Modifier.fillMaxWidth(), contentPadding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (gear.owned) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = null,
                tint = if (gear.owned) SuccessTeal else MutedInk,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(SpeciesVisuals.gearName(gear.id)),
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepNavy
                )
                Text(
                    text = stringResource(SpeciesVisuals.gearEffect(gear.id)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateInk
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        when {
            gear.owned && gear.equipped -> Text(
                text = stringResource(R.string.state_equipped),
                style = MaterialTheme.typography.labelSmall,
                color = SuccessTeal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            gear.owned && gear.equippable -> SecondaryButton(
                text = stringResource(R.string.cta_equip),
                onClick = onEquip,
                modifier = Modifier.fillMaxWidth()
            )
            gear.owned -> Text(
                text = stringResource(R.string.rewards_owned),
                style = MaterialTheme.typography.labelSmall,
                color = SuccessTeal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            gear.affordable -> PrimaryButton(
                text = stringResource(R.string.cta_unlock),
                onClick = onUnlock,
                modifier = Modifier.fillMaxWidth()
            )
            else -> Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(
                        R.string.rewards_cost_format,
                        gear.balance,
                        gear.cost
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { gear.ratio },
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
    }
}
