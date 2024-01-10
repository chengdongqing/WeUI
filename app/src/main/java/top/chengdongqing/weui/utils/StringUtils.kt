package top.chengdongqing.weui.utils

/**
 * 浮点类型数值格式化
 * 没有小数则显示为整数
 * 有小数最多保留2位小数
 */
fun formatFloat(value: Float): String {
    return if (value % 1 == 0f) {
        value.toInt().toString()
    } else {
        String.format("%.2f", value)
    }
}
