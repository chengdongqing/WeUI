package top.chengdongqing.weui.feature.samples.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.DividingRuleColors
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeDividingRule
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.dividingRuleColorScheme
import top.chengdongqing.weui.core.ui.theme.GreenPrimary
import top.chengdongqing.weui.core.ui.theme.RedDanger
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.format
import top.chengdongqing.weui.util.rememberToggleState

@Composable
fun DividingRuleScreen(onBack: () -> Unit) {
    var value by remember { mutableFloatStateOf(0f) }
    val (colors, toggleColors) = rememberToggleState(
        defaultValue = WeTheme.dividingRuleColorScheme,
        reverseValue = DividingRuleColors(
            containerColor = GreenPrimary.copy(0.5f),
            contentColor = Color.White,
            indicatorColor = RedDanger
        )
    )
    val (range, toggleRange) = rememberToggleState(
        defaultValue = 0..100 step 10,
        reverseValue = 150..1500 step 150
    )

    WeScreen(
        title = "DividingRule",
        description = "刻度尺滚动选择器",
        padding = PaddingValues(0.dp),
        onBack = onBack
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "当前值：${value.format()}",
                color = WeTheme.colorScheme.textPrimary,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeDividingRule(range.value, colors = colors.value) {
            value = it
        }
        Spacer(modifier = Modifier.height(60.dp))
        WeButton(text = "切换样式") {
            toggleColors()
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "切换可选值", type = ButtonType.Plain) {
            toggleRange()
        }
    }
}