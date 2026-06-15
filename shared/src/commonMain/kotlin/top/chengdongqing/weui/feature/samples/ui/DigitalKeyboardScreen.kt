package top.chengdongqing.weui.feature.samples.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.digitalkeyboard.DigitalKeyboardConfirmOptions
import top.chengdongqing.weui.core.ui.components.digitalkeyboard.WeDigitalKeyboard
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.theme.RedDanger
import top.chengdongqing.weui.util.rememberToggleState
import top.chengdongqing.weui.util.weClickable

@Composable
fun DigitalKeyboardScreen(onBack: () -> Unit) {
    WeScreen(
        title = "DigitalKeyboard",
        description = "数字键盘",
        onBack = onBack
    ) {
        var value by remember { mutableStateOf("") }
        var visible by remember { mutableStateOf(true) }
        var allowDecimal by remember { mutableStateOf(true) }
        val (confirmButtonOptions, toggleConfirmButtonOptions) = rememberToggleState(
            defaultValue = DigitalKeyboardConfirmOptions(),
            reverseValue = DigitalKeyboardConfirmOptions(
                color = RedDanger,
                text = "转账"
            )
        )

        WeInput(
            value = value,
            label = "金额",
            placeholder = "请输入",
            disabled = true,
            modifier = Modifier.weClickable {
                visible = true
            }
        )

        if (visible) {
            Spacer(modifier = Modifier.height(40.dp))
            WeButton(
                text = "${if (allowDecimal) "不" else ""}允许小数点",
                type = ButtonType.Plain
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