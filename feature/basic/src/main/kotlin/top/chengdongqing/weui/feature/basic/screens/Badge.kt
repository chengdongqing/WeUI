package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.badge.WeBadge
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun BadgeScreen() {
    WeScreen(
        title = "Badge",
        description = "徽章",
        padding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        WeBadge {
            WeButton(text = "按钮")
        }
        WeBadge("8", size = 20.dp) {
            WeButton(text = "按钮")
        }
        WeBadge("New", size = 20.dp, alignment = Alignment.BottomEnd) {
            WeButton(text = "按钮")
        }
        WeBadge(alignment = Alignment.TopStart, size = 5.dp) {
            WeButton(text = "按钮")
        }
        WeBadge(
            "8",
            size = 20.dp,
            color = MaterialTheme.colorScheme.primary,
            alignment = Alignment.BottomStart
        ) {
            WeButton(text = "按钮")
        }
        WeBadge("New", size = 20.dp, alignment = Alignment.CenterEnd) {
            WeButton(text = "按钮")
        }
        WeBadge(alignment = Alignment.CenterEnd) {
            WeButton(text = "按钮")
        }
        WeBadge(alignment = Alignment.CenterStart) {
            WeButton(text = "按钮")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Android开发工程师", color = MaterialTheme.colorScheme.onPrimary)
            WeBadge("New", 20.dp, alignment = Alignment.Center)
        }
    }
}