package top.chengdongqing.weui.util

import cocoapods.LunarSwift.Solar
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
private class IosLunarDate(
    year: Int,
    month: Int,
    day: Int
) : LunarDate {
    private val lunar = Solar.fromYmdHmsWithYear(
        year = year.toLong(),
        month = month.toLong(),
        day = day.toLong(),
        hour = 0,
        minute = 0,
        second = 0
    ).lunar

    override val festivals: List<String>
        get() = lunar.festivals.map { it.toString() }

    override val monthInChinese: String
        get() = lunar.monthInChinese

    override val dayInChinese: String
        get() = lunar.dayInChinese

    override val day: Int
        get() = lunar.day.toInt()
}

actual fun getLunar(
    year: Int,
    month: Int,
    day: Int
): LunarDate = IosLunarDate(year, month, day)
