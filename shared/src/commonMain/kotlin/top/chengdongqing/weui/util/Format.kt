package top.chengdongqing.weui.util

import kotlin.math.pow

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