package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable

@Composable
actual fun RequestStoragePermission(content: @Composable ((String) -> Unit)) {
}

actual suspend fun calculateFileSize(filePath: String): Long {
    TODO("Not yet implemented")
}