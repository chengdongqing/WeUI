package top.chengdongqing.weui.feature.demos.components.digitalkeyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.theme.PrimaryColor

@Composable
fun WeDigitalKeyboard(
    visible: Boolean,
    value: String,
    integers: Int = 6,
    decimals: Int = 2,
    allowDecimal: Boolean = true,
    confirmButtonOptions: DigitalKeyboardConfirmOptions = DigitalKeyboardConfirmOptions(),
    onHide: () -> Unit,
    onConfirm: () -> Unit,
    onChange: (String) -> Unit
) {
    var widthPerItem by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    KeyboardPopup(visible, onHide) {
        Column {
            WeDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
                    .onSizeChanged {
                        widthPerItem = density.run { (it.width.toDp() - 8.dp * 3) / 4 }
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DigitalGrid(widthPerItem, allowDecimal) {
                    value.onKeyClick(it, integers, decimals, onChange)
                }
                ActionBar(
                    widthPerItem,
                    confirmButtonOptions,
                    isEmpty = value.isEmpty(),
                    onBack = { value.onBack(onChange) },
                    onConfirm
                )
            }
        }
    }
}

private fun String.onKeyClick(
    key: String,
    integers: Int,
    decimals: Int,
    onChange: (String) -> Unit
) {
    // 不能有多个小数点
    if (key == "." && this.contains(".")) return

    val newValue = this + key
    // 整数部分长度控制
    val index = newValue.indexOf(".")
    val integerLength = if (index != -1) index else newValue.length
    if (integerLength > integers) return
    // 小数部分长度控制
    val decimalLength = if (index != -1) newValue.lastIndex - index else 0
    if (decimalLength > decimals) return

    onChange(newValue)
}

private fun String.onBack(onChange: (String) -> Unit) {
    if (this.isNotEmpty()) {
        onChange(this.dropLast(1))
    }
}

data class DigitalKeyboardConfirmOptions(
    val color: Color = PrimaryColor,
    val text: String = "确定"
)