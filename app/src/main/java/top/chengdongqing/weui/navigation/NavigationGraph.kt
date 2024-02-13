package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.layers.LayersPage
import top.chengdongqing.weui.ui.views.map.LocationPickerPage
import top.chengdongqing.weui.ui.views.map.LocationPreviewPage
import top.chengdongqing.weui.ui.views.qrcode.generator.QrCodeGeneratePage
import top.chengdongqing.weui.ui.views.qrcode.scanner.QrCodeScanPage
import top.chengdongqing.weui.ui.views.search.SearchBarPage

@Composable
fun NavigationGraph() {
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
            HomePage(navController)
        }
        addBasicGraph()
        addFormGraph()
        addFeedbackGraph()
        addMediaGraph(navController)
        addSystemGraph(navController)
        addNetworkGraph()
        addHardwareGraph()
        addChartGraph()
        addQrCodeGraph(navController)
        addMapGraph()
        addSearchGraph()
        addLayersGraph()
    }
}

private fun NavGraphBuilder.addQrCodeGraph(navController: NavHostController) {
    navigation("qrcode-generator", "qrcode") {
        composable("qrcode-scanner") {
            QrCodeScanPage(navController)
        }
        composable("qrcode-generator") {
            QrCodeGeneratePage()
        }
    }
}

private fun NavGraphBuilder.addMapGraph() {
    navigation("location-preview", "location") {
        composable("location-preview") {
            LocationPreviewPage()
        }
        composable("location-picker") {
            LocationPickerPage()
        }
    }
}

private fun NavGraphBuilder.addSearchGraph() {
    composable("search-bar") {
        SearchBarPage()
    }
}

private fun NavGraphBuilder.addLayersGraph() {
    composable("layers") {
        LayersPage()
    }
}