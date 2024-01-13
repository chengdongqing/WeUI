package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WePicker

@Composable
fun PickerPage() {
    Page(title = "Picker", description = "选择框") {
        WePicker()
    }
}