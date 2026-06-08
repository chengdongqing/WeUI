package top.chengdongqing.weui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.components.ButtonType
import top.chengdongqing.weui.components.DefaultChartLegend
import top.chengdongqing.weui.components.WeButton
import top.chengdongqing.weui.components.WePieChart
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.data.model.ChartData
import top.chengdongqing.weui.utils.format
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
        val dataSource = rememberSaveable { mutableStateOf(buildData()) }
        val (ringWidth, toggleRingWidth) = rememberToggleState(
            defaultValue = 0.dp,
            reverseValue = 40.dp
        )
        val (showLegend, toggleLegend) = rememberToggleState(
            defaultValue = false,
            reverseValue = true
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WePieChart(
                dataSource.value,
                modifier = Modifier.fillMaxWidth(0.75f),
                ringWidth = ringWidth.value,
                formatter = {
                    it.format() + "个"
                }
            ) { items ->
                if (showLegend.value) {
                    DefaultChartLegend(items, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数据") {
            dataSource.value = buildData()
        }
        WeButton(text = "切换类型", type = ButtonType.PLAIN) {
            toggleRingWidth()
        }
        WeButton(
            text = "${if (showLegend.value) "隐藏" else "显示"}图例",
            type = ButtonType.PLAIN
        ) {
            toggleLegend()
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

private fun buildData(): List<ChartData> {
    val allFruits = listOf("苹果", "香蕉", "樱桃", "西瓜", "草莓")
    return allFruits.map { ChartData(randomInt(1, 100).toFloat(), it) }
}
