package top.chengdongqing.weui.core.ui.components.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import kotlin.math.roundToInt

/**
 * 从底部弹出的弹窗
 *
 * @param visible 是否显示
 * @param title 标题
 * @param enterTransition 弹出时的过渡动画
 * @param exitTransition 收起时的过渡动画
 * @param padding 内边距
 * @param draggable 是否可拖动关闭
 * @param onClose 关闭事件
 * @param content 内容
 */
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
    draggable: Boolean = true,
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
        PopupContainer(
            visible = visible && localVisible,
            enterTransition,
            exitTransition,
            onClose
        ) {
            var height by remember { mutableIntStateOf(0) }
            val offsetY = remember { mutableIntStateOf(0) }

            val animatedOffsetY by animateIntAsState(
                targetValue = offsetY.intValue,
                label = "PopupDraggingAnimation"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(x = 0, y = animatedOffsetY)
                    }
                    .draggable(
                        state = rememberDraggableState {
                            handleDrag(offsetY, it)
                        },
                        enabled = draggable,
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            handleDragStopped(offsetY, height, onClose)
                        }
                    )
                    .then(
                        if (draggable) {
                            Modifier.nestedScroll(
                                remember(height) {
                                    PopupNestedScrollConnection(
                                        offsetY,
                                        height,
                                        onClose
                                    )
                                }
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickableWithoutRipple { }
                    .padding(padding)
                    .onSizeChanged {
                        height = it.height
                    }
            ) {
                Column {
                    if (draggable) {
                        DraggableLine()
                    }

                    title?.let {
                        PopupTitle(title = it)
                    }
                    content()
                }
            }
        }
    }
}

@Composable
private fun PopupContainer(
    visible: Boolean,
    enterTransition: EnterTransition,
    exitTransition: ExitTransition,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
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
                visible = visible,
                enter = enterTransition,
                exit = exitTransition
            ) {
                content()
            }
        }
    }
}

@Composable
private fun DraggableLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = -(12.dp))
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

@Composable
private fun PopupTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private class PopupNestedScrollConnection(
    private val offsetY: MutableIntState,
    private val height: Int,
    private val onClose: () -> Unit
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (source == NestedScrollSource.Drag && offsetY.intValue > 0) {
            handleDrag(offsetY, available.y)
            return available
        } else {
            return Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (source == NestedScrollSource.Drag) {
            handleDrag(offsetY, available.y)
            return available
        } else {
            return Offset.Zero
        }
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
        handleDragStopped(offsetY, height, onClose)
        return available
    }
}

private fun handleDrag(offsetY: MutableIntState, delta: Float) {
    offsetY.intValue = (offsetY.intValue + delta.roundToInt())
        .coerceAtLeast(0)
}

private fun handleDragStopped(offsetY: MutableIntState, height: Int, onClose: () -> Unit) {
    if (offsetY.intValue > height / 2) {
        onClose()
    } else {
        offsetY.intValue = 0
    }
}