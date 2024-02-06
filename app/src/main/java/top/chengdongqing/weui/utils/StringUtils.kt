package top.chengdongqing.weui.utils

import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

fun formatFloat(value: Float, decimals: Int = 2): String {
    return if (value % 1 == 0f) {
        value.toInt().toString()
    } else {
        "%.${decimals}f".format(value)
    }
}

fun formatDouble(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        "%.2f".format(value)
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
    }
}

fun formatTime(milliseconds: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern(pattern))
}

fun formatFileSize(filePath: String): String {
    val file = File(filePath)
    val size = if (file.exists()) file.length() else 0

    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${formatFloat(size / 1024f)} KB"
        size < 1024 * 1024 * 1024 -> "${formatFloat(size / (1024 * 1024f))} MB"
        else -> "${formatFloat(size / (1024 * 1024 * 1024f))} GB"
    }
}