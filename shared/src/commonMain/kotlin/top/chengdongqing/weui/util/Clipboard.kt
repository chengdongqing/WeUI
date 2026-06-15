package top.chengdongqing.weui.util

interface Clipboard {
    suspend fun setClipboardData(data: String, label: String = "label")

    suspend fun getClipboardData(): String?
}

expect fun getClipboard(): Clipboard