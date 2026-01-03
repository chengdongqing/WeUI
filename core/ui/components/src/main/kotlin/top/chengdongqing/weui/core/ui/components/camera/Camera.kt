package top.chengdongqing.weui.core.ui.components.camera

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
            ) {
                CameraPreview(state)
                ControlBar(state)
            }
        }
    }
}

@Composable
private fun CameraPreview(state: CameraState) {
    AndroidView(
        factory = { state.previewView },
        modifier = Modifier.fillMaxSize(),
        update = { state.updateCamera() }
    )
}
