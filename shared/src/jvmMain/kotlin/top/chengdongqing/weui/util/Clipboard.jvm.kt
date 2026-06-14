package top.chengdongqing.weui.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class JvmClipboard : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(data)
        clipboard.setContents(selection, null)
    }
}

actual fun getClipboard(): Clipboard = JvmClipboard()