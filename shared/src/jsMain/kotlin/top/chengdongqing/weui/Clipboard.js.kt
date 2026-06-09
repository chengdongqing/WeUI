package top.chengdongqing.weui

import kotlinx.browser.window
import kotlinx.coroutines.await

class JsClipboard : Clipboard {
    @OptIn(ExperimentalWasmJsInterop::class)
    override suspend fun setClipboardData(data: String, label: String) {
        runCatching {
            window.navigator.clipboard.writeText(data).await()
        }.onFailure {
            throw Exception("Clipboard access denied")
        }
    }
}

actual fun getClipboard(): Clipboard = JsClipboard()