package com.Tonkapa7.Piwpul.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Tonkapa7.Piwpul.core.theme.IceSurface
import com.Tonkapa7.Piwpul.core.theme.LakeBlue
import com.Tonkapa7.Piwpul.core.theme.Stroke20

@Composable
fun FrostCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    containerColor: Color = IceSurface.copy(alpha = 0.92f),
    borderColor: Color = Stroke20,
    elevation: Dp = 3.dp,
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
        label = "cardScale"
    )
    val shaped = modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = LakeBlue,
            spotColor = LakeBlue
        )
    if (onClick == null) {
        Surface(
            modifier = shaped,
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    } else {
        Surface(
            onClick = onClick,
            modifier = shaped,
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.dp, borderColor),
            interactionSource = interactionSource
        ) {
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    }
}
