package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import top.chengdongqing.weui.ui.views.form.ButtonPage
import top.chengdongqing.weui.ui.views.form.CheckboxPage

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeUITheme {
                val navController = rememberNavController()

                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(navController = navController, startDestination = "checkbox") {
                        composable("home") {
                            HomePage()
                        }
                        composable("button") {
                            ButtonPage()
                        }
                        composable("checkbox") {
                            CheckboxPage()
                        }
                        composable("loading") {
                            LoadingPage()
                        }
                        composable("dialog") {
                            DialogPage()
                        }
                    }
                }
            }
        }
    }
}

