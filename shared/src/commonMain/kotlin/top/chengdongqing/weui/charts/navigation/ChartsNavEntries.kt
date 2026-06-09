package top.chengdongqing.weui.charts.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.charts.ui.BarChartScreen
import top.chengdongqing.weui.charts.ui.LineChartScreen
import top.chengdongqing.weui.charts.ui.PieChartScreen
import top.chengdongqing.weui.navigation.ChartsNavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.chartsNavEntries(onBack: () -> Unit) {
    entry<ChartsNavKey.Bar>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        BarChartScreen(onBack)
    }
    entry<ChartsNavKey.Line>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        LineChartScreen(onBack)
    }
    entry<ChartsNavKey.Pie>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        PieChartScreen(onBack)
    }
}