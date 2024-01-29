package top.chengdongqing.weui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetupStatusBarStyle(isDark: Boolean = true) {
    val view = LocalView.current
    val window = (view.context as Activity).window

    SideEffect {
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDark
    }
}