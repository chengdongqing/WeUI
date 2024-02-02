package top.chengdongqing.weui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetupStatusBarStyle(isDark: Boolean = true) {
    val view = LocalView.current
    val window = (view.context as Activity).window

    SideEffect {
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDark
    }
}

@Composable
fun SetupFullscreen() {
    val view = LocalView.current
    val window = (view.context as Activity).window

    DisposableEffect(Unit) {
        // 让状态栏区域可用于布局
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowCompat.getInsetsController(window, view)
        // 隐藏系统状态栏、导航栏等
        controller.hide(WindowInsetsCompat.Type.systemBars())
        // 当手动将状态栏或导航栏滑出后只短暂显示一会儿就收起
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}