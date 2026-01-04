package top.chengdongqing.weui.core.utils

import kotlin.math.roundToInt

/**
 * 将接收信号强度 (RSSI) 转换为百分比。
 *
 * **背景知识:**
 * - **dBm (Decibel-milliwatts)**: 表示信号功率的绝对值，计算公式为 10 * log10(功率/1mW)。
 * - **数值特征**: 0 dBm = 1mW。数值越接近 0 信号越强，越负则越弱。
 * - **适用场景**: 蓝牙 (-100 to -40)、Wi-Fi (-90 to -40)、蜂窝网络。
 *
 * @param dBm 原始信号强度数值。
 * @param minDbm 信号阈值底线（0%），低于此值返回 0。蓝牙默认为 -100，Wi-Fi 建议为 -90。
 * @param maxDbm 信号理想上限（100%），高于此值返回 100。通常设为 -40。
 * @return 0 到 100 之间的百分比。
 */
fun dbmToPercentage(
    dBm: Int,
    minDbm: Int = -100,
    maxDbm: Int = -40
): Int {
    return ((dBm - minDbm) * 100 / (maxDbm - minDbm)).coerceIn(0, 100)
}

/**
 * 将 GNSS 载噪比 (C/N0) 转换为百分比值。
 *
 * **背景知识:**
 * - **dBHz (Decibels per Hertz)**: 衡量卫星信号质量的核心指标，指载波功率与噪声功率谱密度的比值。
 * - **区别**: 不同于 dBm，dBHz 反映的是信号在噪声中的“清晰度”。
 * - **典型区间**: 10 dBHz（边缘信号，难以锁定）至 45 dBHz（极佳状态，室外开阔地）。
 * - **定位基准**: 低于 20 dBHz 时，定位精度会显著下降或无法解算。
 *
 * @param dbHz 卫星载波频率的信号质量。
 * @return 0 到 100 之间的百分比。
 */
fun dbHzToPercentage(dbHz: Float): Int {
    val minDbHz = 10f
    val maxDbHz = 45f
    return ((dbHz - minDbHz) * 100 / (maxDbHz - minDbHz)).roundToInt().coerceIn(0, 100)
}
