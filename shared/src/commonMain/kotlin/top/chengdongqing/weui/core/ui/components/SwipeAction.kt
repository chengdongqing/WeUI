package top.chengdongqing.weui.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.theme.RedDanger
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.core.ui.theme.YellowWarning
import kotlin.math.roundToInt

enum class DragAnchor {
    Start,
    Center,
    End
}

enum class SwipeActionStyle {
    Label,
    Icon
}

enum class SwipeActionType(val color: Color) {
    Plain(Color(0XFFC7C6CD)),
    Warning(YellowWarning),
    Danger(RedDanger)
}

data class SwipeActionItem(
    val type: SwipeActionType? = null,
    val label: String,
    val icon: ImageVector? = null
)

@Composable
fun WeSwipeAction(
    startOptions: List<SwipeActionItem>? = null,
    endOptions: List<SwipeActionItem>? = null,
    style: SwipeActionStyle = SwipeActionStyle.Label,
    swipeActionState: SwipeActionState = rememberSwipeActionState(
        actionItemWidth = if (style == SwipeActionStyle.Label) 80.dp else 66.dp,
        startActionCount = startOptions?.size ?: 0,
        endActionCount = endOptions?.size ?: 0
    ),
    onStartTap: ((index: Int) -> Unit)? = null,
    onEndTap: ((index: Int) -> Unit)? = null,
    height: Dp = 60.dp,
    content: @Composable (BoxScope.() -> Unit)
) {
    val state = swipeActionState.draggableState
    val isLabelStyle = style == SwipeActionStyle.Label
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clipToBounds()
    ) {
        // 内容
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0
                    )
                }
                .background(
                    color = WeTheme.colorScheme.surface,
                    if (!isLabelStyle) RoundedCornerShape(8.dp) else RectangleShape
                )
                .padding(horizontal = 16.dp)
                .anchoredDraggable(
                    state,
                    reverseDirection = true,
                    orientation = Orientation.Horizontal
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            content()
        }
        // 左侧按钮组
        startOptions?.let {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                startOptions.forEachIndexed { index, item ->
                    val fraction = (1f / startOptions.size) * (index + 1)
                    ActionItem(
                        width = swipeActionState.actionItemWidth,
                        offset = IntOffset(
                            x = (((-state.requireOffset() * fraction) - swipeActionState.actionItemWidthPx)).roundToInt(),
                            y = 0
                        ),
                        isLabelStyle,
                        item
                    ) {
                        onStartTap?.invoke(index)
                        coroutineScope.launch {
                            state.snapTo(DragAnchor.Center)
                        }
                    }
                }
            }
        }
        // 右侧按钮组
        endOptions?.let {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                endOptions.forEachIndexed { index, item ->
                    val fraction = 1f - (1f / endOptions.size) * index
                    ActionItem(
                        width = swipeActionState.actionItemWidth,
                        offset = IntOffset(
                            x = ((-state.requireOffset() * fraction) + swipeActionState.actionItemWidthPx)
                                .roundToInt(),
                            y = 0
                        ),
                        isLabelStyle,
                        item
                    ) {
                        onEndTap?.invoke(index)
                        coroutineScope.launch {
                            state.snapTo(DragAnchor.Center)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    width: Dp,
    offset: IntOffset,
    isLabelStyle: Boolean,
    item: SwipeActionItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .offset { offset }
            .then(
                if (isLabelStyle && item.type != null) {
                    Modifier
                        .background(item.type.color)
                        .clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLabelStyle || item.icon == null) {
            Text(text = item.label, color = Color.White)
        } else {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(WeTheme.colorScheme.surface)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = WeTheme.colorScheme.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun rememberSwipeActionState(
    initialValue: DragAnchor = DragAnchor.Center,
    actionItemWidth: Dp = 80.dp,
    startActionCount: Int = 0,
    endActionCount: Int = 0
): SwipeActionState {
    val density = LocalDensity.current
    val actionItemWidthPx = density.run { (actionItemWidth).toPx() }
    val startActionWidthPx = density.run { (actionItemWidth * startActionCount).toPx() }
    val endActionWidthPx = density.run { (actionItemWidth * endActionCount).toPx() }

    // 定义锚点逻辑
    val anchors = DraggableAnchors {
        DragAnchor.Start at -startActionWidthPx
        DragAnchor.Center at 0f
        DragAnchor.End at endActionWidthPx
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue,
            anchors
        )
    }

    // 若锚点变化则更新state
    LaunchedEffect(anchors) {
        state.updateAnchors(anchors)
    }

    return SwipeActionState(
        state,
        actionItemWidthPx,
        actionItemWidth
    )
}

data class SwipeActionState(
    val draggableState: AnchoredDraggableState<DragAnchor>,
    val actionItemWidthPx: Float,
    val actionItemWidth: Dp
)