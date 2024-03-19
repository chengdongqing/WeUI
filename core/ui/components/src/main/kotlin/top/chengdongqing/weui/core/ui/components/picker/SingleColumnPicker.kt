package top.chengdongqing.weui.core.ui.components.picker

import androidx.compose.runtime.Composable

@Composable
fun WeSingleColumnPicker(
    visible: Boolean,
    title: String? = null,
    range: List<String>,
    value: Int,
    onChange: (Int) -> Unit,
    onCancel: () -> Unit
) {
    WePicker(
        visible,
        title,
        range = arrayOf(range),
        value = arrayOf(value),
        onChange = {
            onChange(it.first())
        },
        onCancel = onCancel
    )
}