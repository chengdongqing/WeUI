package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.screens.HomeScreen
import top.chengdongqing.weui.ui.screens.layers.LayersScreen
import top.chengdongqing.weui.ui.screens.map.LocationPickerScreen
import top.chengdongqing.weui.ui.screens.map.LocationPreviewScreen
import top.chengdongqing.weui.ui.screens.qrcode.generator.QrCodeGenerateScreen
import top.chengdongqing.weui.ui.screens.qrcode.scanner.QrCodeScanScreen

@Composable
fun ApplicationNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "drop-card",
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
        addDemoGraph()

        composable("qrcode-scanner") {
            QrCodeScanScreen(navController)
        }
        composable("qrcode-generator") {
            QrCodeGenerateScreen()
        }
        composable("location-preview") {
            LocationPreviewScreen()
        }
        composable("location-picker") {
            LocationPickerScreen()
        }
        composable("layers") {
            LayersScreen()
        }
    }
}