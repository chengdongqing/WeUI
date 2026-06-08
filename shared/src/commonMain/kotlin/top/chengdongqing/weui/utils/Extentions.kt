package top.chengdongqing.weui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

fun Modifier.weClickable(enabled: Boolean = true, onClick: () -> Unit) = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled
    ) {
        onClick()
    }
}

fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())