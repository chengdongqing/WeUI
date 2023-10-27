package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.feedback.PopupPage
import top.chengdongqing.weui.ui.views.form.*

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeUITheme {
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
                        composable("loading") {
                            LoadingPage()
                        }
                        composable("dialog") {
                            DialogPage()
                        }
                        composable("popup") {
                            PopupPage()
                        }
                    }
                }
            }
        }
    }
}
