package top.chengdongqing.weui.core.ui.components.mediapreview

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import top.chengdongqing.weui.core.utils.detectTransformGesturesWithManualConsuming
import top.chengdongqing.weui.core.utils.rememberLastState

@Composable
fun ImagePreview(uri: Uri) {
    val activity = LocalContext.current as Activity
    var offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableFloatStateOf(1f) }
    val lastOffset = rememberLastState(value = offset)

    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { tapOffset ->
                    scale = if (scale > 1f) 1f else 2f
                    offset = tapOffset.calculateDoubleTapOffset(scale, size)
                }) {
                    activity.finish()
                }
            }
            .pointerInput(Unit) {
                detectTransformGesturesWithManualConsuming { centroid, pan, zoom, _, consumeChanges ->
                    offset = offset.calculateNewOffset(centroid, pan, scale, zoom, size)
                    scale = maxOf(1f, scale * zoom)
                    if (scale > 1f && offset.x != lastOffset.value.x) {
                        consumeChanges()
                    }
                }
            }
            .graphicsLayer {
                translationX = -offset.x * scale
                translationY = -offset.y * scale
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
    )
}

private fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) - (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}

private fun Offset.calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize
): Offset {
    return Offset(
        this.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        this.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}