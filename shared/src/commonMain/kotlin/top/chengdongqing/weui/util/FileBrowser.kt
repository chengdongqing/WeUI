package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable

@Composable
expect fun RequestStoragePermission(content: @Composable () -> Unit)

expect fun getStorageRootPath(): String

expect suspend fun calculateFileSize(filePath: String): Long