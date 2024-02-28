package top.chengdongqing.weui.ui.screens.qrcode.generator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.input.WeTextarea
import top.chengdongqing.weui.ui.components.qrcode.generator.WeQrCodeGenerator
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import kotlin.math.roundToInt

@Composable
fun QrCodeGenerateScreen() {
    var content by remember { mutableStateOf("https://weui.io") }
    var finalContent by remember { mutableStateOf("") }
    val size = rememberScreenWidth()
    val toast = rememberWeToast()

    WeScreen(title = "QrCodeGenerator", description = "二维码生成") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeTextarea(value = content, placeholder = "请输入内容", topBorder = true) {
                content = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "生成二维码", type = ButtonType.PLAIN) {
                if (content.isNotEmpty()) {
                    finalContent = content
                } else {
                    toast.show(ToastOptions("请输入内容", ToastIcon.FAIL))
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            if (finalContent.isNotEmpty()) {
                WeQrCodeGenerator(
                    content = finalContent,
                    size = size,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun rememberScreenWidth(fraction: Float = 0.6f): Int {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    return remember {
        with(density) {
            (configuration.screenWidthDp * fraction).dp.toPx().roundToInt()
        }
    }
}