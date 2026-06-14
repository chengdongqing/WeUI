package top.chengdongqing.weui.util

interface Clipboard {
    suspend fun setClipboardData(data: String, label: String = "label")
}

expect fun getClipboard(): Clipboard