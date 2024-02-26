package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.digitalkeyboard.WeDigitalKeyboard
import top.chengdongqing.weui.ui.components.input.WeInput
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun DigitalKeyboardScreen() {
    WeScreen(title = "DigitalKeyboard", description = "数字键盘") {
        var text by remember { mutableStateOf("") }

        Column {
            WeInput(
                value = text,
                disabled = true,
                label = "金额",
                placeholder = "请输入",
            )
            Spacer(modifier = Modifier.height(30.dp))

            WeDigitalKeyboard(
                isEmpty = text.isEmpty(),
                onBack = {
                    if (text.isNotEmpty()) {
                        text = text.dropLast(1)
                    }
                },
                onConfirm = {}
            ) { key ->
                text += key
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDigitalKeyboard() {
    DigitalKeyboardScreen()
}