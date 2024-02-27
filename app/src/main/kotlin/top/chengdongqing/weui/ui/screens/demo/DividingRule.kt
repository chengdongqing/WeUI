package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.dividingrule.WeDividingRule
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme

@Composable
fun DividingRuleScreen() {
    WeScreen(
        title = "DividingRule",
        description = "刻度尺滚动选择器",
        padding = PaddingValues(0.dp)
    ) {
        WeDividingRule()
    }
}

@Preview
@Composable
private fun PreviewDividingRule() {
    WeUITheme {
        DividingRuleScreen()
    }
}