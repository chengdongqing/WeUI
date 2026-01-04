package top.chengdongqing.weui.core.ui.components.contextmenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.utils.toIntOffset

@Composable
fun WeContextMenu(
    state: ContextMenuState,
    onTap: (listIndex: Int, menuIndex: Int) -> Unit
) {
    if (!state.visible) return
    val props = state.props ?: return

    val layout = state.calculateLayout(LocalDensity.current, LocalWindowInfo.current.containerSize)
    var isVisible by remember { mutableStateOf(false) }

    // 动画控制逻辑
    LaunchedEffect(Unit) { isVisible = true }
    LaunchedEffect(isVisible) {
        if (!isVisible) {
            delay(160)
            state.hide()
        }
    }

    Popup(
        offset = layout.offset,
        onDismissRequest = { isVisible = false }
    ) {
        val animationSpec = tween<Float>(durationMillis = 150, easing = LinearOutSlowInEasing)

        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                initialScale = 0.4f,
                transformOrigin = layout.pivot,
                animationSpec = animationSpec
            ) + fadeIn(animationSpec),
            exit = scaleOut(
                targetScale = 0.4f,
                transformOrigin = layout.pivot,
                animationSpec = animationSpec
            ) + fadeOut(animationSpec)
        ) {
            MenuContent(props.options, state.itemWidthDp, state.itemHeightDp) { menuIndex ->
                isVisible = false
                onTap(props.listIndex, menuIndex)
            }
        }
    }
}

@Composable
fun MenuContent(options: List<String>, menuWidthDp: Dp, itemHeightDp: Dp, onTap: (Int) -> Unit) {
    Box(modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .width(menuWidthDp)
                .graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                    shape = RoundedCornerShape(4.dp)
                    clip = true
                }
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            options.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(itemHeightDp)
                        .clickable { onTap(index) }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun rememberContextMenuState(itemWidthDp: Dp = 160.dp, itemHeightDp: Dp = 50.dp) =
    remember { ContextMenuState(itemWidthDp, itemHeightDp) }

@Stable
class ContextMenuState(val itemWidthDp: Dp, val itemHeightDp: Dp) {
    var visible by mutableStateOf(false)
    var props by mutableStateOf<ContextMenuProps?>(null)
        private set

    fun show(position: IntOffset, options: List<String>, listIndex: Int) {
        props = ContextMenuProps(position, options, listIndex)
        visible = true
    }

    fun hide() {
        visible = false
    }

    /**
     * 计算弹出位置及动画原点
     */
    fun calculateLayout(density: Density, containerSize: IntSize): LayoutResult {
        val p = props ?: return LayoutResult()
        val menuWidthPx = with(density) { itemWidthDp.roundToPx() }
        val menuHeightPx = with(density) { (p.options.size * itemHeightDp.value).dp.roundToPx() }

        val isRight = p.position.x > containerSize.width / 2
        val isBottom = p.position.y > containerSize.height / 2

        return LayoutResult(
            offset = IntOffset(
                x = if (isRight) p.position.x - menuWidthPx else p.position.x,
                y = if (isBottom) p.position.y - menuHeightPx else p.position.y
            ),
            pivot = TransformOrigin(if (isRight) 1f else 0f, if (isBottom) 1f else 0f)
        )
    }

    data class LayoutResult(
        val offset: IntOffset = IntOffset.Zero,
        val pivot: TransformOrigin = TransformOrigin.Center
    )
}

data class ContextMenuProps(
    val position: IntOffset,
    val options: List<String>,
    val listIndex: Int
)

@Composable
fun Modifier.contextMenu(onLongPress: (IntOffset) -> Unit): Modifier = composed {
    var parentPosition by remember { mutableStateOf(Offset.Zero) }
    val haptic = LocalHapticFeedback.current

    this
        .onGloballyPositioned {
            parentPosition = it.positionInParent()
        }
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = { touchOffset ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onLongPress((parentPosition + touchOffset).toIntOffset())
            })
        }
}
