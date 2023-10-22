package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.feedback.DialogPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeUITheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomePage(navController = navController)
                    }
                    composable("dialog") {
                        DialogPage()
                    }
                }
            }
        }
    }
}

