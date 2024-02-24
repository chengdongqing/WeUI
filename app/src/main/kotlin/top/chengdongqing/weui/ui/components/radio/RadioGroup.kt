package top.chengdongqing.weui.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

data class RadioOption<T>(
    val label: String,
    val value: T,
    val disabled: Boolean = false
)

/**
 * 单选框
 *
 * @param options 可选项列表
 * @param value 已选中的value
 * @param disabled 是否禁用
 * @param onChange 选中项改变事件
 */
@Composable
fun <T> WeRadioGroup(
    options: List<RadioOption<T>>,
    value: T? = null,
    disabled: Boolean = false,
    onChange: ((value: T) -> Unit)? = null
) {
    Column {
        for (option in options) {
            WeRadio(
                label = option.label,
                checked = option.value == value,
                disabled = disabled || option.disabled
            ) {
                onChange?.invoke(option.value)
            }
        }
    }
}