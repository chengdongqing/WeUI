package top.chengdongqing.weui.core.ui.components.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.data.model.DragAnchor
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import kotlin.math.roundToInt

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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WePopup(
    visible: Boolean,
    title: String? = null,
    enterTransition: EnterTransition = slideInVertically(
        animationSpec = tween(150),
        initialOffsetY = { it }
    ),
    exitTransition: ExitTransition = slideOutVertically(
        animationSpec = tween(150),
        targetOffsetY = { it }
    ),
    padding: PaddingValues = PaddingValues(12.dp),
    swipeable: Boolean = false,
    onClose: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var localVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(200)
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
                    var height by remember { mutableIntStateOf(0) }
                    val swipeState = rememberSwipeState(height, onClose)

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = swipeState
                                        .requireOffset()
                                        .roundToInt()
                                )
                            }
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .background(MaterialTheme.colorScheme.onBackground)
                            .clickableWithoutRipple { }
                            .padding(padding)
                            .onSizeChanged { height = it.height }
                    ) {
                        Column {
                            if (swipeable) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = -(12.dp))
                                        .anchoredDraggable(
                                            state = swipeState,
                                            orientation = Orientation.Vertical
                                        )
                                        .padding(top = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp, 4.dp)
                                            .background(
                                                MaterialTheme.colorScheme.outline,
                                                RoundedCornerShape(2.dp)
                                            )
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
                                        color = MaterialTheme.colorScheme.onPrimary,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberSwipeState(
    height: Int,
    onClose: () -> Unit
): AnchoredDraggableState<DragAnchor> {
    val density = LocalDensity.current
    val state = remember(height) {
        AnchoredDraggableState(
            initialValue = DragAnchor.Center,
            anchors = DraggableAnchors {
                DragAnchor.Center at 0f
                DragAnchor.End at height.toFloat()
            },
            positionalThreshold = { totalDistance -> totalDistance * 0.5f },
            velocityThreshold = { density.run { 100.dp.toPx() } },
            animationSpec = tween()
        )
    }

    LaunchedEffect(state) {
        snapshotFlow { state.targetValue }.collect {
            if (it == DragAnchor.End) {
                onClose()
            }
        }
    }

    return state
}