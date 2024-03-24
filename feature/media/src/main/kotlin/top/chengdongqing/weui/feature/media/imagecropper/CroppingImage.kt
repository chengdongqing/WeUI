package top.chengdongqing.weui.feature.media.imagecropper

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import coil.compose.AsyncImage
import kotlin.math.max

@Composable
internal fun BoxScope.CroppingImage(
    uri: Uri,
    boxSize: Size,
    transform: MutableState<ImageTransform>,
    onInitialStateCalculated: (ImageTransform) -> Unit
) {
    var imageSize by remember { mutableStateOf(Size.Zero) }

    InitialStateEffect(
        boxSize,
        imageSize,
        transform,
        onInitialStateCalculated
    )

    val animatedRotation by animateFloatAsState(
        targetValue = transform.value.rotation,
        label = "CroppingImageRotateAnimation"
    )

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        transform.value = transform.value.calculateNewTransform(
            boxSize, imageSize, panChange, zoomChange
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .matchParentSize()
            .transformable(transformableState)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            onState = {
                it.painter?.intrinsicSize?.let { size ->
                    imageSize = size
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = transform.value.scale,
                    scaleY = transform.value.scale,
                    rotationZ = animatedRotation,
                    translationX = transform.value.offsetX,
                    translationY = transform.value.offsetY
                )
        )
    }
}

@Composable
private fun InitialStateEffect(
    cropperSize: Size,
    imageSize: Size,
    transform: MutableState<ImageTransform>,
    onInitialStateCalculated: (ImageTransform) -> Unit
) {
    LaunchedEffect(cropperSize, imageSize) {
        if (cropperSize != Size.Zero && imageSize != Size.Zero) {
            // 确保图片至少填充整个裁剪框
            val scale = max(
                cropperSize.width / imageSize.width,
                cropperSize.height / imageSize.height
            )

            // 根据图片与裁剪框的尺寸差异计算偏移量，确保图片居中
            val offsetX = if (imageSize.width * scale < cropperSize.width) {
                // 如果缩放后的图片宽度小于裁剪框宽度，说明是高图，需要水平居中
                (cropperSize.width - imageSize.width * scale) / 2f
            } else {
                // 否则，图片宽于或等于裁剪框宽度，不需要额外偏移
                0f
            }

            val offsetY = if (imageSize.height * scale < cropperSize.height) {
                // 如果缩放后的图片高度小于裁剪框高度，说明是宽图，需要垂直居中
                (cropperSize.height - imageSize.height * scale) / 2f
            } else {
                // 否则，图片高于或等于裁剪框高度，不需要额外偏移
                0f
            }

            transform.value = ImageTransform(
                scale = scale,
                offsetX = offsetX,
                offsetY = offsetY,
                rotation = transform.value.rotation
            )
            onInitialStateCalculated(transform.value)
        }
    }
}

private fun ImageTransform.calculateNewTransform(
    boxSize: Size,
    imageSize: Size,
    pan: Offset,
    zoom: Float
): ImageTransform {
    val minScale = max(
        boxSize.width / imageSize.width,
        boxSize.height / imageSize.height
    )
    val newScale = (scale * zoom).coerceIn(minScale, 5f)

    var newOffsetX = offsetX + pan.x
    var newOffsetY = offsetY + pan.y

    val scaledImageWidth = imageSize.width * newScale
    val scaledImageHeight = imageSize.height * newScale
    val overflowX = max(0f, (scaledImageWidth - boxSize.width) / 2f)
    val overflowY = max(0f, (scaledImageHeight - boxSize.height) / 2f)

    newOffsetX = newOffsetX.coerceIn(-overflowX, overflowX)
    newOffsetY = newOffsetY.coerceIn(-overflowY, overflowY)

    return copy(
        scale = newScale,
        offsetX = newOffsetX,
        offsetY = newOffsetY
    )
}