package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.feature.samples.data.model.FileNode

@Composable
actual fun RequestStoragePermission(content: @Composable ((FileNode) -> Unit)) {
}

actual suspend fun calculateFileSize(fileNode: FileNode): Long {
    TODO("Not yet implemented")
}