package top.chengdongqing.weui.feature.qrcode.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.input.WeTextarea
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.qrcode.utils.rememberScanCodeLauncher

@Composable
fun QrCodeScanScreen() {
    WeScreen(title = "QrCodeScanner", description = "扫码") {
        var value by remember { mutableStateOf<String?>(null) }
        val scanCode = rememberScanCodeLauncher {
            value = it.joinToString("\n")
        }

        value?.let {
            WeTextarea(it, label = "扫码结果")
            Spacer(modifier = Modifier.height(40.dp))
        }
        WeButton(text = "扫一扫") {
            scanCode()
        }
    }
}