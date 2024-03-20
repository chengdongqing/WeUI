package top.chengdongqing.weui.feature.form.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.switch.WeSwitch

@Composable
fun SwitchScreen() {
    WeScreen(title = "Switch", description = "开关") {
        val checked = remember { mutableStateOf(false) }

        WeSwitch(checked = checked.value) {
            checked.value = it
        }
        Spacer(Modifier.height(16.dp))
        WeSwitch(checked = checked.value, disabled = true) {
            checked.value = it
        }
    }
}