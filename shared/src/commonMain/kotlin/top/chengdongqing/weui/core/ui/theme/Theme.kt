package top.chengdongqing.weui.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import top.chengdongqing.weui.util.AppPlatform
import top.chengdongqing.weui.util.getPlatform
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.noto_sans_sc

@Immutable
data class WeColorScheme(
    val primary: Color = GreenPrimary,
    val danger: Color = RedDanger,
    val link: Color = PurpleLink,
    // 背景层级（从低到高）
    val background: Color,       // 页面底色
    val surface: Color,          // 卡片/列表容器
    val surfaceVariant: Color,   // 输入框/次级容器
    val elevated: Color,         // 浮层/弹窗
    // 文本层级
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,     // 时间戳、占位符等
    // 其他
    val divider: Color,          // 分隔线
)

val LightColorScheme = WeColorScheme(
    background = Grey_ED,
    surface = Color.White,
    surfaceVariant = Grey_F7,
    elevated = Color.White,
    textPrimary = TextPrimaryLight,
    textSecondary = TextSecondaryLight,
    textTertiary = TextTertiaryLight,
    divider = DividerLight,
)

val DarkColorScheme = WeColorScheme(
    background = Dark_BG,
    surface = Dark_Surface,
    surfaceVariant = Dark_Surface2,
    elevated = Dark_Elevated,
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textTertiary = TextTertiaryDark,
    divider = DividerDark,
)

val LocalColorScheme = staticCompositionLocalOf { LightColorScheme }

@Composable
fun WeUITheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // 颜色方案
    val colorScheme = when {
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }
    // 字体配置（仅web端需要）
    val fontFamily = if (getPlatform() == AppPlatform.Web) {
        FontFamily(Font(Res.font.noto_sans_sc))
    } else {
        null
    }

    MaterialTheme {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(
                fontFamily = fontFamily
            ),
            LocalColorScheme provides colorScheme
        ) {
            content()
        }
    }
}

object WeTheme {
    val colorScheme: WeColorScheme
        @Composable
        get() = LocalColorScheme.current
}