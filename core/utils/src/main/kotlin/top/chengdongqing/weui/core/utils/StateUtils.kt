package top.chengdongqing.weui.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberToggleState(defaultValue: T, reverseValue: T): Pair<MutableState<T>, () -> T> {
    val state = remember { mutableStateOf(defaultValue) }

    val toggleValue = {
        val newValue = if (state.value == defaultValue) reverseValue else defaultValue
        state.value = newValue
        newValue
    }

    return Pair(state, toggleValue)
}
