package top.chengdongqing.weui

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.navigation.AppNavigation
import top.chengdongqing.weui.theme.WeUITheme

@Composable
fun App() {
    WeUITheme {
        AppNavigation()
    }
}