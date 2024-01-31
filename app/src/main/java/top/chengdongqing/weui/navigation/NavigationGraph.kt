package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.layers.LayersPage
import top.chengdongqing.weui.ui.views.map.LocationPickerPage
import top.chengdongqing.weui.ui.views.map.LocationPreviewPage
import top.chengdongqing.weui.ui.views.media.gallery.GalleryViewModel
import top.chengdongqing.weui.ui.views.navigation.NavBarPage
import top.chengdongqing.weui.ui.views.navigation.TabBarPage
import top.chengdongqing.weui.ui.views.search.SearchBarPage

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val galleryViewModel: GalleryViewModel = viewModel()

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
        addMediaGraph(navController, galleryViewModel)
        addSystemGraph(navController)
        addNetworkGraph()
        addHardwareGraph()
        addChartGraph()
        addMapGraph()
        addNavigationGraph()
        addSearchGraph()
        addLayersGraph()
    }
}

fun NavGraphBuilder.addMapGraph() {
    navigation("location-preview", "location") {
        composable("location-preview") {
            LocationPreviewPage()
        }
        composable("location-picker") {
            LocationPickerPage()
        }
    }
}

fun NavGraphBuilder.addNavigationGraph() {
    navigation("nav-bar", "navigation") {
        composable("nav-bar") {
            NavBarPage()
        }
        composable("tab-bar") {
            TabBarPage()
        }
    }
}

fun NavGraphBuilder.addSearchGraph() {
    composable("search-bar") {
        SearchBarPage()
    }
}

fun NavGraphBuilder.addLayersGraph() {
    composable("layers") {
        LayersPage()
    }
}