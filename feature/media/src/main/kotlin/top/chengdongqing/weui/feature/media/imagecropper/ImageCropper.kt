package top.chengdongqing.weui.feature.media.imagecropper

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.ui.theme.BackgroundColorDark
import top.chengdongqing.weui.core.utils.getFileProviderUri
import top.chengdongqing.weui.core.utils.toIntSize
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration

@Composable
fun WeImageCropper(uri: Uri, onCancel: () -> Unit, onConfirm: (Uri) -> Unit) {
    val transform = remember { mutableStateOf(ImageTransform()) }
    var boxSize by remember { mutableStateOf(Size.Zero) }
    var initialState by remember { mutableStateOf<ImageTransform?>(null) }

    val toast = rememberToastState()
    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenSize = density.run {
        Size(
            width = configuration.screenWidthDp.dp.toPx(),
            height = configuration.screenHeightDp.dp.toPx()
        )
    }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColorDark)
    ) {
        CroppingImage(
            uri,
            boxSize,
            transform
        ) {
            initialState = it
        }
        CropperMask { boxSize = it }
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
                context.cropImage(
                    uri,
                    boxSize,
                    screenSize,
                    transform.value
                )?.let { bitmap ->
                    val croppedUri = saveCroppedImage(context, bitmap)
                    onConfirm(croppedUri)
                }
                toast.hide()
            }
        }
    }
}

private suspend fun saveCroppedImage(context: Context, bitmap: Bitmap): Uri {
    val tempFile = withContext(Dispatchers.IO) {
        val tempFile = File.createTempFile("cropped_", ".png").apply {
            deleteOnExit()
        }
        FileOutputStream(tempFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        tempFile
    }

    return context.getFileProviderUri(tempFile)
}

private suspend fun Context.cropImage(
    uri: Uri,
    boxSize: Size,
    screenSize: Size,
    transform: ImageTransform
): Bitmap? {
    val originalBitmap = loadBitmap(this, uri) ?: return null

    val intSize = boxSize.toIntSize()
    val targetBitmap = Bitmap.createBitmap(
        intSize.width,
        intSize.height,
        originalBitmap.config
    )

    Canvas(targetBitmap).apply {
        // 将Canvas的原点移动到中心
        translate(
            boxSize.width / 2f,
            boxSize.height / 2f
        )
        // 应用旋转变换
        rotate(transform.rotation)

        // 应用缩放变换
        val scale = transform.scale * (screenSize.width / boxSize.width)
        scale(scale, scale)

        // 计算偏移量
        val offsetX =
            (transform.offsetX + (screenSize.width - boxSize.width) / 2) * (boxSize.width / screenSize.width)
        val offsetY =
            (transform.offsetY + (screenSize.height - boxSize.height) / 2) * (boxSize.height / screenSize.height)

        drawBitmap(
            originalBitmap,
            -originalBitmap.width / 2 + offsetX,
            -originalBitmap.height / 2 + offsetY,
            null
        )
    }

    return targetBitmap
}

private suspend fun loadBitmap(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }
}