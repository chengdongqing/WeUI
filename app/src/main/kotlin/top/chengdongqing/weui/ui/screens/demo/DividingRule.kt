package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.dividingrule.DividingRuleColors
import top.chengdongqing.weui.ui.components.dividingrule.WeDividingRule
import top.chengdongqing.weui.ui.components.dividingrule.dividingRuleColorScheme
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.DangerColorLight
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.formatFloat
import top.chengdongqing.weui.utils.rememberToggleState

@Composable
fun DividingRuleScreen() {
    WeScreen(
        title = "DividingRule",
        description = "刻度尺滚动选择器",
        padding = PaddingValues(0.dp)
    ) {
        var value by remember { mutableFloatStateOf(0f) }
        val (colors, toggleColors) = rememberToggleState(
            defaultValue = MaterialTheme.dividingRuleColorScheme,
            reverseValue = DividingRuleColors(
                containerColor = PrimaryColor.copy(0.5f),
                contentColor = Color.White,
                indicatorColor = DangerColorLight
            )
        )
        val (range, toggleRange) = rememberToggleState(
            defaultValue = 0..100 step 10,
            reverseValue = 150..1500 step 150
        )

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "当前值：${formatFloat(value)}",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeDividingRule(range, colors = colors) {
            value = it
        }
        Spacer(modifier = Modifier.height(60.dp))
        WeButton(text = "切换样式") {
            toggleColors()
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "切换可选值", type = ButtonType.PLAIN) {
            toggleRange()
        }
    }
}

@Preview
@Composable
private fun PreviewDividingRule() {
    WeUITheme {
        DividingRuleScreen()
    }
}