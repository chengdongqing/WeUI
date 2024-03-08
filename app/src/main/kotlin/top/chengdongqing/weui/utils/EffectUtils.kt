package top.chengdongqing.weui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope

@Composable
fun UpdatedEffect(key: Any?, block: suspend CoroutineScope.() -> Unit) {
    val updated = remember { mutableStateOf(false) }

    LaunchedEffect(key) {
        if (updated.value) {
            block()
        }
        updated.value = true
    }
}