package top.chengdongqing.weui.theme

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat

@Composable
actual fun StatusBarAppearanceEffect(isDark: Boolean) {
    val view = LocalView.current
    val window = LocalActivity.current?.window ?: return
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
actual fun rememberStatusBarHeight(): Dp {
    val density = LocalDensity.current
    val statusBars = WindowInsets.statusBars

    return remember {
        with(density) {
            statusBars.getTop(this).toDp()
        }
    }
}