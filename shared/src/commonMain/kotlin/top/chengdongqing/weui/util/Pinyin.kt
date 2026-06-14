package top.chengdongqing.weui.util

/**
 * 获取首字母
 */
expect fun String.getInitial(): Char

/**
 * 是否是中文字符
 */
val Char.isChinese: Boolean
    get() = this.code in 0x4E00..0x9FFF