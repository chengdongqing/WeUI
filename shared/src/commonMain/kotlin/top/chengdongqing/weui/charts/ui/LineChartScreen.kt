package top.chengdongqing.weui.charts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.charts.components.WeLineChart
import top.chengdongqing.weui.charts.model.ChartData
import top.chengdongqing.weui.charts.model.LineChartData
import top.chengdongqing.weui.components.ButtonType
import top.chengdongqing.weui.components.WeButton
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.theme.GreenPrimary
import top.chengdongqing.weui.theme.WeTheme
import top.chengdongqing.weui.theme.YellowWarning
import top.chengdongqing.weui.util.format
import top.chengdongqing.weui.util.randomFloat

@Composable
fun LineChartScreen(onBack: () -> Unit) {
    WeScreen(
        title = "LineChart",
        description = "折线图",
        containerColor = WeTheme.colorScheme.surface,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        onBack = onBack
    ) {
        var dataSource by rememberSaveable {
            mutableStateOf(
                listOf(
                    LineChartData(
                        buildData(6),
                        GreenPrimary.copy(0.8f)
                    )
                )
            )
        }

        WeLineChart(
            dataSources = dataSource
        ) {
            "¥" + it.format()
        }
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数据") {
            dataSource = buildList {
                add(
                    LineChartData(
                        buildData(),
                        GreenPrimary.copy(0.8f)
                    )
                )
                if (dataSource.size == 2) {
                    add(
                        LineChartData(
                            buildData(),
                            YellowWarning.copy(0.8f)
                        )
                    )
                }
            }
        }
        WeButton(text = "切换数量", type = ButtonType.Plain) {
            dataSource = buildList {
                add(
                    LineChartData(
                        buildData(),
                        GreenPrimary.copy(0.8f)
                    )
                )
                if (dataSource.size == 1) {
                    add(
                        LineChartData(
                            buildData(),
                            YellowWarning.copy(0.8f)
                        )
                    )
                }
            }
        }
    }
}

private fun buildData(size: Int = 6): List<ChartData> {
    return MutableList(size) { index ->
        val value = randomFloat(0f, 10000f)
        ChartData(value, "${index + 1}月")
    }
}