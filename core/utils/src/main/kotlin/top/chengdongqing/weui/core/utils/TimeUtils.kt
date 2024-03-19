package top.chengdongqing.weui.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration

const val DefaultDateFormatter = "yyyy-MM-dd"
const val DefaultTimeFormatter = "HH:mm:ss"
const val DefaultDateTimeFormatter = "$DefaultDateFormatter $DefaultTimeFormatter"
const val ChineseDateFormatter = "yyyy年MM月dd日"
const val ChineseDateWeekFormatter = "$ChineseDateFormatter EEEE"

/**
 * 格式化时间
 *
 * @param milliseconds 毫秒数
 * @param pattern 格式
 */
fun formatTime(milliseconds: Long, pattern: String = DefaultDateTimeFormatter): String {
    return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern(pattern, Locale.CHINA))
}

/**
 * 格式化时长
 *
 * @param isFull 是否格式化为完整时长
 */
fun Duration.format(isFull: Boolean = false): String {
    val hours = inWholeSeconds / HOUR_IN_SECONDS
    val minutes = (inWholeSeconds % HOUR_IN_SECONDS) / MINUTE_IN_SECONDS
    val seconds = inWholeSeconds % MINUTE_IN_SECONDS

    return when {
        hours > 0 || isFull -> "%02d:%02d:%02d".format(hours, minutes, seconds)
        else -> "%02d:%02d".format(minutes, seconds)
        // %02d: 格式说明符，用于格式化整数。d 表示整数，02 表示如果数字少于两位，会在前面补零以达到两位数
    }
}

/**
 * 格式化时长（中文表达方式）
 *
 * @param isFull 是否格式化为完整时长
 */
fun Duration.formatChinese(isFull: Boolean = false): String {
    val hours = inWholeSeconds / HOUR_IN_SECONDS
    val minutes = (inWholeSeconds % HOUR_IN_SECONDS) / MINUTE_IN_SECONDS
    val seconds = inWholeSeconds % MINUTE_IN_SECONDS

    return when {
        hours > 0 || isFull -> {
            "${hours}时${minutes}分${seconds}秒"
        }

        minutes > 0 -> {
            "${minutes}分${seconds}秒"
        }

        else -> "${seconds}秒"
    }
}

private const val HOUR_IN_SECONDS = 3600
private const val MINUTE_IN_SECONDS = 60