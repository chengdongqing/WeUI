package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.chengdongqing.weui.ui.components.WeDialogHolder

@Composable
fun DialogPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            WeDialogHolder(
                title = "弹窗标题",
                content = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "主操作",
                cancelText = "辅助操作",
                onOk = {
                    it.value = false
                },
                onCancel = {
                    it.value = false
                }) {
                Button(onClick = { it.value = true }) {
                    Text(text = "Dialog 样式一")
                }
            }

            WeDialogHolder(
                title = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "知道了",
                onOk = {
                    it.value = false
                }) {
                Button(onClick = { it.value = true }) {
                    Text(text = "Dialog 样式二")
                }
            }
        }
    }
}