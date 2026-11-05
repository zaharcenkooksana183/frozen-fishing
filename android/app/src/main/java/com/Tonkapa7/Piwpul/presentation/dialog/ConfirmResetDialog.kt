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
import com.Tonkapa7.Piwpul.core.theme.MissCoral
import com.Tonkapa7.Piwpul.core.theme.SlateInk

@Composable
fun ConfirmResetDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = IceSurface,
        title = {
            Text(
                text = stringResource(R.string.settings_reset),
                style = MaterialTheme.typography.titleMedium,
                color = DeepNavy
            )
        },
        text = {
            Text(
                text = stringResource(R.string.settings_reset_message),
                style = MaterialTheme.typography.bodyMedium,
                color = SlateInk
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.cta_reset),
                    style = MaterialTheme.typography.labelLarge,
                    color = MissCoral
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = SlateInk
                )
            }
        }
    )
}
