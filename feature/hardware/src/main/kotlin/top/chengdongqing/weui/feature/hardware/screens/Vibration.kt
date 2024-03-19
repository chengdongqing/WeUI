package top.chengdongqing.weui.feature.hardware.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.vibrateLong
import top.chengdongqing.weui.core.utils.vibrateShort

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