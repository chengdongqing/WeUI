package top.chengdongqing.weui.core.utils

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum
import com.github.houbb.pinyin.util.PinyinHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PinyinUtils {
    suspend fun groupByInitial(labels: List<String>): Map<Char, List<String>> =
        withContext(Dispatchers.Default) {
            val groupedCities = mutableMapOf<Char, MutableList<String>>()

            labels.forEach { label ->
                val firstChar = label.first()
                val initial = if (firstChar.isChinese()) {
                    PinyinHelper.toPinyin(label, PinyinStyleEnum.FIRST_LETTER).first()
                } else {
                    firstChar
                }.uppercaseChar()
                if (initial in 'A'..'Z') {
                    groupedCities.getOrPut(initial) { mutableListOf() }.add(label)
                } else {
                    groupedCities.getOrPut('#') { mutableListOf() }.add(label)
                }
            }
            groupedCities.values.forEach { it.sort() }

            groupedCities
        }
}

fun Char.isChinese(): Boolean {
    return this.code in 0x4E00..0x9FFF
}