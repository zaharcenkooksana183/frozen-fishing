package com.Tonkapa7.Piwpul.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.ui.RarityChip
import com.Tonkapa7.Piwpul.core.ui.SpeciesVisuals
import com.Tonkapa7.Piwpul.domain.model.Rarity

@Composable
fun SpeciesDetailDialog(
    speciesId: Int,
    rarity: Rarity,
    points: Int,
    count: Int,
    bestLengthCm: Int,
    firstCaughtLabel: String,
    metric: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = IceSurface,
        title = {
            Text(
                text = stringResource(SpeciesVisuals.name(speciesId)),
                style = MaterialTheme.typography.titleMedium,
                color = DeepNavy
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(SpeciesVisuals.sprite(speciesId)),
                        contentDescription = stringResource(SpeciesVisuals.name(speciesId)),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(130.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                RarityChip(
                    rarity = rarity,
                    label = stringResource(SpeciesVisuals.rarityLabel(rarity))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.collection_caught_format, count),
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk
                )
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow(
                    label = stringResource(R.string.collection_points_value),
                    value = stringResource(R.string.menu_points_format, points)
                )
                Spacer(modifier = Modifier.height(6.dp))
                DetailRow(
                    label = stringResource(R.string.collection_best_length),
                    value = if (metric) {
                        stringResource(R.string.game_length_cm_format, bestLengthCm)
                    } else {
                        stringResource(
                            R.string.game_length_in_format,
                            (bestLengthCm / 2.54f).toInt()
                        )
                    }
                )
                Spacer(modifier = Modifier.height(6.dp))
                DetailRow(
                    label = stringResource(R.string.collection_first_caught),
                    value = firstCaughtLabel
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_close),
                    style = MaterialTheme.typography.labelLarge,
                    color = LakeBlueDeep
                )
            }
        }
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SlateInk
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = DeepNavy
        )
    }
}
