package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.components.radio.RadioOption
import top.chengdongqing.weui.ui.components.radio.WeRadioGroup

@Composable
fun RadioPage() {
    WePage(title = "Radio", description = "单选框", backgroundColor = Color.White) {
        var value by remember { mutableStateOf<Any>("2") }

        WeRadioGroup(
            listOf(
                RadioOption(label = "cell standard", value = "1"),
                RadioOption(label = "cell standard", value = "2"),
                RadioOption(label = "cell standard", value = "3", disabled = true),
            ),
            value
        ) {
            value = it
        }
    }
}