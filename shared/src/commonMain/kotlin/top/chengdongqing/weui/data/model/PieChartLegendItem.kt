package top.chengdongqing.weui.data.model

import androidx.compose.ui.graphics.Color

data class PieChartLegendItem(
    val label: String,
    val value: Float,
    val color: Color,
    val percentage: Float,
    val formattedValue: String
)