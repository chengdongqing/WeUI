package top.chengdongqing.weui.core.ui.components.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun WePicker(
    visible: Boolean,
    title: String? = null,
    range: List<String>,
    value: Int,
    onChange: (Int) -> Unit,
    onCancel: () -> Unit
) {
    WeMultipleColumnPicker(
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

@Stable
interface PickerState {
    val visible: Boolean

    fun show(
        title: String? = null,
        range: List<String>,
        value: Int,
        onChange: (Int) -> Unit
    )

    fun hide()
}

@Composable
fun rememberPickerState(): PickerState {
    val state = remember { PickerStateImpl() }

    state.props?.let { props ->
        WePicker(
            visible = state.visible,
            title = props.title,
            range = props.range,
            value = props.value,
            onChange = props.onChange,
            onCancel = { state.hide() }
        )
    }

    return state
}

private class PickerStateImpl : PickerState {
    override var visible by mutableStateOf(false)
    var props by mutableStateOf<PickerProps?>(null)
        private set

    override fun show(
        title: String?,
        range: List<String>,
        value: Int,
        onChange: (Int) -> Unit
    ) {
        props = PickerProps(title, range, value, onChange)
        visible = true
    }

    override fun hide() {
        visible = false
    }
}

private data class PickerProps(
    val title: String? = null,
    val range: List<String>,
    val value: Int,
    val onChange: (Int) -> Unit
)