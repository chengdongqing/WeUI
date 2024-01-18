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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeDatePicker
import top.chengdongqing.weui.ui.components.form.WeInput
import top.chengdongqing.weui.ui.components.form.WeTimePicker
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PickerPage() {
    Page(title = "Picker", description = "滚动选择器") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            var visible by remember {
                mutableStateOf(false)
            }
            var value by remember {
                mutableStateOf(LocalDate.now())
            }
            WeDatePicker(visible, value, onCancel = { visible = false }) { value = it }
            WeInput(
                value = value.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                textAlign = TextAlign.Center,
                disabled = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "选择日期") {
                visible = true
            }

            Spacer(modifier = Modifier.height(40.dp))

            var visible1 by remember {
                mutableStateOf(false)
            }
            var value1 by remember {
                mutableStateOf(LocalTime.now())
            }
            WeTimePicker(visible1, value1, onCancel = { visible1 = false }) { value1 = it }
            WeInput(
                value = value1.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                textAlign = TextAlign.Center,
                disabled = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "选择时间") {
                visible1 = true
            }
        }
    }
}