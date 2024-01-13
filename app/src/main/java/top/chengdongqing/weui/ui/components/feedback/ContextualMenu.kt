package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup

@Composable
fun WeContextualMenu(options: List<@Composable () -> Unit>, content: @Composable () -> Unit) {
    var showPopup by remember { mutableStateOf(false) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    var elementSize by remember { mutableStateOf(Size.Zero) }

    Box(
        Modifier
            .onGloballyPositioned { coordinates ->
                elementSize = coordinates.size.toSize()
                popupPosition = coordinates.positionInRoot()

                println("elementSize: $elementSize")
                println("popupPosition: $popupPosition")
            }
    ) {
        content()

        if (showPopup) {
            Popup(offset = IntOffset(x = 0, y = 0)) {
                Column(
                    modifier = Modifier
                        .width(180.dp)
                        .shadow(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White)
                ) {
                    options.forEach {
                        MenuItem(onClick = {}) {
                            it()
                        }
                    }
                }
            }
        }
    }
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