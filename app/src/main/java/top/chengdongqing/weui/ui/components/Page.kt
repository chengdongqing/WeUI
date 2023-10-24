package top.chengdongqing.weui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.BackgroundColor
import top.chengdongqing.weui.ui.theme.FontColor

@Composable
fun Page(title: String, description: String, bgColor: Color = BackgroundColor, content: @Composable () -> Unit) {
    Column(
        Modifier
            .background(bgColor)
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
            Text(text = description, color = Color(0f, 0f, 0f, 0.55f), fontSize = 14.sp)
        }

        Spacer(Modifier.height(30.dp))

        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            content()
        }
    }
}