package top.chengdongqing.weui.ui.views.basic

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.WeLoading

@Composable
fun LoadingPage() {
    Page(title = "Loading", description = "正在加载") {
        WeLoading()
    }
}