package top.chengdongqing.weui.feature.samples.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.samples.components.NotificationBarEffect
import top.chengdongqing.weui.feature.samples.components.WeNotificationBar

@Composable
fun NotificationBarScreen() {
    WeScreen(
        title = "NotificationBar",
        description = "通知栏",
        padding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        val content = remember {
            buildString {
                append("Jetpack Compose 是推荐用于构建原生 Android 界面的新工具包。")
                append("它可简化并加快 Android 上的界面开发，帮助您使用更少的代码、强大的工具和直观的 Kotlin API，快速打造生动而精彩的应用。")
            }
        }

        Column {
            Title("滚动效果")
            WeNotificationBar(content)
        }
        Column {
            Title("控制速度")
            WeNotificationBar(
                content,
                scrollVelocity = 200.dp
            )
        }
        Column {
            Title("首尾间距")
            WeNotificationBar(
                content,
                scrollSpacingFraction = 1f / 2f,
                scrollVelocity = 300.dp
            )
        }
        Column {
            Title("单行展示")
            WeNotificationBar(
                content,
                effect = NotificationBarEffect.ELLIPSIS
            )
        }
        Column {
            Title("多行展示")
            WeNotificationBar(
                content,
                effect = NotificationBarEffect.WRAP
            )
        }
    }
}

@Composable
private fun Title(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier.padding(start = 20.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
}