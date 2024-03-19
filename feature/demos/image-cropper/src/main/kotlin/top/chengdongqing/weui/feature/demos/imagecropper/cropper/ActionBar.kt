package top.chengdongqing.weui.feature.demos.imagecropper.cropper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Rotate90DegreesCcw
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.WeButton

@Composable
internal fun BoxScope.ActionBar(
    transform: MutableState<ImageTransform>,
    onChange: (ImageTransform) -> Unit
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "取消", color = Color.White, fontSize = 16.sp)
        IconButton(onClick = {
            onChange(ImageTransform())
        }) {
            Icon(
                imageVector = Icons.Outlined.Replay,
                contentDescription = "恢复",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        IconButton(onClick = {
            onChange(transform.value.copy(rotation = transform.value.rotation - 90f))
        }) {
            Icon(
                imageVector = Icons.Outlined.Rotate90DegreesCcw,
                contentDescription = "旋转",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        WeButton(text = "确定", size = ButtonSize.SMALL)
    }
}