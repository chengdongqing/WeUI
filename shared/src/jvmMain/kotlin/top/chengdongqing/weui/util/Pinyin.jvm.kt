package top.chengdongqing.weui.util

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum
import com.github.houbb.pinyin.util.PinyinHelper

actual fun String.getInitial(): Char {
    if (this.isBlank()) return '#'

    val firstChar = this.first()

    return if (firstChar.isChinese) {
        PinyinHelper.toPinyin(this, PinyinStyleEnum.FIRST_LETTER).first()
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