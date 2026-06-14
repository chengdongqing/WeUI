package top.chengdongqing.weui.util

import platform.UIKit.UIPasteboard

class IOSClipboard : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        UIPasteboard.generalPasteboard.string = data
    }
}

actual fun getClipboard(): Clipboard = IOSClipboard()