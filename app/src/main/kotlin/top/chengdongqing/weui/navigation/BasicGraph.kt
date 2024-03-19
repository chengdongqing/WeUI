package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.basic.BadgeScreen
import top.chengdongqing.weui.ui.screens.basic.LoadMoreScreen
import top.chengdongqing.weui.ui.screens.basic.LoadingScreen
import top.chengdongqing.weui.ui.screens.basic.ProgressScreen
import top.chengdongqing.weui.ui.screens.basic.RefreshViewScreen
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