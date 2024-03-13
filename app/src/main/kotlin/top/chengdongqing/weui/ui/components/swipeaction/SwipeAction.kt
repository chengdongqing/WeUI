package top.chengdongqing.weui.ui.components.swipeaction

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import top.chengdongqing.weui.ui.theme.DangerColorLight
import top.chengdongqing.weui.ui.theme.PlainColor
import top.chengdongqing.weui.ui.theme.WarningColor
import kotlin.math.roundToInt

enum class SwipeActionStyle {
    LABEL,
    ICON
}

enum class SwipeActionType(val color: Color) {
    PLAIN(PlainColor),
    WARNING(WarningColor),
    DANGER(DangerColorLight)
}

data class SwipeActionItem(
    val type: SwipeActionType? = null,
    val label: String,
    val icon: ImageVector? = null
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeSwipeAction(
    startOptions: List<SwipeActionItem>? = null,
    endOptions: List<SwipeActionItem>? = null,
    style: SwipeActionStyle = SwipeActionStyle.LABEL,
    swipeActionState: SwipeActionState = rememberSwipeState(
        actionItemWidth = if (style == SwipeActionStyle.LABEL) 80.dp else 66.dp,
        startActionCount = startOptions?.size ?: 0,
        endActionCount = endOptions?.size ?: 0
    ),
    onStartTap: ((index: Int) -> Unit)? = null,
    onEndTap: ((index: Int) -> Unit)? = null,
    height: Dp = 60.dp,
    content: @Composable (BoxScope.() -> Unit)
) {
    val state = swipeActionState.state
    val isLabelStyle = style == SwipeActionStyle.LABEL

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
                    color = MaterialTheme.colorScheme.onBackground,
                    if (!isLabelStyle) RoundedCornerShape(8.dp) else RectangleShape
                )
                .padding(horizontal = 16.dp)
                .anchoredDraggable(state, Orientation.Horizontal, reverseDirection = true),
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
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberSwipeState(
    initialValue: DragAnchors = DragAnchors.Center,
    actionItemWidth: Dp = 80.dp,
    startActionCount: Int = 0,
    endActionCount: Int = 0
): SwipeActionState {
    val density = LocalDensity.current
    val actionItemWidthPx = density.run { (actionItemWidth).toPx() }
    val startActionWidthPx = density.run { (actionItemWidth * startActionCount).toPx() }
    val endActionWidthPx = density.run { (actionItemWidth * endActionCount).toPx() }

    val state = remember {
        AnchoredDraggableState(
            // 初始状态
            initialValue,
            // 设置每个锚点对应的位置（偏移量）
            anchors = DraggableAnchors {
                DragAnchors.Start at -startActionWidthPx
                DragAnchors.Center at 0f
                DragAnchors.End at endActionWidthPx
            },
            // 位置阀值：滑动多远距离自动进入该锚点
            positionalThreshold = { totalDistance -> totalDistance * 0.5f },
            // 速度阀值：即使没有超过位置阀值，一秒钟滑动多少个像素也能自动进入下一个锚点
            velocityThreshold = { density.run { 100.dp.toPx() } },
            // 切换状态的动画
            animationSpec = tween()
        )
    }

    return SwipeActionState(
        state,
        actionItemWidthPx,
        actionItemWidth
    )
}

@OptIn(ExperimentalFoundationApi::class)
data class SwipeActionState(
    val state: AnchoredDraggableState<DragAnchors>,
    val actionItemWidthPx: Float,
    val actionItemWidth: Dp
)

enum class DragAnchors {
    Start,
    Center,
    End
}