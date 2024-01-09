package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import top.chengdongqing.weui.ui.components.basic.WeLoading
import top.chengdongqing.weui.ui.theme.BackgroundColor
import top.chengdongqing.weui.utils.screenCenterPositionProvider
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

enum class ToastIcon {
    SUCCESS,
    FAIL,
    LOADING,
    NONE
}

/**
 * 弹出式提示框
 *
 * @param visible 是否显示
 * @param title 标题
 * @param icon 图标
 * @param mask 是否显示透明蒙层，防止触摸穿透
 * @param duration 显示的时长
 * @param onClose 关闭事件
 */
@Composable
fun WeToast(
    visible: Boolean,
    title: String,
    icon: ToastIcon = ToastIcon.NONE,
    mask: Boolean = false,
    duration: Duration = 1500.milliseconds,
    onClose: () -> Unit
) {
    val hasIcon = icon != ToastIcon.NONE
    val modifier = if (hasIcon) {
        Modifier.size(136.dp)
    } else {
        Modifier
            .width(152.dp)
            .heightIn(44.dp)
    }

    LaunchedEffect(visible) {
        if (visible) {
            if (duration != Duration.INFINITE) {
                delay(duration)
                onClose()
            }
        }
    }

    if (visible) {
        Popup(
            popupPositionProvider = screenCenterPositionProvider
        ) {
            Box(
                modifier = if (mask) Modifier.fillMaxSize() else Modifier,
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF4C4C4C)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (icon) {
                            ToastIcon.LOADING -> {
                                WeLoading(size = 43.dp, color = BackgroundColor)
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            ToastIcon.SUCCESS,
                            ToastIcon.FAIL ->
                                Icon(
                                    if (icon == ToastIcon.SUCCESS) Icons.Outlined.Check else Icons.Filled.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(43.dp),
                                    tint = BackgroundColor
                                )

                            else -> {}
                        }

                        val lineCount = remember {
                            mutableIntStateOf(1)
                        }
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = if (hasIcon && lineCount.value == 1) 17.sp else 14.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            textAlign = TextAlign.Center,
                            onTextLayout = {
                                lineCount.value = it.lineCount
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeToastHolder(
    title: String,
    icon: ToastIcon = ToastIcon.NONE,
    mask: Boolean = false,
    duration: Duration = 1500.milliseconds,
    holder: @Composable (visible: MutableState<Boolean>) -> Unit
) {
    val visible = remember {
        mutableStateOf(false)
    }

    holder(visible)

    WeToast(
        visible.value,
        title,
        icon,
        mask,
        duration
    ) {
        visible.value = false
    }
}