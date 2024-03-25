package top.chengdongqing.weui.feature.media.camera

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
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
import androidx.compose.ui.platform.LocalLifecycleOwner
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
        val state = rememberCameraState(type)

        Column {
            CameraPreview(state)
            ControlBar(state, onCapture)
        }
    }
}

@Composable
private fun ColumnScope.CameraPreview(state: CameraState) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraSelector = if (state.isUsingFrontCamera) {
        CameraSelector.DEFAULT_FRONT_CAMERA
    } else {
        CameraSelector.DEFAULT_BACK_CAMERA
    }

    Box(modifier = Modifier.weight(1f)) {
        AndroidView(
            factory = { state.previewView },
            modifier = Modifier.fillMaxSize()
        ) { previewView ->
            try {
                val preview = Preview.Builder().build().also { preview ->
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                }
                state.cameraProvider.unbindAll()
                state.camera = state.cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    state.imageCapture,
                    state.videoCapture
                )
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }

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