package top.chengdongqing.weui.ui.components.basic

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.theme.BorderColor

@Composable
fun WeDivider(modifier: Modifier = Modifier) {
    Divider(modifier, thickness = 0.5.dp, color = BorderColor)
}