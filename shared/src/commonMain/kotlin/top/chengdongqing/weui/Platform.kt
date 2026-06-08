package top.chengdongqing.weui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform