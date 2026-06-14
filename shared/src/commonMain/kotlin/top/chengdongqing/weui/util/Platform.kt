package top.chengdongqing.weui.util

enum class AppPlatform {
    Android,
    Ios,
    Desktop,
    Web
}

expect fun getPlatform(): AppPlatform