package top.chengdongqing.weui.feature.form.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.chengdongqing.weui.core.ui.components.WeRadioGroup
import top.chengdongqing.weui.core.ui.components.WeScreen

@Composable
fun RadioScreen(onBack: () -> Unit) {
    var value by remember { mutableStateOf<String?>(null) }

    WeScreen(
        title = "Radio",
        description = "单选框",
        onBack = onBack
    ) {
        WeRadioGroup(options, value) {
            value = it
        }
    }
}

private val options = listOf(
    Pair("男", "man"),
    Pair("女", "woman"),
    Pair("未知", "unknow")
)