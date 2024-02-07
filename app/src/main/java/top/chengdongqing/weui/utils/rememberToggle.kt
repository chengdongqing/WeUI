package top.chengdongqing.weui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberToggle(defaultValue: T, reverseValue: T): Pair<T, () -> T> {
    val (value, setValue) = remember { mutableStateOf(defaultValue) }

    val toggleValue = {
        val newValue = if (value == defaultValue) reverseValue else defaultValue
        setValue(newValue)
        newValue
    }

    return Pair(value, toggleValue)
}