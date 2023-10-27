package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch

enum class PopupDirection {
    LEFT,
    RIGHT,
    BOTTOM,
    TOP
}

@Composable
fun WePopup(
    visible: Boolean,
    direction: PopupDirection = PopupDirection.BOTTOM,
    onClose: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit
) {
    val animatedOffset = remember { Animatable(0f) }
    val alignment = when (direction) {
        PopupDirection.LEFT -> Alignment.CenterStart
        PopupDirection.RIGHT -> Alignment.CenterEnd
        PopupDirection.TOP -> Alignment.TopCenter
        PopupDirection.BOTTOM -> Alignment.BottomCenter
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(visible) {
        scope.launch {
            if (visible) {
                animatedOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(300)
                )
            } else {
                animatedOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(300)
                )
            }
        }
    }

    if (visible) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(Modifier.fillMaxSize()) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val popup = createRef()

                    Box(
                        modifier = Modifier
                            .constrainAs(popup) {
                                when (direction) {
                                    PopupDirection.LEFT, PopupDirection.RIGHT -> {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                    }

                                    PopupDirection.TOP, PopupDirection.BOTTOM -> {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                }
                            }
                            .graphicsLayer {
                                translationY = when (direction) {
                                    PopupDirection.TOP -> -450 * (1 - animatedOffset.value)
                                    PopupDirection.BOTTOM -> 450 * (1 - animatedOffset.value)
                                    else -> 0f
                                }
                                translationX = when (direction) {
                                    PopupDirection.LEFT -> -450 * (1 - animatedOffset.value)
                                    PopupDirection.RIGHT -> 450 * (1 - animatedOffset.value)
                                    else -> 0f
                                }
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .then(Modifier.fillMaxWidth()),
                        contentAlignment = alignment
                    ) {
                        content(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
