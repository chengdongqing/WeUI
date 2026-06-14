package top.chengdongqing.weui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.theme.WeTheme
import top.chengdongqing.weui.util.AppPlatform
import top.chengdongqing.weui.util.getPlatform
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_back_circle_filled

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
    containerColor: Color = WeTheme.colorScheme.background,
    scrollEnabled: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor)
            .statusBarsPadding()
            .composed {
                if (scrollEnabled) {
                    this.verticalScroll(rememberScrollState())
                } else {
                    this
                }
            }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .padding(bottom = 40.dp, top = 10.dp)
        ) {
            if (getPlatform() != AppPlatform.Android) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back_circle_filled),
                    contentDescription = "go back",
                    modifier = Modifier.clickable(onClick = onBack),
                    tint = WeTheme.colorScheme.textPrimary
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                color = WeTheme.colorScheme.textPrimary,
                fontSize = 20.sp,
                lineHeight = 32.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                color = WeTheme.colorScheme.textSecondary,
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