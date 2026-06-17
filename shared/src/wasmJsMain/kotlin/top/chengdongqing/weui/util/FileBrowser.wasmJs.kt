package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun RequestStoragePermission(content: @Composable ((String) -> Unit)) {
    var directoryHandle by remember { mutableStateOf<FileSystemHandle?>(null) }

    directoryHandle?.let {
        content(it.name)
    } ?: WeButton(
        text = "选择文件夹",
        width = 200.dp,
        type = ButtonType.Plain
    ) {
        MainScope().launch {
            try {
                directoryHandle = myWindow.showDirectoryPicker().await<FileSystemHandle>()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

actual suspend fun calculateFileSize(filePath: String): Long {
    return 0
}