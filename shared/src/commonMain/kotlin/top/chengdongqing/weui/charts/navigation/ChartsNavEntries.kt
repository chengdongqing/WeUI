package top.chengdongqing.weui.charts.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.charts.ui.BarChartScreen
import top.chengdongqing.weui.charts.ui.LineChartScreen
import top.chengdongqing.weui.charts.ui.PieChartScreen
import top.chengdongqing.weui.navigation.ChartsNavKey

fun EntryProviderScope<NavKey>.chartsNavEntries(onBack: () -> Unit) {
    entry<ChartsNavKey.Bar> {
        BarChartScreen(onBack)
    }
    entry<ChartsNavKey.Line> {
        LineChartScreen(onBack)
    }
    entry<ChartsNavKey.Pie> {
        PieChartScreen(onBack)
    }
}