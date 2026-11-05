package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.domain.model.Rarity

@Composable
fun StatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    live: Boolean = false
) {
    val semanticsModifier = if (live) {
        Modifier.semantics { liveRegion = LiveRegionMode.Polite }
    } else {
        Modifier
    }
    Column(
        modifier = modifier
            .background(IceSurface.copy(alpha = 0.92f), RoundedCornerShape(16.dp))
            .border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .then(semanticsModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(accent, CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = accent,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SlateInk,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun StatRow(
    entries: List<StatEntry>,
    modifier: Modifier = Modifier,
    live: Boolean = false
) {
    val visible = entries.filter { it.visible }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visible.forEach { entry ->
            StatCard(
                value = entry.value,
                label = entry.label,
                accent = entry.accent,
                live = live,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

data class StatEntry(
    val value: String,
    val label: String,
    val accent: Color,
    val visible: Boolean = true
)

@Composable
fun RarityChip(
    rarity: Rarity,
    label: String,
    modifier: Modifier = Modifier
) {
    val tint = SpeciesVisuals.rarityColor(rarity)
    Row(
        modifier = modifier
            .background(tint.copy(alpha = 0.16f), RoundedCornerShape(10.dp))
            .border(1.dp, tint.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(tint, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = tint,
            maxLines = 1
        )
    }
}
