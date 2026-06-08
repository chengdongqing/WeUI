package top.chengdongqing.weui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.Font
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.noto_sans_sc

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = FontColorDark,
    onSecondary = FontSecondaryColorDark,
    background = BackgroundColorDark,
    onBackground = OnBackgroundColorDark,
    surface = Color.Black,
    onSurface = BackgroundColorDark,
    error = DangerColorDark,
    errorContainer = OnBackgroundColorDark,
    outline = BorderColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = FontColorLight,
    onSecondary = FontSecondaryColorLight,
    background = BackgroundColorLight,
    onBackground = OnBackgroundColorLight,
    surface = Color.White,
    onSurface = BackgroundColorLight,
    error = DangerColorLight,
    errorContainer = Color(0xffFFFBE6),
    outline = BorderColorLight
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

    val fontFamily = FontFamily(Font(Res.font.noto_sans_sc))

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(
                fontFamily = fontFamily
            )
        ) {
            Box(
                // 仅在经典导航键的情况下加底部导航栏边距
                Modifier.run {
                    if (isClassicNavigationMode())
                        navigationBarsPadding()
                    else this
                }
            ) {
                content()
            }
        }
    }
}

/**
 * 是否为经典导航键模式
 */
@Composable
fun isClassicNavigationMode(): Boolean {
    val navInsets = WindowInsets.navigationBars
    val density = LocalDensity.current

    val isClassicMode by remember {
        derivedStateOf {
            val height = with(density) { navInsets.getBottom(density).toDp() }
            height > 30.dp
        }
    }

    return isClassicMode
}