package top.chengdongqing.weui.feature.charts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.randomInt
import top.chengdongqing.weui.core.utils.rememberToggleState
import top.chengdongqing.weui.feature.charts.WePieChart
import top.chengdongqing.weui.feature.charts.model.ChartData

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
            it.format() + "个"
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