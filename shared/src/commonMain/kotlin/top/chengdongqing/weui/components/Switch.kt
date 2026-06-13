package top.chengdongqing.weui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.utils.weClickable

/**
 * 开关
 *
 * @param checked 是否开启
 * @param disabled 是否禁用
 * @param onChange 状态改变事件
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun WeSwitch(
    checked: Boolean = false,
    disabled: Boolean = false,
    onChange: ((checked: Boolean) -> Unit)? = null
) {
    val offsetX by animateDpAsState(
        targetValue = if (checked) 20.dp else 2.dp,
        animationSpec = tween(durationMillis = 100),
        label = "SwitchAnimation"
    )
    val haptic = LocalHapticFeedback.current

    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier
            .styleable(style = Style {
                size(44.dp, 26.dp)
                alpha(if (disabled) 0.7f else 1f)
                shape(RoundedCornerShape(16.dp))
                background(if (checked) primaryColor else outlineColor)
            })
            .weClickable(!disabled) {
                val newValue = !checked
                onChange?.invoke(newValue)

                // 触发震动反馈
                haptic.performHapticFeedback(
                    if (newValue)
                        HapticFeedbackType.ToggleOn
                    else
                        HapticFeedbackType.ToggleOff
                )
            }
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(x = offsetX.roundToPx(), y = 2.dp.roundToPx())
                }
                .styleable(style = Style {
                    size(22.dp)
                    background(Color.White)
                    shape(RoundedCornerShape(50))
                })
        )
    }
}