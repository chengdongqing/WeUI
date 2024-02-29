package top.chengdongqing.weui.ui.screens.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun ButtonScreen() {
    WeScreen(
        title = "Button",
        description = "按钮",
        padding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeButton(text = "主要操作")
        WeButton(text = "主要操作", loading = true)
        WeButton(text = "次要操作", type = ButtonType.PLAIN)
        WeButton(text = "次要操作", type = ButtonType.PLAIN, loading = true)
        WeButton(text = "警示操作", type = ButtonType.DANGER)
        WeButton(text = "警示操作", type = ButtonType.DANGER, loading = true)
        Spacer(Modifier)
        WeButton(text = "按钮禁用", disabled = true)
        Spacer(Modifier)
        WeButton(text = "medium 按钮", size = ButtonSize.MEDIUM)
        WeButton(text = "medium 按钮", size = ButtonSize.MEDIUM, type = ButtonType.PLAIN)
        WeButton(text = "medium 按钮", size = ButtonSize.MEDIUM, type = ButtonType.DANGER)
        Spacer(Modifier)
        Row {
            WeButton(text = "按钮", size = ButtonSize.SMALL)
            Spacer(Modifier.width(12.dp))
            WeButton(text = "按钮", size = ButtonSize.SMALL, type = ButtonType.PLAIN)
            Spacer(Modifier.width(12.dp))
            WeButton(text = "按钮", size = ButtonSize.SMALL, type = ButtonType.DANGER)
        }
    }
}