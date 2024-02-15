package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import top.chengdongqing.weui.ui.components.checkbox.CheckboxOption
import top.chengdongqing.weui.ui.components.checkbox.WeCheckboxGroup
import top.chengdongqing.weui.ui.components.page.WePage

@Composable
fun CheckboxPage() {
    WePage(title = "Checkbox", description = "复选框", backgroundColor = Color.White) {
        var values by remember { mutableStateOf<List<Int>>(emptyList()) }

        WeCheckboxGroup(
            listOf(
                CheckboxOption(label = "standard is dealt for u.", value = 1),
                CheckboxOption(label = "standard is dealicient for u.", value = 2),
                CheckboxOption(label = "standard is dealicient for u.", value = 3, disabled = true)
            ),
            values
        ) {
            values = it
        }
    }
}