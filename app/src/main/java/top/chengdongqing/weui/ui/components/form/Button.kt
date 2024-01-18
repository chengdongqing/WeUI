package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.basic.WeLoading
import top.chengdongqing.weui.ui.theme.DangerColor
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.PrimaryColor

enum class ButtonType(val bgColor: Color, val color: Color) {
    PRIMARY(PrimaryColor, Color.White),
    DANGER(Color(0f, 0f, 0f, 0.05f), DangerColor),
    PLAIN(Color(0f, 0f, 0f, 0.05f), FontColor)
}

enum class ButtonSize(
    val padding: PaddingValues,
    val fontSize: TextUnit,
    val borderRadius: Dp = 8.dp
) {
    LARGE(PaddingValues(vertical = 12.dp, horizontal = 24.dp), 17.sp),
    MEDIUM(PaddingValues(vertical = 10.dp, horizontal = 24.dp), 14.sp),
    SMALL(PaddingValues(vertical = 6.dp, horizontal = 12.dp), 14.sp, 6.dp)
}

/**
 * 按钮
 *
 * @param text 按钮文字
 * @param type 类型
 * @param size 大小
 * @param width 宽度
 * @param disabled 是否禁用
 * @param loading 是否加载中
 * @param onClick 点击事件
 */
@Composable
fun WeButton(
    text: String,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    size: ButtonSize = ButtonSize.LARGE,
    width: Dp = 184.dp,
    disabled: Boolean = false,
    loading: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val localDisabled = disabled || loading

    Box(
        Modifier
            .width(if (size != ButtonSize.SMALL) width else Dp.Unspecified)
            .clip(RoundedCornerShape(size.borderRadius))
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = if (!localDisabled) rememberRipple() else null
            ) {
                if (!localDisabled) {
                    onClick?.invoke()
                }
            }
            .background(if (!disabled) type.bgColor else Color(0xFFF7F7F7))
            .padding(size.padding)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (loading) {
                WeLoading(color = type.color)
                Spacer(Modifier.width(8.dp))
            }

            Text(
                text,
                color = if (!disabled) type.color else Color(0f, 0f, 0f, 0.15f),
                fontSize = size.fontSize
            )
        }
    }
}