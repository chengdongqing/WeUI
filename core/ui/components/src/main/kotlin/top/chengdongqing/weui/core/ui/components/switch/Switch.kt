package top.chengdongqing.weui.core.ui.components.switch

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.utils.clickableWithoutRipple

/**
 * 开关
 *
 * @param checked 是否开启
 * @param disabled 是否禁用
 * @param onChange 状态改变事件
 */
@Composable
fun WeSwitch(
    checked: Boolean = false,
    disabled: Boolean = false,
    onChange: ((checked: Boolean) -> Unit)? = null
) {
    val offsetX by animateDpAsState(
        targetValue = if (checked) 26.dp else 2.dp,
        animationSpec = tween(durationMillis = 100),
        label = "SwitchAnimation"
    )
    val haptic = LocalHapticFeedback.current

    Box(
        Modifier
            .size(50.dp, 26.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (checked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
            .alpha(if (disabled) 0.7f else 1f)
            .clickableWithoutRipple(!disabled) {
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
            Modifier
                .offset(offsetX, 2.dp)
                .size(22.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
        )
    }
}