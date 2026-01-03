package top.chengdongqing.weui.feature.media.imagecropper

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Rotate90DegreesCcw
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.WeButton

@Composable
internal fun BoxScope.ActionBar(
    onRotate: () -> Unit,
    onReset: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "取消",
            color = Color.White,
            modifier = Modifier.clickable { onCancel() }
        )
        IconButton(onReset) {
            Icon(Icons.Outlined.Replay, "恢复", tint = Color.White)
        }
        IconButton(onRotate) {
            Icon(Icons.Outlined.Rotate90DegreesCcw, "旋转", tint = Color.White)
        }
        WeButton(text = "确定", size = ButtonSize.SMALL) {
            onConfirm()
        }
    }
}
