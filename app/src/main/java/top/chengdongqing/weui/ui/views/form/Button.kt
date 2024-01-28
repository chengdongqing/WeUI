package top.chengdongqing.weui.ui.views.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun ButtonPage() {
    WePage(title = "Button", description = "按钮") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeButton(text = "主要操作")
            Spacer(Modifier.height(16.dp))
            WeButton(text = "主要操作", loading = true)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "次要操作", type = ButtonType.PLAIN)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "次要操作", type = ButtonType.PLAIN, loading = true)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "警示操作", type = ButtonType.DANGER)
            Spacer(Modifier.height(16.dp))
            WeButton(text = "警示操作", type = ButtonType.DANGER, loading = true)
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
            Spacer(Modifier.height(100.dp))
        }
    }
}