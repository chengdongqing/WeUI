package top.chengdongqing.weui.utils

import kotlin.time.Duration

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

fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
        else -> String.format("00:%02d", seconds)
    }
}