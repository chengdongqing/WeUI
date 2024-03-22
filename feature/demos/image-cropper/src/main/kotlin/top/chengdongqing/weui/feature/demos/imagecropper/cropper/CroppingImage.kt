package top.chengdongqing.weui.feature.demos.imagecropper.cropper

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

@Composable
internal fun BoxScope.CroppingImage(
    uri: Uri,
    cropperSize: Size,
    transform: MutableState<ImageTransform>,
    onInitialStateCalculated: (ImageTransform) -> Unit
) {
    var imageSize by remember { mutableStateOf(Size.Zero) }

    InitialStateEffect(
        cropperSize,
        imageSize,
        transform,
        onInitialStateCalculated
    )

    val animatedRotation by animateFloatAsState(
        targetValue = transform.value.rotation,
        label = "CroppingImageRotateAnimation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .matchParentSize()
            .pointerInput(cropperSize, imageSize) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val adjustedPan = adjustPanForRotation(pan, -transform.value.rotation)

                    transform.value = transform.value.calculateNewTransform(
                        cropperSize,
                        imageSize,
                        adjustedPan,
                        zoom
                    )
                }
            }
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Fit,
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
    cropperSize: Size,
    imageSize: Size,
    adjustedPan: Offset,
    zoom: Float
): ImageTransform {
    val minScale = max(
        cropperSize.width / imageSize.width,
        cropperSize.height / imageSize.height
    )
    val newScale = (scale * zoom).coerceIn(minScale, 5f)

    var newOffsetX = offsetX + adjustedPan.x
    var newOffsetY = offsetY + adjustedPan.y

    val scaledImageWidth = imageSize.width * newScale
    val scaledImageHeight = imageSize.height * newScale
    val overflowX = max(0f, (scaledImageWidth - cropperSize.width) / 2f)
    val overflowY = max(0f, (scaledImageHeight - cropperSize.height) / 2f)

    newOffsetX = newOffsetX.coerceIn(-overflowX, overflowX)
    newOffsetY = newOffsetY.coerceIn(-overflowY, overflowY)

    return copy(
        scale = newScale,
        offsetX = newOffsetX,
        offsetY = newOffsetY
    )
}

/**
 * 根据图片的旋转角度调整拖动方向
 *
 * @param pan 原始拖动向量
 * @param rotationDegrees 图片的旋转角度
 * @return 调整后的拖动向量
 */
private fun adjustPanForRotation(pan: Offset, rotationDegrees: Float): Offset {
    // 将角度转换为弧度
    val radians = Math.toRadians(rotationDegrees.toDouble())
    // 计算旋转的余弦值和正弦值
    val cos = cos(radians).toFloat()
    val sin = sin(radians).toFloat()

    // 对拖动向量应用旋转变换，考虑到旋转矩阵的使用
    return Offset(
        x = pan.x * cos + pan.y * sin,
        y = -pan.x * sin + pan.y * cos
    )
}