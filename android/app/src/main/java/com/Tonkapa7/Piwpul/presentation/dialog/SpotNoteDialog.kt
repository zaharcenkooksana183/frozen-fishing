package com.Tonkapa7.Piwpul.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.SlateInk

@Composable
fun SpotNoteDialog(
    initialNote: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by rememberSaveable(initialNote) { mutableStateOf(initialNote) }
    val chips = listOf(
        stringResource(R.string.note_chip_1),
        stringResource(R.string.note_chip_2),
        stringResource(R.string.note_chip_3),
        stringResource(R.string.note_chip_4)
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = IceSurface,
        title = {
            Text(
                text = stringResource(R.string.note_dialog_title),
                style = MaterialTheme.typography.titleMedium,
                color = DeepNavy
            )
        },
        text = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    chips.take(2).forEach { chip ->
                        AssistChip(
                            onClick = { text = chip },
                            shape = RoundedCornerShape(12.dp),
                            label = {
                                Text(
                                    text = chip,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LakeBlueDeep
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    chips.drop(2).forEach { chip ->
                        AssistChip(
                            onClick = { text = chip },
                            shape = RoundedCornerShape(12.dp),
                            label = {
                                Text(
                                    text = chip,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LakeBlueDeep
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it.take(80) },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.note_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateInk
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(text) }) {
                Text(
                    text = stringResource(R.string.cta_save),
                    style = MaterialTheme.typography.labelLarge,
                    color = LakeBlue
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
