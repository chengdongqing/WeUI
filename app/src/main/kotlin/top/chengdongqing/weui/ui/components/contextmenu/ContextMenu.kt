package top.chengdongqing.weui.ui.components.contextmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.theme.FontColorLight
import top.chengdongqing.weui.utils.toIntOffset

@Composable
fun WeContextMenu(options: List<String>) {
    var showPopup by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(IntOffset.Zero) }

    LazyColumn(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    offset = it.toIntOffset()
                    showPopup = true
                })
            }
    ) {
        items(20) { index ->
            ListItem("item${index + 1}")
        }
    }

    if (showPopup) {
        Popup(offset = offset) {
            Column(
                modifier = Modifier
                    .width(180.dp)
                    .shadow(8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White)
            ) {
                options.forEach {
                    MenuItem(onClick = {}) {
                        Text(text = it)
                    }
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = content,
            modifier = Modifier.weight(1f),
            color = FontColorLight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
    WeDivider()
}

@Composable
private fun MenuItem(onClick: () -> Unit, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}