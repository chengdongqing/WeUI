package top.chengdongqing.weui.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class JvmClipboard : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(data)
        clipboard.setContents(selection, null)
    }

    override suspend fun getClipboardData(): String? {
        return runCatching {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                withContext(Dispatchers.IO) {
                    clipboard.getData(DataFlavor.stringFlavor)
                } as? String
            } else null
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }
}

actual fun getClipboard(): Clipboard = JvmClipboard()