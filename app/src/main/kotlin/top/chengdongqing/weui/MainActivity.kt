package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.core.utils.clearAllCache
import top.chengdongqing.weui.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        clearAllCache()

        setContent {
            WeUITheme {
                AppNavHost()
            }
        }
    }
}
