package top.chengdongqing.weui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

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