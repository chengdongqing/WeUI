package top.chengdongqing.weui.feature.form.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonSize
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen

@Composable
fun ButtonScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Button",
        description = "按钮",
        padding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        onBack = onBack
    ) {
        WeButton(text = "主要操作")
        WeButton(text = "主要操作", loading = true)
        WeButton(text = "次要操作", type = ButtonType.Plain)
        WeButton(text = "次要操作", type = ButtonType.Plain, loading = true)
        WeButton(text = "警示操作", type = ButtonType.Danger)
        WeButton(text = "警示操作", type = ButtonType.Danger, loading = true)
        WeButton(text = "按钮禁用", enabled = false)
        WeButton(text = "medium 按钮", size = ButtonSize.Medium)
        WeButton(text = "medium 按钮", size = ButtonSize.Medium, type = ButtonType.Plain)
        WeButton(text = "medium 按钮", size = ButtonSize.Medium, type = ButtonType.Danger)
        Row {
            WeButton(text = "按钮", size = ButtonSize.Small)
            Spacer(modifier = Modifier.width(12.dp))
            WeButton(text = "按钮", size = ButtonSize.Small, type = ButtonType.Plain)
            Spacer(modifier = Modifier.width(12.dp))
            WeButton(text = "按钮", size = ButtonSize.Small, type = ButtonType.Danger)
        }
    }
}