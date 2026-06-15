package top.chengdongqing.weui.feature.charts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.feature.charts.components.DefaultChartLegend
import top.chengdongqing.weui.feature.charts.components.WePieChart
import top.chengdongqing.weui.feature.charts.model.ChartData
import top.chengdongqing.weui.util.format
import top.chengdongqing.weui.util.randomInt
import top.chengdongqing.weui.util.rememberToggleState

@Composable
fun PieChartScreen(onBack: () -> Unit) {
    val dataSource = rememberSaveable { mutableStateOf(buildData()) }
    val (ringWidth, toggleRingWidth) = rememberToggleState(
        defaultValue = 0.dp,
        reverseValue = 40.dp
    )
    val (showLegend, toggleLegend) = rememberToggleState(
        defaultValue = false,
        reverseValue = true
    )

    WeScreen(
        title = "PieChart",
        description = "饼图",
        containerColor = WeTheme.colorScheme.surface,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        onBack = onBack
    ) {
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
                    DefaultChartLegend(
                        items,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数据") {
            dataSource.value = buildData()
        }
        WeButton(text = "切换类型", type = ButtonType.Plain) {
            toggleRingWidth()
        }
        WeButton(
            text = "${if (showLegend.value) "隐藏" else "显示"}图例",
            type = ButtonType.Plain
        ) {
            toggleLegend()
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

private fun buildData(): List<ChartData> {
    val allFruits = listOf("苹果", "香蕉", "樱桃", "西瓜", "草莓")
    return allFruits.map {
        ChartData(
            randomInt(1, 100).toFloat(),
            it
        )
    }
}