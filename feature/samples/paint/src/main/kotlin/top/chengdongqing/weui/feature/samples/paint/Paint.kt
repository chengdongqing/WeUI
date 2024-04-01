package top.chengdongqing.weui.feature.samples.paint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.utils.MediaStoreUtils
import kotlin.time.Duration

@Composable
fun PaintScreen() {
    WeScreen(
        title = "Paint",
        description = "画板",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        var color by remember { mutableStateOf(Color.Black) }
        var strokeWidth by remember { mutableFloatStateOf(15f) }
        val paths = remember { mutableStateListOf<StrokeItem>() }
        var size by remember { mutableStateOf(IntSize(100, 100)) }

        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val toast = rememberToastState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .background(Color.White)
        ) {
            DrawingTools(
                color,
                onColorChange = { color = it },
                onWidthChange = { strokeWidth = it },
                onBack = {
                    if (paths.isNotEmpty()) {
                        paths.removeAt(paths.lastIndex)
                    }
                },
                onClear = { paths.clear() },
                onSave = {
                    if (paths.isNotEmpty()) {
                        toast.show(
                            "处理中...",
                            ToastIcon.LOADING,
                            duration = Duration.INFINITE,
                            mask = true
                        )

                        coroutineScope.launch {
                            context.handleSave(paths, size)
                            delay(200)
                            toast.show("已保存到相册", ToastIcon.SUCCESS)
                        }
                    } else {
                        toast.show("画板为空", ToastIcon.FAIL)
                    }
                }
            )
            DrawingBoard(paths, color, strokeWidth) {
                size = it
            }
        }
    }
}

private suspend fun Context.handleSave(paths: List<StrokeItem>, size: IntSize) =
    withContext(Dispatchers.IO) {
        // 创建和画板同样大小的位图
        val bitmap = Bitmap.createBitmap(
            size.width,
            size.height,
            Bitmap.Config.ARGB_8888
        )
        // 绘制轨迹到bitmap
        bitmap.drawToNativeCanvas(paths)

        // 保存到相册
        saveToAlbum(
            bitmap,
            "drawing_${System.currentTimeMillis()}.png"
        )
    }

private fun Bitmap.drawToNativeCanvas(paths: List<StrokeItem>) {
    val canvas = android.graphics.Canvas(this).apply {
        drawColor(Color.White.toArgb())
    }

    paths.forEach { strokeItem ->
        val paint = Paint().apply {
            color = strokeItem.color.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = strokeItem.width
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        canvas.drawPath(strokeItem.path.asAndroidPath(), paint)
    }
}

private fun Context.saveToAlbum(bitmap: Bitmap, filename: String) {
    val contentUri = MediaStoreUtils.getContentUri(MediaType.IMAGE)
    val contentValues = MediaStoreUtils.createContentValues(
        filename,
        mediaType = MediaType.IMAGE,
        mimeType = "image/png",
        this
    )

    contentResolver.insert(contentUri, contentValues)?.let { uri ->
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            MediaStoreUtils.finishPending(uri, this)
        }
    }
}
