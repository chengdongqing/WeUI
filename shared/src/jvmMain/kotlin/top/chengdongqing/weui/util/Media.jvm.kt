package top.chengdongqing.weui.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File
import javax.swing.JFileChooser
import kotlin.coroutines.resume

actual suspend fun saveBitmap(
    bitmap: ImageBitmap,
    filename: String
): Boolean {
    // 在 IO 线程进行位图处理
    val data = withContext(Dispatchers.Default) {
        val image = Image.makeFromBitmap(bitmap.asSkiaBitmap())
        image.encodeToData(EncodedImageFormat.WEBP, 90)?.bytes
    } ?: return false

    // 切换到 Swing 事件调度线程 (EDT) 处理 UI 对话框
    return withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->
            val fileChooser = JFileChooser().apply {
                dialogTitle = "保存图片"
                selectedFile = File(filename)
            }

            val result = fileChooser.showSaveDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                // 在后台线程执行最终的文件写入
                launch(Dispatchers.IO) {
                    try {
                        file.writeBytes(data)
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