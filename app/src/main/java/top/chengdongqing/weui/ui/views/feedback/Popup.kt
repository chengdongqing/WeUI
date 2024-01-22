package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.WePopup
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun PopupPage() {
    Page(title = "Popup", description = "弹出框") {
        var visible by remember {
            mutableStateOf(false)
        }
        var dragClosable by remember {
            mutableStateOf(false)
        }

        WePopup(visible, dragClosable = dragClosable, onClose = {
            visible = false
        }, title = "标题") {
            Text(text = "内容")
            Spacer(modifier = Modifier.height(200.dp))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "样式一", type = ButtonType.PLAIN) {
                dragClosable = false
                visible = true
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "样式二", type = ButtonType.PLAIN) {
                dragClosable = true
                visible = true
            }
        }
    }
}