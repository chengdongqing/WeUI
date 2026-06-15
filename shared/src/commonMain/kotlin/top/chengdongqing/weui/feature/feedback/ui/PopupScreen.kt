package top.chengdongqing.weui.feature.feedback.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WePopup
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.theme.WeTheme

@Composable
fun PopupScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Popup",
        description = "弹出框",
        onBack = onBack
    ) {
        var visible by remember { mutableStateOf(false) }
        var draggable by remember { mutableStateOf(false) }

        WePopup(
            visible = visible,
            title = "标题",
            draggable = draggable,
            onClose = { visible = false }
        ) {
            Text(text = "内容", color = WeTheme.colorScheme.textPrimary)
            Spacer(modifier = Modifier.height(200.dp))
        }

        WeButton(text = "样式一", type = ButtonType.Plain) {
            draggable = false
            visible = true
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "样式二", type = ButtonType.Plain) {
            draggable = true
            visible = true
        }
    }
}