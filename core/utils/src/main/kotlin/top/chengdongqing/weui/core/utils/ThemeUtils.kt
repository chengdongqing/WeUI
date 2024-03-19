package top.chengdongqing.weui.core.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetupStatusBarStyle(isDark: Boolean = true) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = remember { WindowCompat.getInsetsController(window, view) }
    val initialStyle = remember { insetsController.isAppearanceLightStatusBars }

    DisposableEffect(isDark) {
        insetsController.isAppearanceLightStatusBars = isDark

        onDispose {
            insetsController.isAppearanceLightStatusBars = initialStyle
        }
    }
}

@Composable
fun SetupFullscreen(isFullscreen: Boolean = true) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = remember { WindowCompat.getInsetsController(window, view) }

    DisposableEffect(Unit) {
        if (isFullscreen) {
            // 让状态栏区域可用于布局
            WindowCompat.setDecorFitsSystemWindows(window, false)
            // 隐藏系统状态栏、导航栏等
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            // 当手动将状态栏或导航栏滑出后只短暂显示一会儿就收起
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }

        onDispose {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}