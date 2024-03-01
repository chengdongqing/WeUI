package top.chengdongqing.weui.utils

import androidx.compose.ui.graphics.Color

fun generateDistinctColors(count: Int): List<Color> {
    return List(count) { i ->
        // 在360度色相环上均匀分布颜色
        val hue = (360 * i / count) % 360
        // 使用HSV色彩空间转换为Color
        Color.hsv(hue.toFloat(), 0.85f, 0.85f)
    }
}
