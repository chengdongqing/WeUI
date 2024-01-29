package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import top.chengdongqing.weui.ui.views.chart.BarChartPage
import top.chengdongqing.weui.ui.views.chart.LineChartPage
import top.chengdongqing.weui.ui.views.chart.PieChartPage

fun NavGraphBuilder.addChartGraph() {
    navigation("bar-chart", "chart") {
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
}