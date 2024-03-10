package top.chengdongqing.weui.ui.components.imagecropper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp

@Composable
internal fun BoxScope.CropperMask(onSize: (Size) -> Unit) {
    Canvas(modifier = Modifier.matchParentSize()) {
        val spacing = 25.dp.toPx()
        val width = size.width - spacing * 2
        onSize(Size(width, width))

        drawIntoCanvas { canvas ->
            canvas.saveLayer(bounds = Rect(offset = Offset.Zero, size = size), Paint())
            // 绘制遮罩
            drawRect(color = Color(0f, 0f, 0f, 0.4f))
            // 绘制透明区域
            canvas.saveLayer(
                bounds = Rect(
                    offset = Offset(x = spacing, y = size.height / 2 - width / 2),
                    size = Size(width, width)
                ),
                paint = Paint().apply { blendMode = BlendMode.Clear }
            )
            canvas.restore()
        }

        // 构造并绘制四个角的装饰
        drawCorners(spacing, width)
    }
}

private fun DrawScope.drawCorners(space: Float, width: Float) {
    val cornerSize = 10.dp.toPx()
    val strokeWidth = 2.dp.toPx()

    val cornerPaths = listOf(
        // 左上角
        Path().apply {
            moveTo(space, size.height / 2 - width / 2 + cornerSize)
            lineTo(space, size.height / 2 - width / 2)
            lineTo(space + cornerSize, size.height / 2 - width / 2)
        },
        // 右上角
        Path().apply {
            moveTo(size.width - space, size.height / 2 - width / 2 + cornerSize)
            lineTo(size.width - space, size.height / 2 - width / 2)
            lineTo(size.width - space - cornerSize, size.height / 2 - width / 2)
        },
        // 左下角
        Path().apply {
            moveTo(space, size.height / 2 + width / 2 - cornerSize)
            lineTo(space, size.height / 2 + width / 2)
            lineTo(space + cornerSize, size.height / 2 + width / 2)
        },
        // 右下角
        Path().apply {
            moveTo(size.width - space, size.height / 2 + width / 2 - cornerSize)
            lineTo(size.width - space, size.height / 2 + width / 2)
            lineTo(size.width - space - cornerSize, size.height / 2 + width / 2)
        }
    )
    cornerPaths.forEach { path ->
        drawPath(path, color = Color.White, style = Stroke(width = strokeWidth))
    }
}