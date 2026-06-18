package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.feature.samples.data.model.FileNode

@Composable
expect fun RequestStoragePermission(content: @Composable (FileNode) -> Unit)

expect suspend fun calculateFileSize(fileNode: FileNode): Long