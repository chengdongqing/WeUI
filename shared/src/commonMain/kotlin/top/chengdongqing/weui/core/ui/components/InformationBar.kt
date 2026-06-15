package top.chengdongqing.weui.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.core.ui.theme.GreenPrimary
import top.chengdongqing.weui.core.ui.theme.PurpleLink
import top.chengdongqing.weui.core.ui.theme.RedDanger
import top.chengdongqing.weui.util.weClickable
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_check
import weui_kmp.shared.generated.resources.ic_error_circle_filled
import kotlin.time.Duration.Companion.milliseconds

enum class InformationBarType {
    WarnStrong,
    Info,
    TipsStrong,
    TipsWeak,
    Success
}

@Composable
fun WeInformationBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    message: String,
    type: InformationBarType = InformationBarType.Success,
    shape: Shape = RoundedCornerShape(8.dp),
    linkText: String? = null,
    autoClose: Boolean = false,
    onLink: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val colors = colorSchemeOf(type)

    val icon = if (type == InformationBarType.Success) {
        Res.drawable.ic_check
    } else {
        Res.drawable.ic_error_circle_filled
    }

    // 自动关闭
    LaunchedEffect(visible, autoClose, message) {
        if (visible && autoClose) {
            delay(5000.milliseconds)
            onDismiss?.invoke()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(shape)
                .background(colors.backgroundColor)
                .padding(16.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colors.iconColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = colors.textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            linkText?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = colors.linkColor,
                    modifier = Modifier.weClickable {
                        onLink?.invoke()
                    }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            onDismiss?.let {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = null,
                    tint = colors.closeIconColor,
                    modifier = Modifier.weClickable {
                        it()
                    }
                )
            }
        }
    }
}

private data class InformationBarColors(
    val backgroundColor: Color,
    val iconColor: Color = Color.White,
    val textColor: Color = Color.White,
    val linkColor: Color = Color.White,
    val closeIconColor: Color = Color.White
)

@Composable
private fun colorSchemeOf(type: InformationBarType): InformationBarColors {
    val isDark = isSystemInDarkTheme()

    return when (type) {
        InformationBarType.WarnStrong -> InformationBarColors(
            backgroundColor = Color(0xFFFA5151)
        )

        InformationBarType.Info -> InformationBarColors(
            backgroundColor = Color(0f, 0f, 0f, 0.3f)
        )

        InformationBarType.TipsStrong -> InformationBarColors(
            backgroundColor = Color(0xFFFA9D3B)
        )

        InformationBarType.TipsWeak -> InformationBarColors(
            backgroundColor = if (isDark) {
                Color(0.522f, 0.212f, 0.212f, 1.0f)
            } else {
                Color(1f, 0.945f, 0.957f)
            },
            iconColor = RedDanger,
            textColor = if (isDark) {
                Color(0.922f, 0.627f, 0.651f, 1.0f)
            } else {
                Color(0f, 0f, 0f, 0.55f)
            },
            linkColor = PurpleLink,
            closeIconColor = Color(0f, 0f, 0f, 0.55f)
        )

        InformationBarType.Success -> InformationBarColors(
            backgroundColor = GreenPrimary
        )
    }
}