package top.chengdongqing.weui.feature.samples.screens

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
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.theme.DangerColorLight
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.core.utils.rememberToggleState
import top.chengdongqing.weui.feature.samples.components.digitalkeyboard.DigitalKeyboardConfirmOptions
import top.chengdongqing.weui.feature.samples.components.digitalkeyboard.WeDigitalKeyboard

@Composable
fun DigitalKeyboardScreen() {
    WeScreen(title = "DigitalKeyboard", description = "数字键盘") {
        var value by remember { mutableStateOf("") }
        var visible by remember { mutableStateOf(true) }
        var allowDecimal by remember { mutableStateOf(true) }
        val (confirmButtonOptions, toggleConfirmButtonOptions) = rememberToggleState(
            defaultValue = DigitalKeyboardConfirmOptions(),
            reverseValue = DigitalKeyboardConfirmOptions(
                color = DangerColorLight,
                text = "转账"
            )
        )

        WeInput(
            value = value,
            label = "金额",
            placeholder = "请输入",
            disabled = true,
            modifier = Modifier.clickableWithoutRipple {
                visible = true
            }
        )

        if (visible) {
            Spacer(modifier = Modifier.height(40.dp))
            WeButton(
                text = "${if (allowDecimal) "不" else ""}允许小数点",
                type = ButtonType.PLAIN
            ) {
                value = ""
                allowDecimal = !allowDecimal
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "切换样式") {
                toggleConfirmButtonOptions()
            }
        }

        WeDigitalKeyboard(
            visible,
            value = value,
            allowDecimal = allowDecimal,
            confirmButtonOptions = confirmButtonOptions.value,
            onHide = {
                visible = false
            },
            onConfirm = {}
        ) {
            value = it
        }
    }
}

@Preview
@Composable
private fun PreviewDigitalKeyboard() {
    WeUITheme {
        DigitalKeyboardScreen()
    }
}