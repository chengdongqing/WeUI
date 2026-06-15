package top.chengdongqing.weui.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import top.chengdongqing.weui.androidAppInstance

class AndroidClipboard(private val context: Context) : Clipboard {
    private val clipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    }

    override suspend fun setClipboardData(data: String, label: String) {
        val clip = ClipData.newPlainText(label, data)
        clipboardManager?.setPrimaryClip(clip)
    }

    override suspend fun getClipboardData(): String? {
        return clipboardManager?.primaryClip?.takeIf { it.itemCount > 0 }
            ?.getItemAt(0)?.text?.toString()
    }
}

actual fun getClipboard(): Clipboard = AndroidClipboard(androidAppInstance)