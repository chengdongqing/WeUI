package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.noto_sans

@Composable
actual fun getFontFamily(): FontFamily? {
    return FontFamily(Font(Res.font.noto_sans))
}