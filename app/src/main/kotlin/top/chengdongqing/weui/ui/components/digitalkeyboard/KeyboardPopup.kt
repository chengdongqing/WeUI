package top.chengdongqing.weui.ui.components.digitalkeyboard

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.delay

@Composable
internal fun KeyboardPopup(visible: Boolean, onHide: () -> Unit, content: @Composable () -> Unit) {
    val popupPositionProvider = remember {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                return IntOffset(0, windowSize.height)
            }
        }
    }

    var localVisible by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (!visible) {
            delay(250)
        }
        localVisible = visible
    }
    BackHandler(visible) {
        onHide()
    }

    if (visible || localVisible) {
        Popup(popupPositionProvider) {
            AnimatedVisibility(
                visible = visible && localVisible,
                enter = slideInVertically(
                    animationSpec = remember {
                        tween(250)
                    },
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = remember {
                        tween(250)
                    },
                    targetOffsetY = { it }
                )
            ) {
                content()
            }
        }
    }
}