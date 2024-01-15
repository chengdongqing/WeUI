package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.WeContextMenu
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColor

@Composable
fun ContextMenuPage() {
    Page(title = "ContextMenu", description = "上下文菜单") {
        WeContextMenu(listOf({
            Text(text = "菜单1")
        }, {
            Text(text = "菜单2")
        }, {
            Text(text = "菜单3")
        }, {
            Text(text = "菜单4")
        })) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                repeat(20) {
                    ListItem("item${it + 1}")
                }
            }
        }
    }
}

@Composable
private fun ListItem(content: String) {
    Row(
        modifier = Modifier
            .heightIn(56.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        println("456")
                    }
                ) {
                    println("123")
                }
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = content,
            modifier = Modifier.weight(1f),
            color = FontColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Divider(thickness = 0.5.dp, color = BorderColor)
}