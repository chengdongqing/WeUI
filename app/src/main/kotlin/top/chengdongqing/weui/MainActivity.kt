package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.navigation.ApplicationNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeUITheme {
                ApplicationNavHost()
            }
        }
    }
}
