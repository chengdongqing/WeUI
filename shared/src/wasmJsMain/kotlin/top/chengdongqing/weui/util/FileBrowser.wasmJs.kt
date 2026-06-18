package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import top.chengdongqing.weui.feature.samples.data.model.FileSystemDirectoryHandle
import top.chengdongqing.weui.feature.samples.data.model.FileSystemFileHandle
import top.chengdongqing.weui.feature.samples.data.model.JsFile
import top.chengdongqing.weui.feature.samples.data.model.JsIteratorResult
import top.chengdongqing.weui.feature.samples.data.model.WasmFileNode
import top.chengdongqing.weui.feature.samples.data.model.createDirectoryPickerOptions
import top.chengdongqing.weui.feature.samples.data.model.isDirectory
import top.chengdongqing.weui.feature.samples.data.model.myWindow

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun RequestStoragePermission(content: @Composable ((FileNode) -> Unit)) {
    var directoryHandle by remember { mutableStateOf<FileSystemDirectoryHandle?>(null) }
    val scope = rememberCoroutineScope()

    directoryHandle?.let {
        content(
            WasmFileNode(
                id = it.name,
                name = it.name,
                isDirectory = true,
                handle = it,
                parentHandle = it
            )
        )
    } ?: WeButton(
        text = "选择文件夹",
        width = 200.dp,
        type = ButtonType.Plain
    ) {
        scope.launch {
            runCatching {
                directoryHandle = myWindow.showDirectoryPicker(
                    createDirectoryPickerOptions().apply {
                        mode = "readwrite"
                    }
                ).await<FileSystemDirectoryHandle>()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
actual suspend fun calculateFileSize(fileNode: FileNode): Long {
    val node = fileNode as WasmFileNode
    val handle = node.handle

    if (!handle.isDirectory) {
        return getSingleFileSize(handle.unsafeCast())
    }

    return walkDirectory(handle.unsafeCast())
}

@OptIn(ExperimentalWasmJsInterop::class)
private suspend fun walkDirectory(directoryHandle: FileSystemDirectoryHandle): Long {
    var totalSize = 0L

    try {
        val children = directoryHandle.children()

        for (child in children) {
            when {
                !child.isDirectory -> {
                    val fileHandle = child.unsafeCast<FileSystemFileHandle>()
                    totalSize += getSingleFileSize(fileHandle)
                }

                else -> {
                    val subDirHandle = child.unsafeCast<FileSystemDirectoryHandle>()
                    totalSize += walkDirectory(subDirHandle)
                }
            }
        }
    } catch (e: Throwable) {
        println("访问目录失败，已跳过: ${e.message}")
    }

    return totalSize
}

@OptIn(ExperimentalWasmJsInterop::class)
private suspend fun getSingleFileSize(fileHandle: FileSystemFileHandle): Long {
    return runCatching {
        val jsFile = fileHandle.getFile().await<JsFile>()
        jsFile.size.toLong()
    }.getOrDefault(0)
}

@OptIn(ExperimentalWasmJsInterop::class)
suspend fun FileSystemDirectoryHandle.children(): List<FileSystemDirectoryHandle> {

    val result = mutableListOf<FileSystemDirectoryHandle>()
    val iterator = values()

    while (true) {
        val item = iterator.next()
            .await<JsIteratorResult<FileSystemDirectoryHandle>>()

        if (item.done) break

        item.value?.let(result::add)
    }

    return result
}