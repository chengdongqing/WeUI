package top.chengdongqing.weui.util

import kotlinx.browser.window
import org.w3c.files.File
import kotlin.js.Promise

@OptIn(ExperimentalWasmJsInterop::class)
external interface FileSystemHandle : JsAny {
    val name: String
    val kind: String
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface FileSystemFileHandle : FileSystemHandle {
    fun getFile(): Promise<File>
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface FileSystemDirectoryHandle : FileSystemHandle {
    fun values(): JsArray<FileSystemHandle>
    fun getFileHandle(name: String): Promise<FileSystemFileHandle>
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface Window : JsAny {
    fun showDirectoryPicker(): Promise<FileSystemDirectoryHandle>
}

@OptIn(ExperimentalWasmJsInterop::class)
val myWindow = window.unsafeCast<Window>()