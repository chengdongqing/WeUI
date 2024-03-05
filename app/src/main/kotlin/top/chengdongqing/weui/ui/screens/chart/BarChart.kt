package top.chengdongqing.weui.ui.screens.chart

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.charts.ChartData
import top.chengdongqing.weui.ui.components.charts.WeBarChart
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.ui.theme.WarningColor
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.formatFloat
import top.chengdongqing.weui.utils.randomFloat

@Composable
fun BarChartScreen() {
    WeScreen(
        title = "BarChart",
        description = "柱状图",
        containerColor = MaterialTheme.colorScheme.surface,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var dataSource by remember { mutableStateOf(buildData()) }
        var color by remember { mutableStateOf(PrimaryColor.copy(0.8f)) }
        var maxBarWidth by remember { mutableIntStateOf(20) }
        var scrollable by remember { mutableStateOf(false) }

        Box(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState())
            } else {
                Modifier
            }
        ) {
            WeBarChart(
                dataSource,
                color = color,
                barWidthRange = 2..maxBarWidth,
                modifier = if (scrollable) {
                    Modifier.width((LocalConfiguration.current.screenWidthDp * 3).dp)
                } else {
                    Modifier
                }
            ) {
                "¥" + formatFloat(it)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数据") {
            dataSource = buildData(if (scrollable) 24 else 6)
        }
        WeButton(text = "切换颜色", type = ButtonType.DANGER) {
            color = if (color == PrimaryColor.copy(0.8f)) {
                WarningColor.copy(0.8f)
            } else {
                PrimaryColor.copy(0.8f)
            }
        }
        WeButton(text = "切换横向滚动", type = ButtonType.PLAIN) {
            dataSource = buildData(if (scrollable) 6 else 24)
            scrollable = !scrollable
        }
        WeButton(text = "修改最大柱宽", type = ButtonType.PLAIN) {
            maxBarWidth = if (maxBarWidth == 20) 30 else 20
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
private fun PreviewBarChart() {
    WeUITheme {
        BarChartScreen()
    }
}