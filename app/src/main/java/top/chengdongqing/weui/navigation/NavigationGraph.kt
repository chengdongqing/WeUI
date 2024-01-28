package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(300)
            )
        }
    ) {
        composable("home",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            }
        ) {
            HomePage(navController)
        }

        basicGraph()
        formGraph()
        feedbackGraph()
        mediaGraph(navController, galleryViewModel)
        systemGraph(navController)
        networkGraph()
        hardwareGraph()
        chartGraph()
        mapGraph()
        navigationGraph()
        searchGraph()
        layersGraph()
    }
}

fun NavGraphBuilder.mapGraph() {
    composable("location-preview") {
        LocationPreviewPage()
    }
    composable("location-picker") {
        LocationPickerPage()
    }
}

fun NavGraphBuilder.navigationGraph() {
    composable("nav-bar") {
        NavBarPage()
    }
    composable("tab-bar") {
        TabBarPage()
    }
}

fun NavGraphBuilder.searchGraph() {
    composable("search-bar") {
        SearchBarPage()
    }
}

fun NavGraphBuilder.layersGraph() {
    composable("layers") {
        LayersPage()
    }
}