package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.CheckboxOption
import top.chengdongqing.weui.ui.components.form.WeCheckboxGroup

@Composable
fun CheckboxPage() {
    WePage(title = "Checkbox", description = "复选框", backgroundColor = Color.White) {
        val values = remember {
            mutableStateListOf<Any>(1)
        }

        WeCheckboxGroup(
            listOf(
                CheckboxOption(label = "standard is dealt for u.", value = 1),
                CheckboxOption(label = "standard is dealicient for u.", value = 2),
                CheckboxOption(label = "standard is dealicient for u.", value = 3, disabled = true)
            ), values = values
        ) {
            values.clear()
            values.addAll(it)
        }
    }
}