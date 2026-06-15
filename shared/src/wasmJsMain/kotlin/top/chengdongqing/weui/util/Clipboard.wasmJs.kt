package top.chengdongqing.weui.util

import kotlinx.browser.window
import kotlinx.coroutines.await

@OptIn(ExperimentalWasmJsInterop::class)
class WasmClipboard : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        runCatching {
            window.navigator.clipboard.writeText(data).await<Unit>()
        }.onFailure {
            it.printStackTrace()
        }
    }

    override suspend fun getClipboardData(): String? {
        return runCatching {
            (window.navigator.clipboard.readText().await<JsString>()).toString()
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }
}

actual fun getClipboard(): Clipboard = WasmClipboard()