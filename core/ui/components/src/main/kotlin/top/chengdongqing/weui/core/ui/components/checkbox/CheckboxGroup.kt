package top.chengdongqing.weui.core.ui.components.checkbox

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

data class CheckboxOption<T>(
    val label: String,
    val value: T,
    val disabled: Boolean = false
)

/**
 * 多选框
 *
 * @param options 可选项列表
 * @param values 已选中的value列表
 * @param disabled 是否禁用
 * @param onChange 选中项改变事件
 */
@Composable
fun <T> WeCheckboxGroup(
    options: List<CheckboxOption<T>>,
    values: List<T> = listOf(),
    disabled: Boolean = false,
    onChange: ((values: List<T>) -> Unit)? = null
) {
    Column {
        for (option in options) {
            WeCheckbox(
                label = option.label,
                checked = values.contains(option.value),
                disabled = disabled || option.disabled
            ) {
                val newValues = if (values.contains(option.value)) {
                    values.filter { it != option.value }
                } else {
                    values + option.value
                }
                onChange?.invoke(newValues)
            }
        }
    }
}