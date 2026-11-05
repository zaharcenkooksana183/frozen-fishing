package com.Tonkapa7.Piwpul.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tonkapa7.Piwpul.R
import com.Tonkapa7.Piwpul.core.di.AppViewModelFactory
import com.Tonkapa7.Piwpul.core.theme.DeepNavy
import com.Tonkapa7.Piwpul.core.theme.IceSecondary
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.LakeBlueDeep
import com.Tonkapa7.Piwpul.core.theme.MissCoral
import com.Tonkapa7.Piwpul.core.theme.SlateInk
import com.Tonkapa7.Piwpul.core.theme.SnowWhite
import com.Tonkapa7.Piwpul.core.ui.FrostCard
import com.Tonkapa7.Piwpul.core.ui.ScreenBackground
import com.Tonkapa7.Piwpul.core.ui.ScreenHeader
import com.Tonkapa7.Piwpul.core.ui.ScreenOverlay
import com.Tonkapa7.Piwpul.core.ui.SectionHeader
import com.Tonkapa7.Piwpul.presentation.dialog.ConfirmResetDialog
import com.Tonkapa7.Piwpul.core.ui.Icons

@Composable
fun SettingsScreen(onHome: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val resetOpen by viewModel.resetDialogOpen.collectAsStateWithLifecycle()

    ScreenBackground(overlay = ScreenOverlay.hub) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.settings_title),
                subtitle = stringResource(R.string.settings_about_line),
                onHome = onHome
            )
            Spacer(modifier = Modifier.height(10.dp))
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                SwitchRow(
                    label = stringResource(R.string.settings_sound),
                    checked = settings.sound,
                    onChange = viewModel::setSound
                )
                SwitchRow(
                    label = stringResource(R.string.settings_haptics),
                    checked = settings.haptics,
                    onChange = viewModel::setHaptics
                )
                SwitchRow(
                    label = stringResource(R.string.settings_snowfall),
                    checked = settings.snowfall,
                    onChange = viewModel::setSnowfall
                )
                SwitchRow(
                    label = stringResource(R.string.settings_large_text),
                    checked = settings.largeText,
                    onChange = viewModel::setLargeText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = stringResource(R.string.settings_units))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UnitOption(
                    label = stringResource(R.string.settings_units_cm),
                    selected = settings.metric,
                    onClick = { viewModel.setMetric(true) },
                    modifier = Modifier.weight(1f)
                )
                UnitOption(
                    label = stringResource(R.string.settings_units_in),
                    selected = !settings.metric,
                    onClick = { viewModel.setMetric(false) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.settings_about),
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepNavy
                )
                Text(
                    text = stringResource(
                        R.string.settings_version_format,
                        stringResource(R.string.settings_version)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.settings_about_line),
                    style = MaterialTheme.typography.labelSmall,
                    color = LakeBlueDeep
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = viewModel::askReset,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, MissCoral.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = IceSurface.copy(alpha = 0.8f),
                    contentColor = MissCoral
                ),
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.settings_reset),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (resetOpen) {
        ConfirmResetDialog(
            onDismiss = viewModel::dismissReset,
            onConfirm = viewModel::confirmReset
        )
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    val stateLabel = if (checked) {
        stringResource(R.string.state_on)
    } else {
        stringResource(R.string.state_off)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .semantics { stateDescription = stateLabel },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = DeepNavy,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SnowWhite,
                checkedTrackColor = LakeBlue,
                uncheckedThumbColor = SnowWhite,
                uncheckedTrackColor = IceSecondary
            )
        )
    }
}

@Composable
private fun UnitOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) LakeBlue else IceSecondary
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) {
                LakeBlue.copy(alpha = 0.14f)
            } else {
                IceSurface.copy(alpha = 0.8f)
            },
            contentColor = if (selected) LakeBlueDeep else SlateInk
        ),
        modifier = modifier.heightIn(min = 48.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
    }
}
