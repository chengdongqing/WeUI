package top.chengdongqing.weui.utils

import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration

/**
 * 没有小数则显示为整数
 * 有小数最多保留2位小数
 */
fun formatFloat(value: Float): String {
    return if (value % 1 == 0f) {
        value.toInt().toString()
    } else {
        String.format(Locale.CHINESE, "%.2f", value)
    }
}

fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> String.format(Locale.CHINESE, "%02d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format(Locale.CHINESE, "%02d:%02d", minutes, seconds)
        else -> String.format(Locale.CHINESE, "00:%02d", seconds)
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

fun bytesToHex(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    for (i in bytes.indices) {
        val v = bytes[i].toInt() and 0xFF
        hexChars[i * 2] = "0123456789ABCDEF"[v shr 4]
        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }
    return String(hexChars)
}

fun hexToBytes(hex: String): ByteArray {
    val result = ByteArray(hex.length / 2)
    for (i in hex.indices step 2) {
        val byteValue = hex.substring(i, i + 2).toInt(16)
        result[i / 2] = byteValue.toByte()
    }
    return result
}
