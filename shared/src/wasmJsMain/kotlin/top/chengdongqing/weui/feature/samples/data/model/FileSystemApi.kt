package top.chengdongqing.weui.feature.samples.data.model

import kotlinx.browser.window
import kotlin.js.Promise

@OptIn(ExperimentalWasmJsInterop::class)
external interface Window : JsAny {
    fun showDirectoryPicker(options: DirectoryPickerOptions? = definedExternally): Promise<FileSystemHandle>
}

@OptIn(ExperimentalWasmJsInterop::class)
val myWindow = window.unsafeCast<Window>()

@OptIn(ExperimentalWasmJsInterop::class)
external interface FileSystemHandle : JsAny {
    val kind: String
    val name: String
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface FileSystemDirectoryHandle : FileSystemHandle {
    fun values(): JsAsyncIterator<FileSystemHandle>

    fun removeEntry(
        name: String,
        options: RemoveEntryOptions? = definedExternally
    ): Promise<JsAny>
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface FileSystemFileHandle : FileSystemHandle {
    fun getFile(): Promise<JsFile>
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsFile : JsAny {
    val name: JsString
    val type: JsString
    val size: JsBigInt
    val lastModified: JsBigInt
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsAsyncIterator<T : JsAny> : JsAny {
    fun next(): Promise<JsIteratorResult<T>>
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsIteratorResult<T : JsAny> : JsAny {
    val done: Boolean
    val value: T?
}

val FileSystemHandle.isDirectory: Boolean
    get() = this.kind == "directory"

@OptIn(ExperimentalWasmJsInterop::class)
external interface DirectoryPickerOptions : JsAny {
    var mode: String?
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => ({})")
external fun createDirectoryPickerOptions(): DirectoryPickerOptions

@OptIn(ExperimentalWasmJsInterop::class)
external interface RemoveEntryOptions : JsAny {
    var recursive: Boolean? // 是否递归删除子目录及文件
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => ({})")
external fun createRemoveEntryOptions(): RemoveEntryOptions