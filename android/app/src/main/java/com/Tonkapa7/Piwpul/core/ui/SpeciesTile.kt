package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.MutedInk
import com.Tonkapa7.Piwpul.domain.model.Rarity

@Composable
fun SpeciesTile(
    speciesId: Int,
    rarity: Rarity,
    discovered: Boolean,
    caption: String,
    modifier: Modifier = Modifier,
    spriteSize: Dp = 88.dp,
    onClick: (() -> Unit)? = null
) {
    val name = stringResource(SpeciesVisuals.name(speciesId))
    FrostCard(
        modifier = modifier,
        onClick = if (discovered) onClick else null,
        contentPadding = 12.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(spriteSize),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(SpeciesVisuals.sprite(speciesId)),
                contentDescription = if (discovered) name else null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(spriteSize)
                    .alpha(if (discovered) 1f else 0.18f)
            )
            if (!discovered) {
                Text(
                    text = stringResource(R.string.collection_undiscovered),
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedInk,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (discovered) name else stringResource(R.string.collection_undiscovered),
            style = MaterialTheme.typography.titleMedium,
            color = if (discovered) DeepNavy else MutedInk,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (discovered) {
            RarityChip(
                rarity = rarity,
                label = stringResource(SpeciesVisuals.rarityLabel(rarity))
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
        Text(
            text = caption,
            style = MaterialTheme.typography.labelSmall,
            color = MutedInk
        )
    }
}

@Composable
fun SpeciesStrip(
    speciesIds: List<Int>,
    modifier: Modifier = Modifier,
    spriteSize: Dp = 26.dp
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        speciesIds.forEach { id ->
            Image(
                painter = painterResource(SpeciesVisuals.sprite(id)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(spriteSize)
            )
        }
    }
}
