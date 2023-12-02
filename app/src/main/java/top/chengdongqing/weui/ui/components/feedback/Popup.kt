package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

enum class PopupDirection {
    LEFT,
    RIGHT,
    BOTTOM,
    TOP,
    CENTER
}

@Composable
fun WePopup(
    visible: Boolean,
    direction: PopupDirection = PopupDirection.BOTTOM,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    if (visible) {
        Popup(
            alignment = Alignment.BottomStart,
            onDismissRequest = onClose
        ) {
            Box(
                Modifier
                    .height(450.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp, 12.dp))
                    .background(Color.White)
            ) {
                content()
            }
        }
    }
}
