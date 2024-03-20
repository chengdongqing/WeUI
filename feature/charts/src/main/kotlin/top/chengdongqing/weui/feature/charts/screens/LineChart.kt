package top.chengdongqing.weui.feature.charts.screens

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
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.theme.PrimaryColor
import top.chengdongqing.weui.core.ui.theme.WarningColor
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.randomFloat
import top.chengdongqing.weui.feature.charts.WeLineChart
import top.chengdongqing.weui.feature.charts.model.ChartData
import top.chengdongqing.weui.feature.charts.model.LineChartData

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
            "¥" + it.format()
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