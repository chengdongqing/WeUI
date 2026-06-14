package top.chengdongqing.weui.util

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsModule

@OptIn(ExperimentalWasmJsInterop::class)
@JsModule("pinyin-pro")
external object PinyinPro {
    fun pinyin(text: String): String
}

@OptIn(ExperimentalWasmJsInterop::class)
actual fun String.getInitial(): Char {
    if (this.isBlank()) return '#'

    val firstChar = this.first()

    return if (firstChar.isChinese) {
        PinyinPro.pinyin(this).first()
    } else {
        firstChar
    }.uppercaseChar().let {
        if (it in 'A'..'Z') {
            it
        } else {
            '#'
        }
    }
}