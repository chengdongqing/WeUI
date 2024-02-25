package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.notificationbar.NotificationBarEffect
import top.chengdongqing.weui.ui.components.notificationbar.WeNotificationBar
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.FontColor1

@Composable
fun NotificationBarScreen() {
    WeScreen(title = "NotificationBar", description = "通知栏", padding = PaddingValues(0.dp)) {
        val content = remember {
            """
                Jetpack Compose 是推荐用于构建原生 Android 界面的新工具包。
                它可简化并加快 Android 上的界面开发，帮助您使用更少的代码、强大的工具和直观的 Kotlin API，快速打造生动而精彩的应用。
            """.trimIndent().replace("\n", "")
        }

        Column {
            Title("滚动效果")
            WeNotificationBar(content)
            Spacer(modifier = Modifier.height(30.dp))
            Title("控制速度")
            WeNotificationBar(
                content,
                scrollStep = 8
            )
            Spacer(modifier = Modifier.height(30.dp))
            Title("单行展示")
            WeNotificationBar(
                content,
                effect = NotificationBarEffect.ELLIPSIS
            )
            Spacer(modifier = Modifier.height(30.dp))
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
    Text(text = title, color = FontColor1, modifier = Modifier.padding(start = 20.dp))
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview
@Composable
private fun PreviewNotificationBar() {
    NotificationBarScreen()
}