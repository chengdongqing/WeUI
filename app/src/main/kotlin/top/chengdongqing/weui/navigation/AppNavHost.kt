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