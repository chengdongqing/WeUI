package top.chengdongqing.weui.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

// 将极坐标转换为笛卡尔坐标
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