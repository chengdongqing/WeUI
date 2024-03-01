package top.chengdongqing.weui.utils

import kotlin.random.Random

fun randomFloatInRange(min: Float, max: Float): Float {
    require(min < max) { "最小值必须小于最大值" }
    return Random.nextFloat() * (max - min) + min
}