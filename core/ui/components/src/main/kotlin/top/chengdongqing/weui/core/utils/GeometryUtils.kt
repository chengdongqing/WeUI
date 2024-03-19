package top.chengdongqing.weui.core.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

/**
 * 将极坐标转换为笛卡尔坐标
 */
fun polarToCartesian(
    center: Offset,
    radius: Float,
    angleRadians: Double
): Pair<Float, Float> {
    return Pair(
        center.x + (radius * cos(angleRadians)).toFloat(),
        center.y + (radius * sin(angleRadians)).toFloat()
    )
}

/**
 * 计算贝塞尔曲线
 */
fun calculateBezierPath(start: Offset, end: Offset, progress: Float): Offset {
    val controlPoint = Offset((start.x + end.x) / 2, start.y - 200f)

    val x = (1 - progress) * (1 - progress) * start.x + 2 * (1 - progress) *
            progress * controlPoint.x + progress * progress * end.x
    val y = (1 - progress) * (1 - progress) * start.y + 2 * (1 - progress) *
            progress * controlPoint.y + progress * progress * end.y

    return Offset(x, y)
}