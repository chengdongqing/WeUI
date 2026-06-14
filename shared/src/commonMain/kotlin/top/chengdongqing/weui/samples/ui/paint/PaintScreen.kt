package top.chengdongqing.weui.samples.ui.paint

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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.components.ToastIcon
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.components.rememberToastState
import top.chengdongqing.weui.util.saveBitmap
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PaintScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Paint",
        description = "画板",
        padding = PaddingValues(0.dp),
        scrollEnabled = false,
        onBack = onBack
    ) {
        var color by remember { mutableStateOf(Color.Black) }
        var strokeWidth by remember { mutableFloatStateOf(15f) }
        val paths = remember { mutableStateListOf<StrokeItem>() }
        var size by remember { mutableStateOf(IntSize(100, 100)) }

        val coroutineScope = rememberCoroutineScope()
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
                            title = "处理中...",
                            icon = ToastIcon.Loading,
                            duration = Duration.INFINITE,
                            mask = true
                        )

                        coroutineScope.launch {
                            val imageBitmap = drawPathsToImage(paths, size)
                            val filename =
                                "drawing_${Clock.System.now().toEpochMilliseconds()}.webp"
                            val result = saveBitmap(imageBitmap, filename)

                            toast.hide()
                            delay(200.milliseconds)
                            if (result) {
                                toast.show("已保存到相册", ToastIcon.Success)
                            } else {
                                toast.show("保存失败", ToastIcon.Fail)
                            }
                        }
                    } else {
                        toast.show("画板为空", ToastIcon.Fail)
                    }
                }
            )

            DrawingBoard(
                paths,
                color,
                strokeWidth
            ) {
                size = it
            }
        }
    }
}

private fun drawPathsToImage(paths: List<StrokeItem>, size: IntSize): ImageBitmap {
    // 创建离屏画布
    val imageBitmap = ImageBitmap(size.width, size.height)
    val canvas = Canvas(imageBitmap)

    // 使用 DrawScope 绘制
    val drawScope = CanvasDrawScope()
    val sizePx = Size(size.width.toFloat(), size.height.toFloat())

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = sizePx
    ) {
        // 绘制背景
        drawRect(Color.White)

        // 绘制轨迹
        paths.forEach { item ->
            drawPath(
                path = item.path,
                color = item.color,
                style = Stroke(
                    width = item.width,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
    return imageBitmap
}