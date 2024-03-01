package top.chengdongqing.weui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberToggleState(defaultValue: T, reverseValue: T): Pair<T, () -> T> {
    val (value, setValue) = remember { mutableStateOf(defaultValue) }

    val toggleValue = {
        val newValue = if (value == defaultValue) reverseValue else defaultValue
        setValue(newValue)
        newValue
    }

    return Pair(value, toggleValue)
}

@Composable
fun <T> rememberLastState(value: T): T {
    val (last, setLast) = remember { mutableStateOf(value) }
    val (old, setOld) = remember { mutableStateOf(value) }

    LaunchedEffect(value) {
        setLast(old)
        setOld(value)
    }

    return last
}