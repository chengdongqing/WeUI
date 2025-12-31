package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * 这是一个系统提供的自动优化全面屏的工具
         * 1.自动将内容区域延伸到屏幕边缘
         * 2.自动将状态栏和导航栏背景设置为透明
         * 3.动态调整状态栏的文字颜色等
         */
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            WeUITheme {
                AppNavHost()
            }
        }
    }
}
