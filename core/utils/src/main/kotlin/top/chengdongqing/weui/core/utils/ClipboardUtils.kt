package top.chengdongqing.weui.core.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun Context.getClipboardData(): String? =
    clipboardManager?.primaryClip?.takeIf { it.itemCount > 0 }
        ?.getItemAt(0)?.text?.toString()

fun Context.setClipboardData(data: String, label: String = "label") {
    clipboardManager?.apply {
        val clip = ClipData.newPlainText(label, data)
        setPrimaryClip(clip)
    }
}

private val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager