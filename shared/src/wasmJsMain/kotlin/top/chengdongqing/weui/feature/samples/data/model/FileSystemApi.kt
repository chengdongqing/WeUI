package top.chengdongqing.weui.feature.samples.data.model

import kotlinx.browser.window
import org.khronos.webgl.ArrayBuffer
import kotlin.js.Promise

val FileSystemHandle.isDirectory: Boolean
    get() = this.kind == "directory"

@OptIn(ExperimentalWasmJsInterop::class)
external interface DirectoryPickerOptions : JsAny {
    var mode: String?     // 可选值："read" 或 "readwrite"
    var id: String?       // 可选，用于让浏览器记住上次打开的目录
    var startIn: JsAny?   // 可选，起始目录（可传入特定字符串或其它句柄）
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => ({})")
external fun createDirectoryPickerOptions(): DirectoryPickerOptions

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
    fun text(): Promise<JsString>
    fun arrayBuffer(): Promise<ArrayBuffer>
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

@OptIn(ExperimentalWasmJsInterop::class)
external interface RemoveEntryOptions : JsAny {
    var recursive: Boolean? // 是否递归删除子目录及文件
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => ({})")
external fun createRemoveEntryOptions(): RemoveEntryOptions

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(buffer) => [buffer]")
external fun wrapInJsArray(buffer: ArrayBuffer): JsArray<JsAny?>