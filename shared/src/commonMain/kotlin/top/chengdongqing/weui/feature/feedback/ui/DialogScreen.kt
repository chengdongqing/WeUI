package top.chengdongqing.weui.feature.feedback.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.rememberDialogState

@Composable
fun DialogScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Dialog",
        description = "对话框",
        onBack = onBack
    ) {
        val dialog = rememberDialogState()

        WeButton(
            text = "Dialog 样式一",
            type = ButtonType.Plain
        ) {
            dialog.show(
                title = "弹窗标题",
                content = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "主操作",
                cancelText = "辅助操作"
            )
        }
        Spacer(Modifier.height(16.dp))
        WeButton(
            text = "Dialog 样式二",
            type = ButtonType.Plain
        ) {
            dialog.show(
                title = "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                okText = "知道了",
                onCancel = null
            )
        }
    }
}