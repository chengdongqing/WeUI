package top.chengdongqing.weui.utils

import top.chengdongqing.weui.constants.DefaultDateTimeFormatter
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration

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

fun formatDuration(duration: Duration, fullDuration: Boolean = false): String {
    val totalSeconds = duration.inWholeSeconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 || fullDuration -> "%02d:%02d:%02d".format(hours, minutes, seconds)
        minutes > 0 -> "%02d:%02d".format(minutes, seconds)
        else -> "00:%02d".format(seconds)
        // %02d: 格式说明符，用于格式化整数（d 表示整数）。02 表示如果数字少于两位，会在前面补零以达到两位数
    }
}

fun formatTime(milliseconds: Long, pattern: String = DefaultDateTimeFormatter): String {
    return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern(pattern, Locale.CHINA))
}

fun formatFileSize(file: File): String {
    val size = if (file.exists()) file.length() else 0
    return formatFileSize(size)
}

fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${formatFloat(size / 1024f)} KB"
        size < 1024 * 1024 * 1024 -> "${formatFloat(size / (1024 * 1024f))} MB"
        else -> "${formatFloat(size / (1024 * 1024 * 1024f))} GB"
    }
}

fun Boolean.format(trueLabel: String = "是", falseLabel: String = "否") =
    if (this) trueLabel else falseLabel