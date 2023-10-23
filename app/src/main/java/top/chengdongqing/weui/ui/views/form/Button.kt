package top.chengdongqing.weui.ui.views.form

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.ButtonSize
import top.chengdongqing.weui.ui.components.ButtonType
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.WeButton

@Composable
fun ButtonPage() {
    Page(title = "Button", description = "按钮") {
        Column {
            WeButton(text = "主要操作")
            Spacer(Modifier.height(16.dp))
            WeButton(text = "次要操作", type = ButtonType.PLAIN)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "警示操作", type = ButtonType.DANGER)
            Spacer(Modifier.height(32.dp))

            WeButton(text = "按钮禁用", disabled = true)
            Spacer(Modifier.height(32.dp))

            WeButton(text = "medium 按钮", size = ButtonSize.MEDIUM)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "medium 按钮", size = ButtonSize.MEDIUM, type = ButtonType.PLAIN)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "medium 按钮", size = ButtonSize.MEDIUM, type = ButtonType.DANGER)
            Spacer(Modifier.height(32.dp))
            Row {
                WeButton(text = "按钮", size = ButtonSize.SMALL)
                Spacer(Modifier.width(12.dp))
                WeButton(text = "按钮", size = ButtonSize.SMALL, type = ButtonType.PLAIN)
                Spacer(Modifier.width(12.dp))
                WeButton(text = "按钮", size = ButtonSize.SMALL, type = ButtonType.DANGER)
            }
        }
    }
}