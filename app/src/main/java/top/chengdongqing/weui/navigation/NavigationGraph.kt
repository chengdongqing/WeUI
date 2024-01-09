package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.feedback.ActionSheetPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.feedback.PopupPage
import top.chengdongqing.weui.ui.views.form.ButtonPage
import top.chengdongqing.weui.ui.views.form.CheckboxPage
import top.chengdongqing.weui.ui.views.form.RadioPage
import top.chengdongqing.weui.ui.views.form.SliderPage
import top.chengdongqing.weui.ui.views.form.SwitchPage
import top.chengdongqing.weui.ui.views.media.GalleryPage

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
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("home",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        ) {
            HomePage(navController)
        }

        basicGraph()
        formGraph()
        feedbackGraph()
    }
}

fun NavGraphBuilder.basicGraph() {
    composable("loading") {
        LoadingPage()
    }
    composable("gallery") {
        GalleryPage()
    }
}

fun NavGraphBuilder.formGraph() {
    composable("button") {
        ButtonPage()
    }
    composable("checkbox") {
        CheckboxPage()
    }
    composable("radio") {
        RadioPage()
    }
    composable("switch") {
        SwitchPage()
    }
    composable("slider") {
        SliderPage()
    }
}

fun NavGraphBuilder.feedbackGraph() {
    composable("dialog") {
        DialogPage()
    }
    composable("popup") {
        PopupPage()
    }
    composable("action-sheet") {
        ActionSheetPage()
    }
}