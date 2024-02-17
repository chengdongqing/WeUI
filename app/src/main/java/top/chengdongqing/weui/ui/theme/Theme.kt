package top.chengdongqing.weui.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    background = BackgroundColorDark,
    onBackground = OnBackgroundColorDark,
    error = DangerColorDark,
    outline = BorderColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    background = BackgroundColorLight,
    onBackground = OnBackgroundColorLight,
    error = DangerColorLight,
    outline = BorderColor
)

@Composable
fun WeUITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * 各种颜色及其含义：
 *
 * - primary: 主色，在应用的各个屏幕和组件中最常出现的颜色。
 * - onPrimary: 主色的文本和图标颜色。
 * - primaryContainer: 容器的首选色调。
 * - onPrimaryContainer: 在 primaryContainer 上显示的内容的颜色。
 * - inversePrimary: 主色的反色，在需要反色方案的地方使用，例如 SnackBar 上的按钮。
 * - secondary: 次要颜色，用于强调和区分产品的不同部分。
 * - onSecondary: 次要颜色上显示的文本和图标的颜色。
 * - secondaryContainer: 容器的次要色调。
 * - onSecondaryContainer: 在 secondaryContainer 上显示的内容的颜色。
 * - tertiary: 第三颜色，用于平衡主要和次要颜色，或者突出显示元素，例如输入字段。
 * - onTertiary: 第三颜色上显示的文本和图标的颜色。
 * - tertiaryContainer: 容器的第三色调。
 * - onTertiaryContainer: 在 tertiaryContainer 上显示的内容的颜色。
 * - background: 可滚动内容背后的背景颜色。
 * - onBackground: 在背景颜色上显示的文本和图标的颜色。
 * - surface: 影响组件表面的颜色，例如卡片、表单和菜单。
 * - onSurface: 在 surface 上显示的文本和图标的颜色。
 * - surfaceVariant: 用于装饰元素边界的轻微颜色，当不需要强烈对比度时使用。
 * - inverseSurface: 与 surface 形成鲜明对比的颜色，用于位于其他 surface 颜色上的表面。
 * - inverseOnSurface: 与 inverseSurface 形成良好对比的颜色，用于位于 inverseSurface 上的容器上的内容。
 * - error: 错误颜色，用于指示组件中的错误，例如文本字段中的无效文本。
 * - onError: 在错误颜色上显示的文本和图标的颜色。
 * - errorContainer: 错误容器的首选色调。
 * - onErrorContainer: 在 errorContainer 上显示的内容的颜色。
 * - outline: 用于边界的微妙颜色。轮廓颜色角色为增加对比度，以提高可访问性。
 * - outlineVariant: 用于装饰元素边界的实用颜色，当不需要强烈对比度时使用。
 * - scrim: 用于遮挡内容的薄纱的颜色。
 * - surfaceBright: 始终比 surface 亮的 surface 变体，无论是在亮色模式还是暗色模式下。
 * - surfaceDim: 始终比 surface 暗的 surface 变体，无论是在亮色模式还是暗色模式下。
 * - surfaceContainer: 影响组件容器的 surface 变体，例如卡片、表单和菜单。
 * - surfaceContainerHigh: 比 surfaceContainer 更强调的容器的 surface 变体。用于需要比 surfaceContainer 更强调的内容。
 * - surfaceContainerHighest: 比 surfaceContainerHigh 更强调的容器的 surface 变体。用于需要比 surfaceContainerHigh 更强调的内容。
 * - surfaceContainerLow: 比 surfaceContainer 更低调的容器的 surface 变体。用于需要比 surfaceContainer 更低调的内容。
 * - surfaceContainerLowest: 比 surfaceContainerLow 更低调的容器的 surface 变体。用于需要比 surfaceContainerLow 更低调的内容。
 */