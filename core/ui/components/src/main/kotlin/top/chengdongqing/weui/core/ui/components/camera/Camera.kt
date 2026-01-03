package top.chengdongqing.weui.core.ui.components.camera

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.utils.RequestCameraPermission

@Composable
fun WeCamera(
    type: VisualMediaType,
    onRevoked: () -> Unit,
    onCapture: (Uri, VisualMediaType) -> Unit
) {
    RequestCameraPermission(onRevoked = onRevoked) {
        val state = rememberCameraState(type, onCapture = onCapture)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // 对焦
                            state.focus(offset)
                        }
                    }
            ) {
                CameraPreview(state)
                ControlBar(state)

                // 对焦框
                state.focusPoint?.let { offset ->
                    key(offset) {
                        FocusRing(offset)
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(state: CameraState) {
    AndroidView(
        factory = { state.previewView },
        modifier = Modifier.fillMaxSize(),
        update = { state.updateCamera() }
    )
}

@Composable
fun FocusRing(offset: Offset) {
    val ringSizeDp = 60.dp
    val scale = remember { Animatable(1.5f) } // 初始大一点
    val opacity = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 点击瞬间缩小并变亮
        launch {
            scale.animateTo(1f, spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow))
        }
        launch {
            opacity.animateTo(1f, tween(150))
            delay(1000)
            opacity.animateTo(0f, tween(400))
        }
    }

    Canvas(
        modifier = Modifier
            .size(ringSizeDp)
            .offset {
                IntOffset(
                    (offset.x - ringSizeDp.toPx() / 2).toInt(),
                    (offset.y - ringSizeDp.toPx() / 2).toInt()
                )
            }
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                alpha = opacity.value
            }
    ) {
        val ringSize = size.width
        val strokeWidth = 1.dp.toPx()
        val lineLen = 6.dp.toPx()

        // 画外框
        drawRect(
            color = Color.White,
            style = Stroke(width = strokeWidth)
        )

        // 画四边的小短线
        drawLine(Color.White, Offset(ringSize / 2, 0f), Offset(ringSize / 2, lineLen), strokeWidth)
        drawLine(
            Color.White,
            Offset(ringSize / 2, ringSize),
            Offset(ringSize / 2, ringSize - lineLen),
            strokeWidth
        )
        drawLine(Color.White, Offset(0f, ringSize / 2), Offset(lineLen, ringSize / 2), strokeWidth)
        drawLine(
            Color.White,
            Offset(ringSize, ringSize / 2),
            Offset(ringSize - lineLen, ringSize / 2),
            strokeWidth
        )
    }
}
