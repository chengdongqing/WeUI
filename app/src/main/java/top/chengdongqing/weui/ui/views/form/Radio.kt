package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.RadioOption
import top.chengdongqing.weui.ui.components.form.WeRadioGroup

@Composable
fun RadioPage() {
    WePage(title = "Radio", description = "单选框", backgroundColor = Color.White) {
        val value = remember {
            mutableStateOf<Any>("2")
        }

        WeRadioGroup(
            options = listOf(
                RadioOption(label = "cell standard", value = "1"),
                RadioOption(label = "cell standard", value = "2"),
                RadioOption(label = "cell standard", value = "3", disabled = true),
            ),
            value = value.value
        ) {
            value.value = it
        }
    }
}