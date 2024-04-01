package top.chengdongqing.weui.core.ui.components.camera

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.core.data.model.VisualMediaType

@Composable
internal fun ControlBar(state: CameraState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = !state.isUsingFrontCamera,
            onClick = { state.toggleFlash() },
            modifier = Modifier.alpha(if (state.isUsingFrontCamera) 0f else 1f)
        ) {
            Icon(
                imageVector = if (state.isFlashOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                contentDescription = "开关闪光灯",
                tint = Color.White
            )
        }
        CaptureButton(state)
        IconButton(onClick = {
            state.switchCamera()
        }) {
            Icon(
                Icons.Filled.FlipCameraAndroid,
                contentDescription = "切换相机",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CaptureButton(state: CameraState) {
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        if (state.type != VisualMediaType.IMAGE) {
                            if (audioPermissionState.status.isGranted) {
                                state.startRecording()
                            } else {
                                audioPermissionState.launchPermissionRequest()
                            }
                        }
                    },
                    onPress = {
                        awaitRelease()
                        if (state.isRecording) {
                            state.stopRecording()
                        }
                    }
                ) {
                    if (state.type != VisualMediaType.VIDEO) {
                        state.takePhoto()
                    }
                }
            }
    ) {
        if (!state.isRecording) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, Color.White, CircleShape)
                    .padding(8.dp)
                    .background(Color.White, CircleShape)
            )
        } else {
            val borderColor = Color.LightGray
            val activeColor = MaterialTheme.colorScheme.primary

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .drawBehind {
                        val strokeWidth = 6.dp.toPx()
                        drawCircle(
                            color = borderColor,
                            radius = size.width / 2,
                            style = Stroke(width = strokeWidth)
                        )
                        drawArc(
                            color = activeColor,
                            startAngle = -90f,
                            sweepAngle = 360 * state.videoProgress,
                            useCenter = false,
                            style = Stroke(strokeWidth)
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}