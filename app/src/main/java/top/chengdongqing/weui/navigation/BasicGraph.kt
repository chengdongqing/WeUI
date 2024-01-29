package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import top.chengdongqing.weui.ui.views.basic.BadgePage
import top.chengdongqing.weui.ui.views.basic.CalendarPage
import top.chengdongqing.weui.ui.views.basic.LoadMorePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.basic.ProgressPage
import top.chengdongqing.weui.ui.views.basic.ScrollViewPage
import top.chengdongqing.weui.ui.views.basic.StepsPage
import top.chengdongqing.weui.ui.views.basic.SwiperPage

fun NavGraphBuilder.addBasicGraph() {
    navigation("badge", "basic") {
        composable("badge") {
            BadgePage()
        }
        composable("loading") {
            LoadingPage()
        }
        composable("load-more") {
            LoadMorePage()
        }
        composable("progress") {
            ProgressPage()
        }
        composable("steps") {
            StepsPage()
        }
        composable("swiper") {
            SwiperPage()
        }
        composable("scroll-view") {
            ScrollViewPage()
        }
        composable("calendar") {
            CalendarPage()
        }
    }
}