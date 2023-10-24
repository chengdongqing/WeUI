package top.chengdongqing.weui.ui.views.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeCheckbox

@Composable
fun CheckboxPage() {
    Page(title = "Checkbox", description = "复选框", bgColor = Color.White) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            WeCheckbox(label = "standard is dealt for u.", checked = true) {
            }
            WeCheckbox(label = "standard is dealicient for u.") {
            }
            WeCheckbox(label = "standard is dealicient for u.", checked = true, disabled = true) {
            }
        }
    }
}