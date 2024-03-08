package top.chengdongqing.weui.utils

fun formatFloat(value: Float, decimals: Int = 2): String {
    return if (value % 1 == 0f) {
        value.toInt().toString()
    } else {
        "%.${decimals}f".format(value)
        // %.2f: 格式说明符，用于格式化浮点数（f 表示浮点数）。.2 表示小数点后保留两位数字。
    }
}

fun formatDouble(value: Double, decimals: Int = 2): String {
    return if (value % 1 == 0.0) {
        value.toInt().toString()
    } else {
        "%.${decimals}f".format(value)
    }
}

fun formatDegree(value: Float, decimals: Int = 0): String {
    return "${"%.${decimals}f".format(value)}°"
}

fun formatDistance(meters: Int): String {
    return if (meters >= 1000) {
        "%.1fkm".format(meters / 1000f)
    } else {
        "${meters}m"
    }
}