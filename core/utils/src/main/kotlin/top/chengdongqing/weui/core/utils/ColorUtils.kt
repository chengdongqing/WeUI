package top.chengdongqing.weui.core.utils

import androidx.compose.ui.graphics.Color

/**
 * 生成不重复的随机颜色
 *
 * @param count 需要的颜色数量
 * @param saturation 饱和度 (0.0 - 1.0)，值越小越“清新/粉嫩”，值越大越“浓郁/鲜艳”，一般 40%-60%
 * @param value 明度 (0.0 - 1.0)，值越大越“明亮/通透”，一般 90% 以上
 */
fun generateColors(
    count: Int,
    saturation: Float = 0.55f, // 默认清新风格
    value: Float = 1.0f        // 默认高亮通透
): List<Color> {
    // 使用黄金分割比 (Golden Ratio) 0.618 来让颜色分布更自然，避免相邻颜色太接近
    val goldenRatioConjugate = 0.618034f

    return List(count) { i ->
        val hue = (i * goldenRatioConjugate * 360) % 360
        Color.hsv(hue, saturation, value)
    }
}
