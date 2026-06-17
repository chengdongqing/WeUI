package top.chengdongqing.weui.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow
import kotlin.time.Instant

/**
 * 格式化数字
 *
 * @param decimals 保留的小数位数
 * @param fillZero 是否强制补零
 */
fun Float.format(
    decimals: Int = 2,
    fillZero: Boolean = false
): String {
    return if (!fillZero && rem(1) == 0f) {
        toInt().toString()
    } else {
        val factor = 10.0.pow(decimals.toDouble()).toFloat()
        val rounded = (this * factor) / factor

        // 补零
        rounded.toString().let { s ->
            if (decimals == 0) s.substringBefore(".")
            else s.split(".").let { it[0] + "." + it[1].padEnd(decimals, '0').take(decimals) }
        }
    }
}

/**
 * 格式化文件大小
 */
fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${(size / 1024f).format()} KB"
        size < 1024 * 1024 * 1024 -> "${(size / (1024 * 1024f)).format()} MB"
        else -> "${(size / (1024 * 1024 * 1024f)).format()} GB"
    }
}

/**
 * 格式化时间
 *
 * @param milliseconds 毫秒数
 * @param pattern 格式
 */
fun formatTime(
    milliseconds: Long,
    pattern: DateTimeFormat<LocalDateTime> = DefaultDateTimeFormatter
): String =
    Instant.fromEpochMilliseconds(milliseconds)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .format(pattern)

fun Boolean.format(trueLabel: String = "是", falseLabel: String = "否") =
    if (this) trueLabel else falseLabel