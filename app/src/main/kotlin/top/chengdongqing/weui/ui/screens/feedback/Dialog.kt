package top.chengdongqing.weui.ui.screens.feedback

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.dialog.rememberDialogState
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun DialogScreen() {
    WeScreen(title = "Dialog", description = "对话框") {
        val dialog = rememberDialogState()

        WeButton(text = "Dialog 样式一", type = ButtonType.PLAIN) {
            dialog.show(
                title = "弹窗标题",
                content = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "主操作",
                cancelText = "辅助操作"
            )
        }
        Spacer(Modifier.height(16.dp))
        WeButton(text = "Dialog 样式二", type = ButtonType.PLAIN) {
            dialog.show(
                title = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "知道了",
                onCancel = null
            )
        }
    }
}