package top.chengdongqing.weui

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.navigation.AppNavigation

@Composable
fun App() {
    WeUITheme {
        AppNavigation()
    }
}