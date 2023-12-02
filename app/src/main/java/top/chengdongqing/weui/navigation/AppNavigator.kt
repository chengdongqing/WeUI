package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.basic.MediaPickPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.feedback.PopupPage
import top.chengdongqing.weui.ui.views.form.*

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
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
                HomePage()
            }
            addFormRoutes()
            addBasicRoutes()
            addFeedbackRoutes()
        }
    }
}

fun NavGraphBuilder.addBasicRoutes() {
    composable("loading") {
        LoadingPage()
    }
    composable("mediaPicker") {
        MediaPickPage()
    }
}

fun NavGraphBuilder.addFormRoutes() {
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

fun NavGraphBuilder.addFeedbackRoutes() {
    composable("dialog") {
        DialogPage()
    }
    composable("popup") {
        PopupPage()
    }
}