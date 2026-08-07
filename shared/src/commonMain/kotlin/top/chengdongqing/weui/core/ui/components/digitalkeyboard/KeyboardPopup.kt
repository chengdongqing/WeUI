package top.chengdongqing.weui.core.ui.components.digitalkeyboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import top.chengdongqing.weui.navigation.BackHandler

@Composable
internal fun KeyboardPopup(
    visible: Boolean,
    onHide: () -> Unit,
    content: @Composable () -> Unit
) {
    val visibilityState = remember { MutableTransitionState(false) }
    LaunchedEffect(visible) {
        visibilityState.targetState = visible
    }

    BackHandler(visible, onHide)

    if (visibilityState.currentState || visibilityState.targetState) {
        Popup(popupPositionProvider = PopupPositionProvider) {
            AnimatedVisibility(
                visibleState = visibilityState,
                enter = slideInVertically(
                    animationSpec = tween(250),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(250),
                    targetOffsetY = { it }
                )
            ) {
                content()
            }
        }
    }
}

private object PopupPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(0, windowSize.height)
    }
}