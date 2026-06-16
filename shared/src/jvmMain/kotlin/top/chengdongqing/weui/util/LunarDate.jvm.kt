package top.chengdongqing.weui.util

import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar

class JvmLunar(
    year: Int,
    month: Int,
    day: Int
) : LunarDate {
    private val lunar: Lunar = Lunar(Solar(year, month, day))

    override val festivals: List<String>
        get() = lunar.festivals
    override val monthInChinese: String
        get() = lunar.monthInChinese
    override val dayInChinese: String
        get() = lunar.dayInChinese
    override val day: Int
        get() = lunar.day
}

actual fun getLunar(
    year: Int,
    month: Int,
    day: Int
): LunarDate = JvmLunar(year, month, day)