package top.chengdongqing.weui.feature.feedback.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.popup.WePopup
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun PopupScreen() {
    WeScreen(title = "Popup", description = "弹出框") {
        var visible by remember { mutableStateOf(false) }
        var draggable by remember { mutableStateOf(false) }

        WePopup(
            visible,
            title = "标题",
            draggable = draggable,
            onClose = { visible = false }
        ) {
            Text(text = "内容", color = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.height(200.dp))
        }

        WeButton(text = "样式一", type = ButtonType.PLAIN) {
            draggable = false
            visible = true
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "样式二", type = ButtonType.PLAIN) {
            draggable = true
            visible = true
        }
    }
}