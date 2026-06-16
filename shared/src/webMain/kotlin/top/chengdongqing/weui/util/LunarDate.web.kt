package top.chengdongqing.weui.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsArray
import kotlin.js.JsModule
import kotlin.js.JsString
import kotlin.js.toList

@OptIn(ExperimentalWasmJsInterop::class)
@JsModule("lunar-javascript")
external object LunarModule {
    class Solar {
        companion object {
            fun fromYmd(y: Int, m: Int, d: Int): Solar
        }

        fun getLunar(): Lunar
    }

    class Lunar {
        fun getFestivals(): JsArray<JsString>
        fun getMonthInChinese(): String
        fun getDayInChinese(): String
        fun getDay(): Int
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
class JsLunar(year: Int, month: Int, day: Int) : LunarDate {
    private val lunar = LunarModule.Solar.fromYmd(year, month, day).getLunar()

    override val festivals = lunar.getFestivals().toList().map { it.toString() }
    override val monthInChinese = lunar.getMonthInChinese()
    override val dayInChinese = lunar.getDayInChinese()
    override val day = lunar.getDay()
}

actual fun getLunar(
    year: Int,
    month: Int,
    day: Int
): LunarDate = JsLunar(year, month, day)