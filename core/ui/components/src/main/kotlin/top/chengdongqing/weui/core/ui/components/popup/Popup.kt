package top.chengdongqing.weui.core.ui.components.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
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
            val draggableState = rememberDraggableState {
                offsetY.intValue = (offsetY.intValue + it.roundToInt()).coerceAtLeast(0)
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(x = 0, y = animatedOffsetY)
                    }
                    .fillMaxWidth()
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
                        DraggableLine(draggableState, offsetY, height, onClose)
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
private fun DraggableLine(
    draggableState: DraggableState,
    offsetY: MutableIntState,
    height: Int,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = -(12.dp))
            .draggable(
                draggableState,
                orientation = Orientation.Vertical,
                onDragStopped = {
                    if (offsetY.intValue > height / 2) {
                        onClose()
                    } else {
                        offsetY.intValue = 0
                    }
                }
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