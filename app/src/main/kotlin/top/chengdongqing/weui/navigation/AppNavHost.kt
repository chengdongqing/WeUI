package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.feature.basic.navigation.addBasicGraph
import top.chengdongqing.weui.feature.charts.navigation.addChartGraph
import top.chengdongqing.weui.feature.feedback.navigation.addFeedbackGraph
import top.chengdongqing.weui.feature.form.navigation.addFormGraph
import top.chengdongqing.weui.feature.hardware.navigation.addHardwareGraph
import top.chengdongqing.weui.feature.location.navigation.addLocationGraph
import top.chengdongqing.weui.feature.media.navigation.addMediaGraph
import top.chengdongqing.weui.feature.network.navigation.addNetworkGraph
import top.chengdongqing.weui.feature.qrcode.navigation.addQrCodeGraph
import top.chengdongqing.weui.feature.samples.navigation.addSamplesGraph
import top.chengdongqing.weui.feature.system.navigation.addSystemGraph
import top.chengdongqing.weui.home.HomeScreen
import top.chengdongqing.weui.layers.LayersScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "home",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        addBasicGraph()
        addFormGraph()
        addFeedbackGraph()
        addMediaGraph(navController)
        addSystemGraph(navController)
        addNetworkGraph()
        addHardwareGraph()
        addChartGraph()
        addQrCodeGraph()
        addLocationGraph()
        addSamplesGraph()
        composable("layers") {
            LayersScreen()
        }
    }
}


/**
 * NavHost提供了四种主要的动画配置：enterTransition、exitTransition、popEnterTransition和popExitTransition。
 *
 * 1. enterTransition（进入转换）：当导航到一个新的组件时，这个新组件的进入动画。简而言之，当你从组件A导航到组件B时，组件B的`enterTransition`定义了它是如何出现在屏幕上的。
 *
 * 2. exitTransition`退出转换）：当从当前组件导航到另一个组件时，当前组件的退出动画。当你从组件A导航到组件B时，组件A的`exitTransition`定义了它是如何从屏幕上消失的。
 *
 * 3. popEnterTransition（弹出进入转换）：当从当前组件回退到前一个组件时，那个前一个组件的进入动画。如果你在组件B中按下返回按钮，预期回到组件A，那么组件A的`popEnterTransition`定义了它是如何重新出现在屏幕上的。
 *
 * 4. popExitTransition（弹出退出转换）：当通过回退操作退出当前组件时，当前组件的退出动画。如果你在组件B中按下返回按钮回到组件A，那么组件B的`popExitTransition`定义了它是如何从屏幕上消失的。
 */