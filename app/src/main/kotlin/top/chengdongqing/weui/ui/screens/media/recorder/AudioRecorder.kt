package top.chengdongqing.weui.ui.screens.media.recorder

import android.Manifest
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState
import top.chengdongqing.weui.utils.format
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioRecorderScreen() {
    WeScreen(title = "AudioRecorder", description = "音频录制") {
        val permissionState = rememberMultiplePermissionsState(
            buildList {
                add(Manifest.permission.RECORD_AUDIO)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        )
        val state = rememberAudioRecorderState()
        val toast = rememberToastState()

        Text(
            text = state.duration.seconds.format(isFull = true),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(60.dp))
        Box(
            modifier = Modifier.size(84.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable {
                        if (permissionState.allPermissionsGranted) {
                            if (state.isRecording) {
                                state.stop()
                                toast.show("录音已保存", ToastIcon.SUCCESS)
                            } else {
                                state.start()
                            }
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                    .padding(12.dp)
            ) {
                RecordIcon(state.isRecording)
            }
        }
    }
}

@Composable
private fun RecordIcon(isRecording: Boolean) {
    if (!isRecording) {
        Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = "开始录音",
            modifier = Modifier.size(50.dp),
            tint = MaterialTheme.colorScheme.onSecondary
        )
    } else {
        val transition = rememberInfiniteTransition(label = "")
        val animatedSize by transition.animateFloat(
            initialValue = 30f,
            targetValue = 40f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1500, easing = LinearEasing),
                RepeatMode.Reverse
            ),
            label = "AudioRecorderStopIconAnimation"
        )

        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = "停止录音",
            modifier = Modifier.size(animatedSize.dp),
            tint = Color.Red
        )
    }
}