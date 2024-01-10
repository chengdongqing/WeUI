package top.chengdongqing.weui.ui.views.device

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.Page

@Composable
fun SystemStatusPage() {
    Page(title = "SystemStatus", description = "系统状态") {
        Column {
            Text(text = "网络状态")
            Text(text = "系统主题")
            Text(text = "截屏事件")
        }
    }
}