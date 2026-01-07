package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import top.chengdongqing.weui.feature.location.navigation.addLocationGraph

class LocationNavImpl : LocationNav {
    override fun addLocationGraph(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.addLocationGraph()
    }
}
