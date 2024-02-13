package top.chengdongqing.weui.ui.views.media.recorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.views.media.camera.RequestCameraPermission
import top.chengdongqing.weui.utils.MediaStoreUtils
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.rememberToggle

@Composable
fun VideoRecorderPage(navController: NavController) {
    RequestCameraPermission(
        navController,
        permissions = listOf(Manifest.permission.RECORD_AUDIO)
    ) {
        VideoRecorder()
    }
}

@Composable
private fun VideoRecorder() {
    val context = LocalContext.current
    val (camera, setCamera) = remember { mutableStateOf<Camera?>(null) }
    val (recording, setRecorder) = remember { mutableStateOf<Recording?>(null) }
    val videoCapture = remember {
        VideoCapture.withOutput(Recorder.Builder().build())
    }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val (isFlashOn, toggleFlash) = rememberToggle(
        defaultValue = false,
        reverseValue = true
    )
    val (_, toggleCameraSelector) = rememberToggle(
        CameraSelector.DEFAULT_BACK_CAMERA,
        CameraSelector.DEFAULT_FRONT_CAMERA
    )

    val coroutineScope = rememberCoroutineScope()
    Column {
        CameraView(videoCapture, cameraSelector, setCamera)
        ControlBar(isFlashOn,
            onTakeVideo = {
                takeVideo(context, videoCapture, setRecorder)
                coroutineScope.launch {
                    delay(6000)
                    recording?.stop()
                }
            },
            onToggleFlash = {
                camera?.let {
                    if (camera.cameraInfo.hasFlashUnit()) {
                        camera.cameraControl.enableTorch(toggleFlash())
                    }
                }
            },
            onSwitchCamera = {
                cameraSelector = toggleCameraSelector()
            })
    }
}

@Composable
private fun ColumnScope.CameraView(
    videoCapture: VideoCapture<Recorder>,
    cameraSelector: CameraSelector,
    setCamera: (Camera) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    AndroidView(
        factory = { context ->
            cameraProvider = ProcessCameraProvider.getInstance(context).get()
            val previewView = PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                keepScreenOn = true
            }
            previewView
        },
        modifier = Modifier.weight(1f),
        update = { previewView ->
            try {
                val preview = Preview.Builder().build().also { preview ->
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                }
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )?.also(setCamera)
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
    )
}

@Composable
private fun ControlBar(
    isFlashOn: Boolean,
    onToggleFlash: () -> Unit,
    onTakeVideo: () -> Unit,
    onSwitchCamera: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onToggleFlash) {
            Icon(
                if (isFlashOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                contentDescription = "开关闪光灯",
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .size(80.dp)
                .clickable(onClickLabel = "拍照", onClick = onTakeVideo)
                .border(2.dp, Color.White, CircleShape)
                .padding(8.dp)
                .background(Color.Red, CircleShape)
        )
        IconButton(onClick = onSwitchCamera) {
            Icon(
                Icons.Filled.FlipCameraAndroid,
                contentDescription = "切换相机",
                tint = Color.White
            )
        }
    }
}

@SuppressLint("MissingPermission")
private fun takeVideo(
    context: Context,
    videoCapture: VideoCapture<Recorder>,
    setVideoRecording: (Recording) -> Unit
) {
    val contentUri = MediaStoreUtils.getContentUri(MediaType.VIDEO)
    val contentValues = MediaStoreUtils.createContentValues(
        filename = "VID_${System.currentTimeMillis()}.mp4",
        mimeType = "video/mp4",
        mediaType = MediaType.VIDEO,
        context
    )
    val outputOptions = MediaStoreOutputOptions
        .Builder(context.contentResolver, contentUri)
        .setContentValues(contentValues)
        .build()

    videoCapture.output.prepareRecording(context, outputOptions)
        .withAudioEnabled()
        .start(ContextCompat.getMainExecutor(context)) { event ->
            when (event) {
                is VideoRecordEvent.Start -> {
                    Toast.makeText(context, "开始录制", Toast.LENGTH_SHORT).show()
                }

                is VideoRecordEvent.Status -> {
                    println("recordedDurationNanos: " + event.recordingStats.recordedDurationNanos)
                }

                is VideoRecordEvent.Finalize -> {
                    if (event.hasError()) {
                        println("recordedDurationNanos: " + event.recordingStats.recordedDurationNanos)
                        event.cause?.printStackTrace()
                        Toast.makeText(context, "录制失败", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "视频已保存", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.also(setVideoRecording)
}