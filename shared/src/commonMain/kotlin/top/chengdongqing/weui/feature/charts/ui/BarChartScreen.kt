package top.chengdongqing.weui.feature.charts.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.theme.GreenPrimary
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.core.ui.theme.YellowWarning
import top.chengdongqing.weui.feature.charts.components.WeBarChart
import top.chengdongqing.weui.feature.charts.model.ChartData
import top.chengdongqing.weui.util.format
import top.chengdongqing.weui.util.randomFloat
import top.chengdongqing.weui.util.rememberToggleState

@Composable
fun BarChartScreen(onBack: () -> Unit) {
    var dataSource by rememberSaveable { mutableStateOf(buildData()) }
    val (color, toggleColor) = rememberToggleState(
        defaultValue = GreenPrimary.copy(0.8f),
        reverseValue = YellowWarning.copy(0.8f)
    )
    val (maxBarWidth, toggleMaxBarWidth) = rememberToggleState(
        defaultValue = 20,
        reverseValue = 30
    )
    var scrollable by remember { mutableStateOf(false) }
    val containerDpSize = LocalWindowInfo.current.containerDpSize

    WeScreen(
        title = "BarChart",
        description = "柱状图",
        containerColor = WeTheme.colorScheme.surface,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        onBack = onBack
    ) {
        Box(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState())
            } else {
                Modifier
            }
        ) {
            WeBarChart(
                dataSource,
                color = color.value,
                barWidthRange = 2..maxBarWidth.value,
                modifier = if (scrollable) {
                    Modifier.width((containerDpSize.width.value * 3).dp)
                } else {
                    Modifier
                }
            ) {
                "¥" + it.format()
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数据") {
            dataSource = buildData(if (scrollable) 24 else 6)
        }
        WeButton(text = "切换颜色", type = ButtonType.Danger) {
            toggleColor()
        }
        WeButton(text = "切换横向滚动", type = ButtonType.Plain) {
            dataSource = buildData(if (scrollable) 6 else 24)
            scrollable = !scrollable
        }
        WeButton(text = "切换最大柱宽", type = ButtonType.Plain) {
            toggleMaxBarWidth()
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun buildData(size: Int = 6): List<ChartData> {
    return MutableList(size) { index ->
        val value = randomFloat(0f, 10000f)
        ChartData(
            value,
            "${index + 1}月"
        )
    }
}