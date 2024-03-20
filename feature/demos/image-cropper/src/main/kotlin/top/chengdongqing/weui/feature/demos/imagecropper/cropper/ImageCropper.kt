package top.chengdongqing.weui.feature.demos.imagecropper.cropper

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import top.chengdongqing.weui.core.ui.theme.BackgroundColorDark
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WeImageCropper(uri: Uri, onChange: () -> Unit) {
    val transform = remember { mutableStateOf(ImageTransform()) }
    var cropperSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColorDark),
        contentAlignment = Alignment.Center
    ) {
        CroppingImage(uri, transform)
        CropperMask { cropperSize = it }
        ActionBar(transform, onChange = { transform.value = it }) {
            onChange()
        }
    }
}

@Composable
private fun CroppingImage(
    uri: Uri,
    transform: MutableState<ImageTransform>
) {
    val animatedRotation by animateFloatAsState(targetValue = transform.value.rotation, label = "")

    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = transform.value.scale,
                scaleY = transform.value.scale,
                rotationZ = animatedRotation,
                translationX = transform.value.offsetX,
                translationY = transform.value.offsetY
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val adjustedPan = adjustPanForRotation(pan, -transform.value.rotation)

                    val scale = (transform.value.scale * zoom).coerceIn(0.5f, 5f)
                    val offsetX = transform.value.offsetX + adjustedPan.x
                    val offsetY = transform.value.offsetY + adjustedPan.y

                    transform.value = transform.value.copy(
                        scale = scale,
                        offsetX = offsetX,
                        offsetY = offsetY
                    )
                }
            },
        contentScale = ContentScale.Fit
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

internal data class ImageTransform(
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f
)

@Preview
@Composable
private fun PreviewImageCropper() {
    WeUITheme {
        WeImageCropper(uri = Uri.parse("https://s1.xiaomiev.com/activity-outer-assets/web/su7/1-3_m.jpg")) {

        }
    }
}