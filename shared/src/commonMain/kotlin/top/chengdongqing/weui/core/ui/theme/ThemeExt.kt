package top.chengdongqing.weui.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * 设置状态栏字体颜色
 */
@Composable
expect fun StatusBarAppearanceEffect(isDark: Boolean)

/**
 * 获取状态栏高度
 */
@Composable
expect fun rememberStatusBarHeight(): Dp