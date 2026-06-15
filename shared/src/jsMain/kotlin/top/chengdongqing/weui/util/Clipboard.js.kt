package top.chengdongqing.weui.util

import kotlinx.browser.window
import kotlinx.coroutines.await

class JsClipboard : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        runCatching {
            window.navigator.clipboard.writeText(data).await()
        }.onFailure {
            it.printStackTrace()
        }
    }

    override suspend fun getClipboardData(): String? {
        return runCatching {
            window.navigator.clipboard.readText().await()
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }
}

actual fun getClipboard(): Clipboard = JsClipboard()