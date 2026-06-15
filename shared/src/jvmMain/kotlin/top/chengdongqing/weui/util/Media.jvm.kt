package top.chengdongqing.weui.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlin.coroutines.resume

actual suspend fun saveBitmap(
    bitmap: ImageBitmap,
    filename: String
): Boolean {
    // 获取图片bytes
    val data = withContext(Dispatchers.Default) {
        val image = Image.makeFromBitmap(bitmap.asSkiaBitmap())
        image.encodeToData(EncodedImageFormat.WEBP, 90)?.bytes
    } ?: return false

    // 调用原生 FileDialog
    return withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->
            val dialog = FileDialog(null as Frame?, "保存图片", FileDialog.SAVE).apply {
                file = filename
                isVisible = true // 阻塞 UI 线程直到用户操作
            }

            val directory = dialog.directory
            val file = dialog.file

            if (directory != null && file != null) {
                val targetFile = File(directory, file)
                // 异步写入文件
                launch(Dispatchers.IO) {
                    try {
                        targetFile.writeBytes(data)
                        continuation.resume(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        continuation.resume(false)
                    }
                }
            } else {
                continuation.resume(false)
            }
        }
    }
}