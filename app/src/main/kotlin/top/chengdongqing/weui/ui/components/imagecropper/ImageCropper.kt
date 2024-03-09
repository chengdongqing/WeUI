package top.chengdongqing.weui.ui.components.imagecropper

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Rotate90DegreesCcw
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.theme.BackgroundColorDark
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.SetupStatusBarStyle

@Composable
fun WeImageCropper(uri: Uri, onChange: (Bitmap) -> Unit) {
    SetupStatusBarStyle(false)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .clipToBounds()
            .background(BackgroundColorDark),
        contentAlignment = Alignment.Center
    ) {
        CropperImage(uri)
        CropperMask()
        ActionBar()
    }
}

@Composable
private fun BoxScope.ActionBar() {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "取消", color = Color.White, fontSize = 17.sp)
        Icon(
            imageVector = Icons.Outlined.Replay,
            contentDescription = "恢复",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
        Icon(
            imageVector = Icons.Outlined.Rotate90DegreesCcw,
            contentDescription = "旋转",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
        WeButton(text = "确定", size = ButtonSize.SMALL)
    }
}

@Composable
private fun CropperImage(uri: Uri) {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotationZ by remember { mutableFloatStateOf(0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotationZ,
                translationX = offsetX,
                translationY = offsetY
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    scale = (scale * zoom).coerceIn(1f, 5f)
                    rotationZ += rotation
                    offsetX += pan.x
                    offsetY += pan.y
                }
            },
        contentScale = ContentScale.Fit
    )
}

@Preview
@Composable
private fun PreviewImageCropper() {
    WeUITheme {
        WeImageCropper(uri = Uri.parse("https://s1.xiaomiev.com/activity-outer-assets/web/su7/1-3_m.jpg")) {

        }
    }
}