package top.chengdongqing.weui.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

val Clock.Companion.localDateTime
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
val Clock.Companion.localDate
    get() = localDateTime.date
val Clock.Companion.localTime
    get() = localDateTime.time

val LocalTime.Companion.Min: LocalTime
    get() = LocalTime(0, 0)
val LocalTime.Companion.Max: LocalTime
    get() = LocalTime(23, 59, 59)

@OptIn(FormatStringsInDatetimeFormats::class)
val ChineseDateFormatter = LocalDate.Format {
    byUnicodePattern("yyyy年MM月dd日")
}

@OptIn(FormatStringsInDatetimeFormats::class)
val DefaultTimeFormatter = LocalTime.Format {
    byUnicodePattern("HH:mm:ss")
}

@OptIn(FormatStringsInDatetimeFormats::class)
val DefaultDateTimeFormatter = LocalDateTime.Format {
    byUnicodePattern("yyyy-MM-dd HH:mm:ss")
}