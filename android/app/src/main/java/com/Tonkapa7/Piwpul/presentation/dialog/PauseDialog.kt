package com.Tonkapa7.Piwpul.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.SlateInk

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(22.dp),
        containerColor = IceSurface,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.titleMedium,
                color = DeepNavy
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_message),
                style = MaterialTheme.typography.bodyMedium,
                color = SlateInk
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.cta_resume),
                    style = MaterialTheme.typography.labelLarge,
                    color = LakeBlueDeep
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onMenu) {
                Text(
                    text = stringResource(R.string.cta_menu),
                    style = MaterialTheme.typography.labelLarge,
                    color = SlateInk
                )
            }
        }
    )
}
