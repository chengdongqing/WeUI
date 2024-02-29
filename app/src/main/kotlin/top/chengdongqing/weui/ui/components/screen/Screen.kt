package top.chengdongqing.weui.ui.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColorDark
import top.chengdongqing.weui.ui.theme.FontColorLight
import top.chengdongqing.weui.ui.theme.FontSecondaryColorDark
import top.chengdongqing.weui.ui.theme.FontSecondaryColorLight

/**
 * 页面组件
 *
 * @param title 标题
 * @param description 描述
 * @param padding 内边距
 * @param containerColor 背景颜色
 * @param scrollEnabled 是否启用滚动
 * @param horizontalAlignment 横向对齐方式
 * @param verticalArrangement 竖向排列方式
 * @param content 内容
 */
@Composable
fun WeScreen(
    title: String,
    description: String,
    padding: PaddingValues = PaddingValues(16.dp),
    containerColor: Color = MaterialTheme.colorScheme.background,
    scrollEnabled: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(containerColor)
            .statusBarsPadding()
            .then(
                if (scrollEnabled) {
                    Modifier.verticalScroll(rememberScrollState())
                } else {
                    Modifier
                }
            )
    ) {
        Column(Modifier.padding(40.dp)) {
            Text(
                text = title,
                color = defaultColors.titleColor,
                fontSize = 20.sp,
                lineHeight = 32.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                color = defaultColors.descriptionColor,
                fontSize = 14.sp
            )
        }
        Spacer(Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    }
}

private data class ScreenColors(
    val titleColor: Color = FontColorLight,
    val descriptionColor: Color = FontSecondaryColorLight
)

private val defaultColors: ScreenColors
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            ScreenColors(
                titleColor = FontColorDark,
                descriptionColor = FontSecondaryColorDark
            )
        } else {
            ScreenColors()
        }
    }