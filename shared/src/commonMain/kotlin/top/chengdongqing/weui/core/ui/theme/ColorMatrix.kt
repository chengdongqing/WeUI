package top.chengdongqing.weui.core.ui.theme

import androidx.compose.ui.graphics.ColorMatrix

/**
 * 反色矩阵
 */
val InvertColorMatrix by lazy {
    ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f, // red
            0f, -1f, 0f, 0f, 255f, // green
            0f, 0f, -1f, 0f, 255f, // blue
            0f, 0f, 0f, 1f, 0f    // alpha
        )
    )
}