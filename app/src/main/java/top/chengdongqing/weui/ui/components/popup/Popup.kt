package top.chengdongqing.weui.ui.components.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import top.chengdongqing.weui.extensions.clickableWithoutRipple
import top.chengdongqing.weui.ui.theme.BackgroundColor

/**
 * 从底部弹出的半屏弹窗
 *
 * @param visible 是否显示
 * @param title 标题
 * @param enterTransition 弹出时的过渡动画
 * @param exitTransition 收起时的过渡动画
 * @param padding 内边距
 * @param swipeable 是否可滑动关闭
 * @param onClose 关闭事件
 * @param content 内容
 */
@Composable
fun WePopup(
    visible: Boolean,
    title: String? = null,
    enterTransition: EnterTransition = slideInVertically(
        animationSpec = remember {
            tween(300)
        },
        initialOffsetY = { it }
    ),
    exitTransition: ExitTransition = slideOutVertically(
        animationSpec = remember {
            tween(300)
        },
        targetOffsetY = { it }
    ),
    padding: PaddingValues = PaddingValues(12.dp),
    swipeable: Boolean = false,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    var localVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(350)
        }
        localVisible = visible
    }

    if (visible || localVisible) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickableWithoutRipple {
                        onClose()
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = visible && localVisible,
                    enter = enterTransition,
                    exit = exitTransition
                ) {
                    var height by remember {
                        mutableIntStateOf(0)
                    }
                    var offsetY by remember {
                        mutableFloatStateOf(0f)
                    }
                    val density = LocalDensity.current
                    val offsetDp = density.run {
                        offsetY.toDp()
                    }

                    Box(
                        modifier = Modifier
                            .offset(
                                y = animateDpAsState(
                                    targetValue = offsetDp,
                                    label = "PopupDragAnimation"
                                ).value
                            )
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .background(Color.White)
                            .clickableWithoutRipple { }
                            .padding(padding)
                            .onSizeChanged {
                                height = it.height
                            }
                    ) {
                        Column {
                            if (swipeable) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = -(12.dp))
                                        .draggable(
                                            state = rememberDraggableState { dragAmount ->
                                                val value = offsetY + dragAmount
                                                if (value >= 0) {
                                                    offsetY = value
                                                }
                                            },
                                            orientation = Orientation.Vertical,
                                            onDragStopped = {
                                                if (offsetY < height / 2) {
                                                    offsetY = 0f
                                                } else {
                                                    onClose()
                                                }
                                            })
                                        .padding(top = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp, 4.dp)
                                            .background(BackgroundColor, RoundedCornerShape(2.dp))
                                    )
                                }
                            }

                            title?.let {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = it,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            content()
                        }
                    }
                }
            }
        }
    }
}
