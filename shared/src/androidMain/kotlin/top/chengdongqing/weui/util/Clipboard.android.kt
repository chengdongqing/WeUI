package top.chengdongqing.weui.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import top.chengdongqing.weui.androidAppInstance

class AndroidClipboard(private val context: Context) : Clipboard {
    override suspend fun setClipboardData(data: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, data)
        clipboard?.setPrimaryClip(clip)
    }
}

actual fun getClipboard(): Clipboard = AndroidClipboard(androidAppInstance)