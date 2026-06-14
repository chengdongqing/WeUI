package top.chengdongqing.weui.util

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.format(decimals: Int = 2): String {
    return if (rem(1) == 0f) {
        toInt().toString()
    } else {
        val multiplier = 10.0.pow(decimals.toDouble()).toFloat()
        val rounded = (this * multiplier).roundToInt() / multiplier

        rounded.toString()
    }
}