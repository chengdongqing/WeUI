package top.chengdongqing.weui.feature.charts.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.charts.screens.BarChartScreen
import top.chengdongqing.weui.feature.charts.screens.LineChartScreen
import top.chengdongqing.weui.feature.charts.screens.PieChartScreen

fun NavGraphBuilder.addChartGraph() {
    composable("bar_chart") {
        BarChartScreen()
    }
    composable("line_chart") {
        LineChartScreen()
    }
    composable("pie_chart") {
        PieChartScreen()
    }
}