package top.chengdongqing.weui.ui.screens.chart

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
import top.chengdongqing.weui.ui.components.charts.WePieChart
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.formatFloat
import top.chengdongqing.weui.utils.randomInt
import kotlin.random.Random

@Composable
fun PieChartScreen() {
    WeScreen(
        title = "PieChart",
        description = "饼图",
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        var dataSource by remember { mutableStateOf(buildData()) }
        var ringWidth by remember { mutableStateOf(0.dp) }

        WePieChart(dataSource, ringWidth) {
            formatFloat(it) + "个"
        }
        Spacer(modifier = Modifier.height(80.dp))
        WeButton(text = "更新数据") {
            dataSource = buildData()
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "切换类型", type = ButtonType.PLAIN) {
            ringWidth = if (ringWidth == 0.dp) 30.dp else 0.dp
        }
    }
}

private val allFruits = listOf("苹果", "香蕉", "樱桃", "西瓜", "草莓")

private fun buildData(size: Int = 5): List<ChartData> {
    val chartDataList = mutableListOf<ChartData>()
    val usedIndices = mutableSetOf<Int>()

    repeat(size) {
        var randomIndex = Random.nextInt(allFruits.size)
        while (randomIndex in usedIndices) {
            randomIndex = Random.nextInt(allFruits.size)
        }
        usedIndices.add(randomIndex)

        val randomLabel = allFruits[randomIndex]
        val chartData = ChartData(randomInt(1, 100).toFloat(), randomLabel)
        chartDataList.add(chartData)
    }

    return chartDataList
}


@Preview
@Composable
private fun PreviewPieChart() {
    WeUITheme {
        PieChartScreen()
    }
}