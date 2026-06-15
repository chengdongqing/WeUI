package top.chengdongqing.weui.util

import platform.UIKit.UIPasteboard

class IOSClipboard : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        UIPasteboard.generalPasteboard.string = data
    }

    override suspend fun getClipboardData(): String? {
        return UIPasteboard.generalPasteboard.string
    }
}

actual fun getClipboard(): Clipboard = IOSClipboard()