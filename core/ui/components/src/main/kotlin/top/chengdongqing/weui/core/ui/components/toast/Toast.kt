package top.chengdongqing.weui.core.ui.components.toast

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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.ui.components.loading.WeLoading
import top.chengdongqing.weui.core.ui.theme.BackgroundColorLight
import top.chengdongqing.weui.core.utils.rememberStatusBarHeight
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
 * @param mask 是否防止触摸穿透
 * @param onClose 关闭事件
 */
@Composable
fun WeToast(
    visible: Boolean,
    title: String,
    icon: ToastIcon = ToastIcon.NONE,
    duration: Duration = 1500.milliseconds,
    mask: Boolean = false,
    onClose: () -> Unit
) {
    val hasIcon = icon != ToastIcon.NONE
    var localVisible by remember {
        mutableStateOf(visible)
    }

    LaunchedEffect(visible, duration, title) {
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

    val positionProvider = remember {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                return windowSize.center - popupContentSize.center
            }
        }
    }
    val statusBarHeight = rememberStatusBarHeight()

    if (visible || localVisible) {
        Popup(popupPositionProvider = positionProvider) {
            Box(
                modifier = if (mask) {
                    Modifier
                        .fillMaxSize()
                        .offset(y = -statusBarHeight)
                } else {
                    Modifier.toastSize(hasIcon)
                },
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = visible && localVisible,
                    enter = fadeIn() + scaleIn(tween(100), initialScale = 0.8f),
                    exit = fadeOut() + scaleOut(tween(100), targetScale = 0.8f)
                ) {
                    Box(
                        modifier =
                        Modifier
                            .toastSize(hasIcon)
                            .clip(
                                if (icon != ToastIcon.NONE) {
                                    RoundedCornerShape(12.dp)
                                } else {
                                    RoundedCornerShape(8.dp)
                                }
                            )
                            .background(Color(0xFF4C4C4C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            when (icon) {
                                ToastIcon.LOADING -> {
                                    WeLoading(size = 43.dp, color = BackgroundColorLight)
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                ToastIcon.SUCCESS,
                                ToastIcon.FAIL ->
                                    Icon(
                                        if (icon == ToastIcon.SUCCESS) Icons.Outlined.Check else Icons.Filled.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(43.dp),
                                        tint = BackgroundColorLight
                                    )

                                else -> {}
                            }

                            val textMeasurer = rememberTextMeasurer()
                            val textLayoutResult = remember(title) {
                                textMeasurer.measure(title, TextStyle(fontSize = 17.sp))
                            }
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = if (hasIcon && textLayoutResult.size.width <= 354) 17.sp else 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.toastSize(hasIcon: Boolean): Modifier {
    return if (hasIcon) {
        this.size(136.dp)
    } else {
        this
            .width(152.dp)
            .heightIn(44.dp)
    }
}

@Stable
interface ToastState {
    /**
     * 是否显示
     */
    val visible: Boolean

    /**
     * 显示提示框
     */
    fun show(
        title: String,
        icon: ToastIcon = ToastIcon.NONE,
        duration: Duration = 1500.milliseconds,
        mask: Boolean = false
    )

    /**
     * 隐藏提示框
     */
    fun hide()
}

@Composable
fun rememberToastState(): ToastState {
    val state = remember { ToastStateImpl() }

    state.props?.let { props ->
        WeToast(
            visible = state.visible,
            title = props.title,
            icon = props.icon,
            duration = props.duration,
            mask = props.mask
        ) {
            state.hide()
        }
    }

    return state
}

private class ToastStateImpl : ToastState {
    override var visible by mutableStateOf(false)
    var props by mutableStateOf<ToastProps?>(null)
        private set

    override fun show(title: String, icon: ToastIcon, duration: Duration, mask: Boolean) {
        props = ToastProps(title, icon, duration, mask)
        visible = true
    }

    override fun hide() {
        visible = false
    }
}

private data class ToastProps(
    val title: String,
    val icon: ToastIcon,
    val duration: Duration,
    val mask: Boolean
)