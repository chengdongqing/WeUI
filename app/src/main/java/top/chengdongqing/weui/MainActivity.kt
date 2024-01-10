package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import top.chengdongqing.weui.navigation.NavigationGraph
import top.chengdongqing.weui.ui.theme.WeUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeUITheme {
                Box(modifier = Modifier.navigationBarsPadding()) {
                    NavigationGraph()
                }
            }
        }
    }
}
