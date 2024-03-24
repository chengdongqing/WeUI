package top.chengdongqing.weui.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun rememberSingleThreadExecutor(): ExecutorService {
    val executor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            executor.shutdown()
        }
    }

    return executor
}
