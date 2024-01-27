package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * @param duration 显示的时长
 * @param onClose 关闭事件
 */
@Composable
fun WeToast(
    visible: Boolean,
    title: String,
    icon: ToastIcon = ToastIcon.NONE,
    duration: Duration = 1500.milliseconds,
    onClose: () -> Unit
) {
    val hasIcon = icon != ToastIcon.NONE
    var localVisible by remember {
        mutableStateOf(visible)
    }

    LaunchedEffect(visible, duration) {
        if (visible && duration != Duration.INFINITE) {
            delay(duration)
            onClose()
        }
    }
    LaunchedEffect(visible) {
        if (!visible) {
            delay(150)
        }
        localVisible = visible
    }

    if (visible || localVisible) {
        Popup {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = visible && localVisible,
                    enter = fadeIn() + scaleIn(tween(100), initialScale = 0.8f),
                    exit = fadeOut() + scaleOut(tween(100), targetScale = 0.8f)
                ) {
                    Box(
                        modifier = if (hasIcon) {
                            Modifier.size(136.dp)
                        } else {
                            Modifier
                                .width(152.dp)
                                .heightIn(44.dp)
                        }
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
                                fontSize = if (hasIcon && lineCount.intValue == 1) 17.sp else 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                textAlign = TextAlign.Center,
                                onTextLayout = {
                                    lineCount.intValue = it.lineCount
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberWeToast(): WeToastState {
    val visible = remember {
        mutableStateOf(false)
    }
    var localState by remember {
        mutableStateOf(Toast(""))
    }

    WeToast(
        visible = visible.value,
        title = localState.title,
        icon = localState.icon,
        duration = localState.duration
    ) {
        visible.value = false
    }

    return WeToastState(visible) {
        localState = it
    }
}

class WeToastState(
    private val visible: MutableState<Boolean>,
    private val setLocalState: (Toast) -> Unit
) {
    fun visible(): Boolean {
        return visible.value
    }

    fun open(
        title: String,
        icon: ToastIcon = ToastIcon.NONE,
        duration: Duration = 1500.milliseconds
    ) {
        setLocalState(Toast(title, icon, duration))
        visible.value = true
    }

    fun close() {
        visible.value = false
    }
}

data class Toast(
    val title: String,
    val icon: ToastIcon = ToastIcon.NONE,
    val duration: Duration = 1500.milliseconds
)