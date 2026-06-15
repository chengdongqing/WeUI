package top.chengdongqing.weui.feature.basic.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.WeBadge
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.toBadgeText
import top.chengdongqing.weui.core.ui.theme.WeTheme

@Composable
fun BadgeScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Badge",
        description = "徽章",
        padding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        onBack = onBack
    ) {
        WeBadge {
            WeButton(text = "按钮")
        }
        WeBadge(
            content = "8",
            size = 20.dp
        ) {
            WeButton(text = "按钮")
        }
        WeBadge(
            content = "New",
            size = 20.dp,
            alignment = Alignment.BottomEnd
        ) {
            WeButton(text = "按钮")
        }
        WeBadge(
            alignment = Alignment.TopStart,
            size = 5.dp
        ) {
            WeButton(text = "按钮")
        }
        WeBadge(
            content = "8",
            size = 20.dp,
            containerColor = WeTheme.colorScheme.primary,
            alignment = Alignment.BottomStart
        ) {
            WeButton(text = "按钮")
        }
        WeBadge(
            content = 200.toBadgeText(),
            size = 20.dp,
            alignment = Alignment.CenterEnd
        ) {
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
            Text(
                text = "Android开发工程师",
                color = WeTheme.colorScheme.textPrimary
            )
            WeBadge(
                content = "New",
                size = 20.dp,
                alignment = Alignment.Center
            )
        }
    }
}