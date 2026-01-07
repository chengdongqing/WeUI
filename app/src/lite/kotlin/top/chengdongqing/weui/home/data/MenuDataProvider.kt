package top.chengdongqing.weui.home.data

internal object MenuDataProvider {
    val menuGroups = MenuGroups.filter { it.title != "地图组件" }
}
