package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.SlateInk

@Composable
fun ScreenHeader(
    title: String,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth().height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onHome, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_home),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = DeepNavy,
                maxLines = 1
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk,
                    maxLines = 1
                )
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

@Composable
fun PauseButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier.size(48.dp)) {
        Image(
            painter = painterResource(R.drawable.icon_pause),
            contentDescription = stringResource(R.string.cd_pause),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(32.dp)
        )
    }
}
