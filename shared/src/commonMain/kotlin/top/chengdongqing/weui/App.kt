package top.chengdongqing.weui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import top.chengdongqing.weui.navigation.AppNavigation
import top.chengdongqing.weui.theme.WeUITheme

@Composable
@Preview
fun App() {
    WeUITheme {
        AppNavigation()
    }
}