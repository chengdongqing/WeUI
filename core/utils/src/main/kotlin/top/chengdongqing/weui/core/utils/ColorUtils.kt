package top.chengdongqing.weui.core.utils

import androidx.compose.ui.graphics.Color

/**
 * 生成不重复的随机颜色
 * @param count 需要的颜色数量
 */
fun generateColors(count: Int): List<Color> {
    return List(count) { i ->
        // 在360度色相环上均匀分布颜色
        val hue = (360 * i / count) % 360
        // 使用HSV色彩空间转换为Color
        Color.hsv(hue.toFloat(), 0.85f, 0.85f)
    }
}