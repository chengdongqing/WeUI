package top.chengdongqing.weui.ui.views.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeDatePicker
import top.chengdongqing.weui.ui.components.form.WeInput
import java.time.LocalDate

@Composable
fun PickerPage() {
    Page(title = "Picker", description = "滚动选择器") {
        var visible by remember {
            mutableStateOf(false)
        }
        var value by remember {
            mutableStateOf(LocalDate.now())
        }
        WeDatePicker(
            visible,
            value,
            onCancel = { visible = false }
        ) { value = it }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeInput(value = value.toString(), alignment = Alignment.Center)
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "选择日期") {
                visible = true
            }
        }
    }
}