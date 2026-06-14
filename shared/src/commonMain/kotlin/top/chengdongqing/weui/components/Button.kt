package top.chengdongqing.weui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.components.loading.WeLoading
import top.chengdongqing.weui.theme.GreenPrimary
import top.chengdongqing.weui.theme.RedDanger
import top.chengdongqing.weui.theme.TextPrimaryDark
import top.chengdongqing.weui.theme.TextPrimaryLight

enum class ButtonType {
    Primary,
    Danger,
    Plain
}

enum class ButtonSize(
    val padding: PaddingValues,
    val fontSize: TextUnit,
    val borderRadius: Dp = 8.dp
) {
    Large(PaddingValues(vertical = 12.dp, horizontal = 24.dp), 17.sp),
    Medium(PaddingValues(vertical = 10.dp, horizontal = 24.dp), 14.sp),
    Small(PaddingValues(vertical = 6.dp, horizontal = 12.dp), 14.sp, 6.dp)
}

/**
 * 按钮
 *
 * @param text 按钮文字
 * @param type 类型
 * @param size 大小
 * @param width 宽度
 * @param prefix 前缀
 * @param enabled 是否启用
 * @param loading 是否加载中
 * @param onClick 点击事件
 */
@Composable
fun WeButton(
    text: String,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.Primary,
    size: ButtonSize = ButtonSize.Large,
    width: Dp = 184.dp,
    prefix: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val colors = colorSchemeOf(type, enabled)
    val finalEnabled = enabled && !loading

    Box(
        Modifier
            .width(if (size != ButtonSize.Small) width else Dp.Unspecified)
            .clip(RoundedCornerShape(size.borderRadius))
            .clickable(enabled = finalEnabled) {
                onClick?.invoke()
            }
            .background(colors.containerColor)
            .padding(size.padding)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (loading) {
                WeLoading(color = colors.contentColor)
                Spacer(Modifier.width(8.dp))
            }
            prefix?.let {
                it()
                Spacer(Modifier.width(8.dp))
            }

            Text(
                text,
                color = colors.contentColor,
                fontSize = size.fontSize
            )
        }
    }
}

private data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color
)

@Composable
private fun colorSchemeOf(type: ButtonType, enabled: Boolean): ButtonColors {
    val isDarkTheme = isSystemInDarkTheme()

    return when (type) {
        ButtonType.Primary -> ButtonColors(
            if (enabled) GreenPrimary else {
                if (isDarkTheme) {
                    Color(0xFF373737)
                } else {
                    Color(0xFFDEDEDE)
                }
            },
            if (enabled) Color.White else {
                if (isDarkTheme) {
                    Color(0xFFBBBBBB).copy(alpha = 0.4f)
                } else {
                    Color(0xFFBBBBBB)
                }
            }
        )

        ButtonType.Danger -> if (isDarkTheme) {
            ButtonColors(RedDanger, TextPrimaryDark)
        } else {
            ButtonColors(Color.Black.copy(0.05f), RedDanger)
        }

        ButtonType.Plain -> if (isDarkTheme) {
            ButtonColors(Color.White.copy(0.1f), TextPrimaryDark)
        } else {
            ButtonColors(Color.Black.copy(0.05f), TextPrimaryLight)
        }
    }
}