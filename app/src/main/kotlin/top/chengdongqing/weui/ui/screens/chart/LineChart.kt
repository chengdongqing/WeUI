package top.chengdongqing.weui.ui.screens.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.charts.ChartData
import top.chengdongqing.weui.ui.components.charts.LineChartData
import top.chengdongqing.weui.ui.components.charts.WeLineChart
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.ui.theme.WarningColor
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.formatFloat
import top.chengdongqing.weui.utils.randomFloat

@Composable
fun LineChartScreen() {
    WeScreen(
        title = "LineChart",
        description = "折线图",
        containerColor = MaterialTheme.colorScheme.surface,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var dataSource by remember {
            mutableStateOf(
                listOf(
                    LineChartData(
                        buildData(6),
                        PrimaryColor.copy(0.8f)
                    )
                )
            )
        }

        WeLineChart(
            dataSources = dataSource
        ) {
            "¥" + formatFloat(it)
        }
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数据") {
            dataSource = buildList {
                add(
                    LineChartData(
                        buildData(),
                        PrimaryColor.copy(0.8f)
                    )
                )
                if (dataSource.size == 2) {
                    add(
                        LineChartData(
                            buildData(),
                            WarningColor.copy(0.8f)
                        )
                    )
                }
            }
        }
        WeButton(text = "切换数量", type = ButtonType.PLAIN) {
            dataSource = buildList {
                add(
                    LineChartData(
                        buildData(),
                        PrimaryColor.copy(0.8f)
                    )
                )
                if (dataSource.size == 1) {
                    add(
                        LineChartData(
                            buildData(),
                            WarningColor.copy(0.8f)
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

@Preview
@Composable
private fun PreviewLineChart() {
    WeUITheme {
        LineChartScreen()
    }
}