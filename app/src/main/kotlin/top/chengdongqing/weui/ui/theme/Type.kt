package top.chengdongqing.weui.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

/**
 *
 * 各种文本样式及其含义：
 *
 * displayLarge：最大的显示文本，通常用于突出显示重要信息或吸引注意力。
 * displayMedium：第二大的显示文本，也用于突出显示重要信息，但相对较小。
 * displaySmall：最小的显示文本，用于较小的标题或信息。
 * headlineLarge：最大的标题，用于突出显示短而重要的文本或数字，可以选择有表现力的字体。
 * headlineMedium：第二大的标题，用于突出显示短而重要的文本或数字，字体相对较小。
 * headlineSmall：最小的标题，用于突出显示短而重要的文本或数字，字体相对较小。
 * titleLarge：最大的标题，用于中等重要性的文本，长度较短。
 * titleMedium：第二大的标题，用于中等重要性的文本，长度较短。
 * titleSmall：最小的标题，用于中等重要性的文本，长度较短。
 * bodyLarge：最大的正文，用于长篇文章，适合小字体。
 * bodyMedium：第二大的正文，用于长篇文章，适合小字体。
 * bodySmall：最小的正文，用于长篇文章，适合小字体。
 * labelLarge：用于按钮、标签等，呼吁行动的较大文本。
 * labelMedium：较小的字体，用于标注图像或引入标题。
 * labelSmall：最小的字体，用于标注图像或引入标题。
 */