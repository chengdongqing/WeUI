package top.chengdongqing.weui.ui.screens.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.switch.WeSwitch

@Composable
fun SwitchScreen() {
    WeScreen(title = "Switch", description = "开关") {
        val checked = remember { mutableStateOf(false) }

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