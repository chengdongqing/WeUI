package top.chengdongqing.weui.ui.views.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeSwitch

@Composable
fun SwitchPage() {
    WePage(title = "Switch", description = "开关", backgroundColor = Color.White) {
        val checked = remember {
            mutableStateOf(false)
        }

        Column {
            WeSwitch(checked = checked.value) {
                checked.value = it
            }
            Spacer(Modifier.height(16.dp))
            WeSwitch(checked = checked.value, disabled = true) {
                checked.value = it
            }
        }
    }
}