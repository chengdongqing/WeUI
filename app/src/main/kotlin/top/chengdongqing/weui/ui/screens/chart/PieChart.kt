package top.chengdongqing.weui.ui.screens.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.charts.ChartData
import top.chengdongqing.weui.ui.components.charts.WePieChart
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.formatFloat
import top.chengdongqing.weui.utils.randomInt
import top.chengdongqing.weui.utils.rememberToggleState

@Composable
fun PieChartScreen() {
    WeScreen(
        title = "PieChart",
        description = "饼图",
        containerColor = MaterialTheme.colorScheme.surface,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var dataSource by remember { mutableStateOf(buildData()) }
        val (ringWidth, toggleRingWidth) = rememberToggleState(
            defaultValue = 0.dp,
            reverseValue = 30.dp
        )

        WePieChart(dataSource, ringWidth.value) {
            formatFloat(it) + "个"
        }
        WeButton(text = "更新数据") {
            dataSource = buildData()
        }
        WeButton(text = "切换类型", type = ButtonType.PLAIN) {
            toggleRingWidth()
        }
    }
}

private fun buildData(): List<ChartData> {
    val allFruits = listOf("苹果", "香蕉", "樱桃", "西瓜", "草莓")
    return allFruits.map { ChartData(randomInt(1, 100).toFloat(), it) }
}

@Preview
@Composable
private fun PreviewPieChart() {
    WeUITheme {
        PieChartScreen()
    }
}