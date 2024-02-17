package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.chart.BarChartPage
import top.chengdongqing.weui.ui.views.chart.LineChartPage
import top.chengdongqing.weui.ui.views.chart.PieChartPage

fun NavGraphBuilder.addChartGraph() {
    composable("bar-chart") {
        BarChartPage()
    }
    composable("line-chart") {
        LineChartPage()
    }
    composable("pie-chart") {
        PieChartPage()
    }
}