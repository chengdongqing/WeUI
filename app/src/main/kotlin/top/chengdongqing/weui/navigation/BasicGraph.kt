package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.basic.BadgeScreen
import top.chengdongqing.weui.ui.screens.basic.LoadMoreScreen
import top.chengdongqing.weui.ui.screens.basic.LoadingScreen
import top.chengdongqing.weui.ui.screens.basic.ProgressScreen
import top.chengdongqing.weui.ui.screens.basic.RefreshableScreen
import top.chengdongqing.weui.ui.screens.basic.SkeletonScreen
import top.chengdongqing.weui.ui.screens.basic.StepsScreen
import top.chengdongqing.weui.ui.screens.basic.SwipeActionScreen
import top.chengdongqing.weui.ui.screens.basic.SwiperScreen
import top.chengdongqing.weui.ui.screens.basic.TabViewScreen
import top.chengdongqing.weui.ui.screens.basic.TreeScreen

fun NavGraphBuilder.addBasicGraph() {
    composable("badge") {
        BadgeScreen()
    }
    composable("loading") {
        LoadingScreen()
    }
    composable("load-more") {
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
    composable("refreshable-view") {
        RefreshableScreen()
    }
    composable("tab-view") {
        TabViewScreen()
    }
    composable("swipe-action") {
        SwipeActionScreen()
    }
    composable("skeleton") {
        SkeletonScreen()
    }
    composable("tree") {
        TreeScreen()
    }
}