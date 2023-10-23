package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.form.ButtonPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeUITheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "loading") {
                    composable("home") {
                        HomePage(navController = navController)
                    }
                    composable("button") {
                        ButtonPage()
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

