package top.chengdongqing.weui.utils

import top.chengdongqing.weui.constant.DefaultDateTimeFormatter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDuration(totalSeconds: Long, fullDuration: Boolean = false): String {
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

fun formatSeconds(seconds: Int): String {
    if (seconds < 0) {
        return "--"
    }

    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return when {
        hours > 0 -> {
            if (minutes == 0 && remainingSeconds == 0) {
                "${hours}时"
            } else if (minutes > 0 && remainingSeconds == 0) {
                "${hours}时${minutes}分"
            } else {
                "${hours}时${minutes}分${remainingSeconds}秒"
            }
        }

        minutes > 0 -> {
            if (remainingSeconds > 0) {
                "${minutes}分${remainingSeconds}秒"
            } else {
                "${minutes}分"
            }
        }

        else -> {
            "${remainingSeconds}秒"
        }
    }
}