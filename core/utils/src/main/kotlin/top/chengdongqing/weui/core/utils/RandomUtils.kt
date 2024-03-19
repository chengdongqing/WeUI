package top.chengdongqing.weui.core.utils

import kotlin.random.Random

fun randomFloat(min: Float, max: Float): Float {
    require(min < max) { "最小值必须小于最大值" }
    return Random.nextFloat() * (max - min) + min
}

fun randomInt(min: Int, max: Int): Int {
    require(min < max) { "最小值必须小于最大值" }
    return Random.nextInt(min, max + 1)
}