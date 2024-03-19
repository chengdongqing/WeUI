package top.chengdongqing.weui.core.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

fun Modifier.clickableWithoutRipple(enabled: Boolean = true, onClick: () -> Unit) = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled
    ) {
        onClick()
    }
}

fun Boolean.format(trueLabel: String = "是", falseLabel: String = "否") =
    if (this) trueLabel else falseLabel

fun Boolean?.isTrue(): Boolean = this == true

fun Offset.toIntOffset() = IntOffset(this.x.roundToInt(), this.y.roundToInt())
fun Size.toIntSize() = IntSize(this.width.roundToInt(), this.height.roundToInt())