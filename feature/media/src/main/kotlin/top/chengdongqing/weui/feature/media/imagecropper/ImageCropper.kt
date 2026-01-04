package top.chengdongqing.weui.feature.media.imagecropper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.utils.getFileProviderUri
import top.chengdongqing.weui.core.utils.toIntSize
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration

@Composable
fun WeImageCropper(
    uri: Uri,
    state: CropperState = rememberCropperState(),
    onCancel: () -> Unit,
    onConfirm: (Uri) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val toast = rememberToastState()
    val imageBitmap by context.loadImageBitmap(uri)

    // 当图片或容器尺寸变化时，同步状态
    LaunchedEffect(imageBitmap, state.screenSize, state.boxSize) {
        imageBitmap?.let {
            state.imageSize = IntSize(it.width, it.height)
            state.reset(animate = false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { state.screenSize = it }
                .pointerInput(imageBitmap, state.boxSize) {
                    // 处理平移和缩放
                    detectTransformGestures { _, pan, zoom, _ ->
                        scope.launch {
                            state.applyTransform(pan, zoom)
                        }
                    }
                }
                .pointerInput(imageBitmap, state.boxSize) {
                    // 监听手指离开，触发回弹
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.changes.all { !it.pressed }) {
                                scope.launch { state.settle() }
                            }
                        }
                    }
                }
        ) {
            imageBitmap?.let { img ->
                withTransform({
                    translate(state.offsetX.value, state.offsetY.value)
                    scale(state.scale.value, state.scale.value, pivot = Offset.Zero)
                    rotate(state.rotation.value, pivot = Offset(img.width / 2f, img.height / 2f))
                }) {
                    drawImage(img)
                }
            }
        }

        // 非裁剪区域遮罩
        CropperMask { state.boxSize = it.toIntSize() }

        // 底部操作按钮
        ActionBar(
            onRotate = {
                scope.launch {
                    val newRotation = state.rotation.value - 90f
                    // 预计算旋转后的回弹位置
                    val target = calculateCorrectBounds(
                        state.scale.value, newRotation, state.offsetX.value, state.offsetY.value,
                        state.imageSize, state.boxSize, state.screenSize
                    )
                    launch { state.rotation.animateTo(newRotation, spring()) }
                    launch { state.offsetX.animateTo(target.x, spring()) }
                    launch { state.offsetY.animateTo(target.y, spring()) }
                }
            },
            onReset = { scope.launch { state.reset() } },
            onCancel = onCancel,
            onConfirm = {
                imageBitmap?.let { image ->
                    toast.show(
                        "处理中...",
                        ToastIcon.LOADING,
                        duration = Duration.INFINITE,
                        mask = true
                    )

                    scope.launch {
                        val resultUri = withContext(Dispatchers.IO) {
                            val bitmap = image.asAndroidBitmap().crop(state)
                            context.createImageUri(bitmap)
                        }
                        withContext(Dispatchers.Main) { onConfirm(resultUri) }
                    }
                }
            }
        )
    }
}

@Composable
private fun Context.loadImageBitmap(uri: Uri): State<ImageBitmap?> {
    return produceState(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            this@loadImageBitmap.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream).asImageBitmap()
            }
        }
    }
}

private fun Context.createImageUri(bitmap: Bitmap, quality: Int = 100): Uri {
    val tempFile = File.createTempFile("cropped_", ".jpg").apply {
        deleteOnExit()
    }

    FileOutputStream(tempFile).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }

    return getFileProviderUri(tempFile)
}
