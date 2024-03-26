package top.chengdongqing.weui.core.ui.components.camera

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.utils.RequestCameraPermission

@Composable
fun WeCamera(
    type: VisualMediaType,
    onRevoked: () -> Unit,
    onCapture: (Uri, VisualMediaType) -> Unit
) {
    RequestCameraPermission(onRevoked = onRevoked) {
        val state = rememberCameraState(type, onCapture = onCapture)

        Column {
            CameraPreview(state)
            ControlBar(state)
        }
    }
}

@Composable
private fun ColumnScope.CameraPreview(state: CameraState) {
    Box(modifier = Modifier.weight(1f)) {
        AndroidView(
            factory = { state.previewView },
            modifier = Modifier.fillMaxSize(),
            update = { state.updateCamera() }
        )

        val tips = remember {
            buildList {
                if (state.type == VisualMediaType.IMAGE_AND_VIDEO || state.type == VisualMediaType.IMAGE) {
                    add("轻触拍照")
                }
                if (state.type == VisualMediaType.IMAGE_AND_VIDEO || state.type == VisualMediaType.VIDEO) {
                    add("长按摄像")
                }
            }.joinToString("，")
        }
        Text(
            text = tips,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
        )
    }
}