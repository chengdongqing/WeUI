package top.chengdongqing.weui.feature.demos.imagecropper.cropper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.ui.theme.BackgroundColorDark
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration

@Composable
fun WeImageCropper(uri: Uri, onCancel: () -> Unit, onConfirm: (Uri) -> Unit) {
    val transform = remember { mutableStateOf(ImageTransform()) }
    var cropperSize by remember { mutableStateOf(Size.Zero) }
    var initialState by remember { mutableStateOf<ImageTransform?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val toast = rememberToastState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColorDark)
    ) {
        CroppingImage(uri, cropperSize, transform) {
            initialState = it
        }
        CropperMask { cropperSize = it }
        ActionBar(
            transform,
            onCancel,
            onReset = {
                initialState?.let {
                    transform.value = it
                }
            }
        ) {
            toast.show("处理中...", ToastIcon.LOADING, Duration.INFINITE, mask = true)
            coroutineScope.launch {
                cropTransformedBitmap(context, uri, transform.value, cropperSize)?.let { bitmap ->
                    val croppedUri = saveCroppedImage(context, bitmap)
                    croppedUri?.let(onConfirm)
                }
                toast.hide()
            }
        }
    }
}

private suspend fun saveCroppedImage(context: Context, bitmap: Bitmap): Uri? {
    val tempFile = withContext(Dispatchers.IO) {
        val tempFile = File.createTempFile("cropped_", ".png", context.cacheDir).apply {
            deleteOnExit()
        }
        FileOutputStream(tempFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        tempFile
    }

    return FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
}

private suspend fun cropTransformedBitmap(
    context: Context,
    sourceUri: Uri,
    transform: ImageTransform,
    cropperSize: Size
): Bitmap? {
    val originalBitmap = loadBitmap(context, sourceUri) ?: return null

    val transformedBitmap = Bitmap.createBitmap(
        cropperSize.width.toInt(),
        cropperSize.height.toInt(),
        originalBitmap.config
    )

    Canvas(transformedBitmap).apply {
        scale(transform.scale, transform.scale, cropperSize.width / 2, cropperSize.height / 2)
        rotate(transform.rotation, cropperSize.width / 2, cropperSize.height / 2)
        translate(transform.offsetX, transform.offsetY)
        drawBitmap(originalBitmap, 0f, 0f, null)
    }

    return transformedBitmap
}

private suspend fun loadBitmap(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }
}