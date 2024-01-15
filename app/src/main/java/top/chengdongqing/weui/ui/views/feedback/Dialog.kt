package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.rememberWeDialog
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun DialogPage() {
    Page(title = "Dialog", description = "对话框") {
        Column {
            val weDialog = rememberWeDialog(okText = "主操作", "辅助操作")
            WeButton(text = "Dialog 样式一", type = ButtonType.PLAIN) {
                weDialog.open(
                    "弹窗标题",
                    "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内"
                )
            }

            Spacer(Modifier.height(16.dp))

            val weDialog1 = rememberWeDialog(okText = "知道了")
            WeButton(text = "Dialog 样式二", type = ButtonType.PLAIN) {
                weDialog1.open(
                    "弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内",
                    onCancel = null
                )
            }
        }
    }
}