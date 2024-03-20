package top.chengdongqing.weui.feature.form.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.input.WeTextarea
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun InputScreen() {
    WeScreen(title = "Input", description = "输入框") {
        val value = remember { mutableStateMapOf<String, String>() }

        WeInput(
            value = value["account"],
            label = "账号",
            placeholder = "请输入"
        ) {
            value["account"] = it
        }
        WeInput(
            value = value["password"],
            label = "密码",
            placeholder = "请输入",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        ) {
            value["password"] = it
        }
        WeInput("WeUI", label = "微信号", disabled = true)
        WeTextarea(value["desc"] ?: "", placeholder = "请描述你所经历的事情", max = 200) {
            value["desc"] = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(
            text = "确定",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}