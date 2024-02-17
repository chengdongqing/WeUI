package top.chengdongqing.weui.ui.components.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.FontColor1

/**
 * 页面组件
 *
 * @param title 标题
 * @param description 描述
 * @param padding 内边距
 * @param backgroundColor 背景颜色
 * @param content 内容
 */
@Composable
fun WePage(
    title: String,
    description: String,
    padding: PaddingValues = PaddingValues(16.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    Column(
        Modifier
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        Column(Modifier.padding(40.dp)) {
            Text(
                text = title,
                color = FontColor,
                fontSize = 20.sp,
                lineHeight = 32.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(text = description, color = FontColor1, fontSize = 14.sp)
        }

        Spacer(Modifier.height(30.dp))

        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            content()
        }
    }
}