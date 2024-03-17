package top.chengdongqing.weui.ui.screens.hardware

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.vibrateLong
import top.chengdongqing.weui.utils.vibrateShort

@Composable
fun VibrationScreen() {
    WeScreen(title = "Vibration", description = "震动") {
        val context = LocalContext.current

        WeButton(text = "短震动") {
            context.vibrateShort()
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "长震动", type = ButtonType.PLAIN) {
            context.vibrateLong()
        }
    }
}