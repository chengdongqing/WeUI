package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.chart.BarChartScreen
import top.chengdongqing.weui.ui.screens.chart.LineChartScreen
import top.chengdongqing.weui.ui.screens.chart.PieChartScreen

fun NavGraphBuilder.addChartGraph() {
    composable("bar_chart") {
        BarChartScreen()
    }
    composable("bar_chart") {
        LineChartScreen()
    }
    composable("pie_chart") {
        PieChartScreen()
    }
}