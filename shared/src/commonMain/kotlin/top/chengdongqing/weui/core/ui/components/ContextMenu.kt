package top.chengdongqing.weui.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.PopupProperties
import top.chengdongqing.weui.core.ui.theme.WeTheme
import kotlin.math.roundToInt

@Composable
fun WeContextMenu(
    state: ContextMenuState,
    onClick: (listIndex: Int, menuIndex: Int) -> Unit
) {
    val props = state.props ?: return

    val density = LocalDensity.current
    val containerSize = LocalWindowInfo.current.containerSize
    val layout = remember(state.visible) {
        state.calculateLayout(density, containerSize)
    }

    val visibilityState = remember { MutableTransitionState(false) }
    LaunchedEffect(state.visible) {
        visibilityState.targetState = state.visible
    }

    if (visibilityState.currentState || visibilityState.targetState) {
        Popup(
            offset = layout.offset,
            onDismissRequest = { state.hide() },
            properties = PopupProperties(focusable = true)
        ) {
            val animationSpec = tween<Float>(durationMillis = 150, easing = LinearOutSlowInEasing)

            AnimatedVisibility(
                visibleState = visibilityState,
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
                    state.hide()
                    onClick(props.listIndex, menuIndex)
                }
            }
        }
    }
}

@Composable
fun MenuContent(options: List<String>, menuWidthDp: Dp, itemHeightDp: Dp, onClick: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .width(menuWidthDp),
        color = WeTheme.colorScheme.surfaceVariant,
        shadowElevation = 12.dp,
        tonalElevation = 2.dp
    ) {
        Column {
            options.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(itemHeightDp)
                        .clickable { onClick(index) }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        fontSize = 15.sp,
                        color = WeTheme.colorScheme.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun rememberContextMenuState(itemWidthDp: Dp = 140.dp, itemHeightDp: Dp = 50.dp) =
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
fun Modifier.weContextMenu(
    onClick: (() -> Unit)? = null,
    onLongClick: (IntOffset) -> Unit
): Modifier = composed {
    var parentPosition by remember { mutableStateOf(Offset.Zero) }
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    this
        .onGloballyPositioned {
            parentPosition = it.positionInParent()
        }
        .indication(
            interactionSource = interactionSource,
            indication = if (onClick != null) LocalIndication.current else null
        )
        .pointerInput(onClick, onLongClick) {
            detectTapGestures(
                onLongPress = { touchOffset ->
                    val finalOffset = (parentPosition + touchOffset).toIntOffset()
                    onLongClick(finalOffset)
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) // 轻微振动
                },
                onPress = { offset ->
                    // 触发波纹
                    val press = PressInteraction.Press(offset)
                    interactionSource.emit(press)
                    // 等待释放或取消
                    val released = tryAwaitRelease()
                    // 隐藏波纹
                    if (released) {
                        interactionSource.emit(PressInteraction.Release(press))
                    } else {
                        interactionSource.emit(PressInteraction.Cancel(press))
                    }
                },
            ) {
                onClick?.invoke()
            }
        }
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())