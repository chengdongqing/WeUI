package top.chengdongqing.weui.feature.media.imagecropper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.toOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.utils.getFileProviderUri
import top.chengdongqing.weui.core.utils.toIntSize
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.time.Duration

@Composable
fun WeImageCropper(uri: Uri, onCancel: () -> Unit, onConfirm: (Uri) -> Unit) {
    val context = LocalContext.current
    val imageBitmap by context.loadImageBitmap(uri)
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    val transform = remember { mutableStateOf(ImageTransform()) }
    var initialTransform by remember { mutableStateOf<ImageTransform?>(null) }
    val animatedRotation by animateFloatAsState(targetValue = transform.value.rotation, label = "")

    val coroutineScope = rememberCoroutineScope()
    val toast = rememberToastState()

    TransformInitializeEffect(
        screenSize,
        boxSize,
        imageBitmap,
        transform
    ) {
        initialTransform = it
    }

    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    screenSize = it
                }
                .background(Color.Red)
                .transformable(rememberTransformableState { zoomChange, panChange, _ ->
                    imageBitmap?.let { image ->
                        transform.value = transform.value.createNewTransform(
                            zoomChange,
                            panChange,
                            boxSize,
                            image
                        )
                    }
                })
        ) {
            imageBitmap?.let {
                rotate(degrees = animatedRotation) {
                    scale(transform.value.scale) {
                        drawImage(
                            it,
                            dstOffset = IntOffset(
                                transform.value.offsetX.roundToInt(),
                                transform.value.offsetY.roundToInt()
                            )
                        )
                    }
                }
            }
        }
        CropperMask {
            boxSize = it.toIntSize()
        }
        ActionBar(
            transform,
            onCancel = onCancel,
            onReset = {
                initialTransform?.let {
                    transform.value = it
                }
            }
        ) {
            imageBitmap?.let {
                toast.show(
                    "处理中...",
                    ToastIcon.LOADING,
                    duration = Duration.INFINITE,
                    mask = true
                )

                coroutineScope.launch(Dispatchers.IO) {
                    val bitmap = it.drawToNativeCanvas(screenSize, boxSize, transform.value)
                    context.createImageUri(bitmap).apply(onConfirm)
                }
            }
        }
    }
}

@Composable
private fun TransformInitializeEffect(
    screenSize: IntSize,
    boxSize: IntSize,
    imageBitmap: ImageBitmap?,
    transform: MutableState<ImageTransform>,
    onInit: (ImageTransform) -> Unit
) {
    LaunchedEffect(boxSize, imageBitmap) {
        if (screenSize != IntSize.Zero && boxSize != IntSize.Zero && imageBitmap != null) {
            val scale = max(
                boxSize.width / imageBitmap.width.toFloat(),
                boxSize.height / imageBitmap.height.toFloat()
            )
            val offsetX = (screenSize.width - imageBitmap.width) / 2f
            val offsetY = (screenSize.height - imageBitmap.height) / 2f

            transform.value = ImageTransform(
                scale = scale,
                offsetX = offsetX,
                offsetY = offsetY
            ).also(onInit)
        }
    }
}

@Composable
private fun Context.loadImageBitmap(uri: Uri): State<ImageBitmap?> {
    val context = this

    return produceState<ImageBitmap?>(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream).asImageBitmap()
            }
        }
    }
}

private fun ImageTransform.createNewTransform(
    zoomChange: Float,
    panChange: Offset,
    boxSize: IntSize,
    image: ImageBitmap
): ImageTransform {
    val minScale = max(
        boxSize.width / image.width.toFloat(),
        boxSize.height / image.height.toFloat()
    )
    val newScale = (scale * zoomChange).coerceIn(minScale, 5f)

    var newOffsetX = offsetX + panChange.x * newScale
    var newOffsetY = offsetY + panChange.y * newScale

    return copy(
        scale = newScale,
        offsetX = newOffsetX,
        offsetY = newOffsetY
    )
}

private fun ImageBitmap.drawToNativeCanvas(
    screenSize: IntSize,
    boxSize: IntSize,
    transform: ImageTransform
): Bitmap {
    val bitmap = Bitmap.createBitmap(
        screenSize.width,
        screenSize.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)

    transform.apply {
        val offset = screenSize.center.toOffset()
        val matrix = Matrix().apply {
            postTranslate(offsetX, offsetY)
            postRotate(rotation, offset.x, offset.y)
            postScale(scale, scale, offset.x, offset.y)
        }
        canvas.drawBitmap(asAndroidBitmap(), matrix, null)
    }

    val offset = IntSize(
        width = screenSize.width - boxSize.width,
        height = screenSize.height - boxSize.height
    ).center

    return Bitmap.createBitmap(
        bitmap,
        offset.x,
        offset.y,
        boxSize.width,
        boxSize.height
    )
}

private fun Context.createImageUri(bitmap: Bitmap): Uri {
    val tempFile = File.createTempFile("cropped_", ".png").apply {
        deleteOnExit()
    }

    FileOutputStream(tempFile).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }

    return getFileProviderUri(tempFile)
}