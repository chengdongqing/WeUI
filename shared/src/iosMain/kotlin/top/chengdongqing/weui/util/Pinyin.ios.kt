package top.chengdongqing.weui.util

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSStringTransformMandarinToLatin
import platform.Foundation.NSStringTransformStripDiacritics
import platform.Foundation.create
import platform.Foundation.stringByApplyingTransform

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun String.getInitial(): Char {
    if (isBlank()) return '#'

    val firstChar = first()
    val initial = if (firstChar.isChinese) {
        val source = NSString.create(string = this)
        val pinyin = source
            .stringByApplyingTransform(NSStringTransformMandarinToLatin, reverse = false)
            ?.let { transformed ->
                NSString.create(string = transformed).stringByApplyingTransform(
                    NSStringTransformStripDiacritics,
                    reverse = false
                )
            }
        pinyin?.firstOrNull() ?: '#'
    } else {
        firstChar
    }.uppercaseChar()

    return initial.takeIf { it in 'A'..'Z' } ?: '#'
}
