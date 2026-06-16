package top.chengdongqing.weui.util

interface LunarDate {
    /**
     * 获取节日
     */
    val festivals: List<String>

    /**
     * 获取月份
     */
    val monthInChinese: String

    /**
     * 获取农历日
     */
    val dayInChinese: String

    /**
     * 获取数字日
     */
    val day: Int
}

expect fun getLunar(year: Int, month: Int, day: Int): LunarDate

fun LunarDate.toDisplayString(): String {
    return when {
        festivals.isNotEmpty() -> festivals.first()
        day == 1 -> monthInChinese + "月"
        else -> dayInChinese
    }
}