package top.chengdongqing.weui.feature.location.data.model

enum class MapType(val appName: String) {
    AMAP("高德"),
    BAIDU("百度"),
    TENCENT("腾讯"),
    GOOGLE("谷歌");

    companion object {
        fun ofIndex(index: Int): MapType? {
            return entries.getOrNull(index)
        }
    }
}