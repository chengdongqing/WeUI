package top.chengdongqing.weui.feature.media.imagecropper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
internal fun BoxScope.CropperMask(onSizeChange: (Size) -> Unit) {
    Canvas(modifier = Modifier.matchParentSize()) {
        val spacing = 25.dp.toPx()
        val width = size.width - spacing * 2
        val rectSize = Size(width, width)
        val rectOffset = Offset(x = spacing, y = (size.height - width) / 2)
        onSizeChange(rectSize)

        // 构建遮罩路径
        val combinedPath = Path().apply {
            // 添加全屏矩形
            addRect(Rect(Offset.Zero, size))
            // 添加中间裁剪框矩形
            addRect(Rect(rectOffset, rectSize))
            // 设置填充类型为 EvenOdd (奇偶填充)
            // 重叠的部分（中间框）就会被“挖空”
            fillType = PathFillType.EvenOdd
        }
        // 绘制路径
        drawPath(
            path = combinedPath,
            color = Color.Black.copy(alpha = 0.4f)
        )

        // 绘制四个角
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