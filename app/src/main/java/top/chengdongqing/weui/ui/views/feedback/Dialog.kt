package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.ButtonType
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.WeButton
import top.chengdongqing.weui.ui.components.WeDialogHolder

@Composable
fun DialogPage() {
    Page(title = "Dialog", description = "对话框") {
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
                }
            ) {
                WeButton(text = "Dialog 样式一", type = ButtonType.PLAIN) {
                    it.value = true
                }
            }

            Spacer(Modifier.height(16.dp))

            WeDialogHolder(
                title = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "知道了",
                onOk = {
                    it.value = false
                }
            ) {
                WeButton(text = "Dialog 样式二", type = ButtonType.PLAIN) {
                    it.value = true
                }
            }
        }
    }
}