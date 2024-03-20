package top.chengdongqing.weui.feature.basic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.basic.screens.BadgeScreen
import top.chengdongqing.weui.feature.basic.screens.LoadMoreScreen
import top.chengdongqing.weui.feature.basic.screens.LoadingScreen
import top.chengdongqing.weui.feature.basic.screens.ProgressScreen
import top.chengdongqing.weui.feature.basic.screens.RefreshViewScreen
import top.chengdongqing.weui.feature.basic.screens.SkeletonScreen
import top.chengdongqing.weui.feature.basic.screens.StepsScreen
import top.chengdongqing.weui.feature.basic.screens.SwipeActionScreen
import top.chengdongqing.weui.feature.basic.screens.SwiperScreen
import top.chengdongqing.weui.feature.basic.screens.TabViewScreen
import top.chengdongqing.weui.feature.basic.screens.TreeScreen

fun NavGraphBuilder.addBasicGraph() {
    composable("badge") {
        BadgeScreen()
    }
    composable("loading") {
        LoadingScreen()
    }
    composable("load_more") {
        LoadMoreScreen()
    }
    composable("progress") {
        ProgressScreen()
    }
    composable("steps") {
        StepsScreen()
    }
    composable("swiper") {
        SwiperScreen()
    }
    composable("refresh_view") {
        RefreshViewScreen()
    }
    composable("tab_view") {
        TabViewScreen()
    }
    composable("swipe_action") {
        SwipeActionScreen()
    }
    composable("skeleton") {
        SkeletonScreen()
    }
    composable("tree") {
        TreeScreen()
    }
}