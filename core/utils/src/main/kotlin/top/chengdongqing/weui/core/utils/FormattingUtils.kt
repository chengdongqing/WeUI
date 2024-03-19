package top.chengdongqing.weui.core.utils

fun Float.format(decimals: Int = 2): String {
    return if (rem(1) == 0f) {
        toInt().toString()
    } else {
        "%.${decimals}f".format(this)
        // %.2f: 格式说明符，用于格式化浮点数，f 表示浮点数，.2 表示小数点后保留两位数字
    }
}

fun Double.format(decimals: Int = 2): String {
    return if (rem(1) == 0.0) {
        toInt().toString()
    } else {
        "%.${decimals}f".format(this)
    }
}

/**
 * 格式化度数
 *
 * @param degrees 度数
 */
fun formatDegree(degrees: Float, decimals: Int = 1): String {
    return "${"%.${decimals}f".format(degrees)}°"
}

/**
 * 格式化距离
 *
 * @param meters 米数
 */
fun formatDistance(meters: Int, decimals: Int = 1): String {
    return if (meters >= 1000) {
        "%.${decimals}fkm".format(meters / 1000f)
    } else {
        "${meters}m"
    }
}