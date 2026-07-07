package top.chengdongqing.weui.util

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.onTap(
    enabled: Boolean = true,
    onClick: () -> Unit
) = clickable(
    interactionSource = null,
    indication = null,
    enabled = enabled,
    onClick = onClick
)