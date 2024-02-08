package top.chengdongqing.weui.ui.views.media.recorder

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.utils.formatDuration
import java.io.File
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioRecorderPage() {
    WePage(title = "AudioRecorder", description = "音频录制") {
        val permissionState = rememberMultiplePermissionsState(
            buildList {
                add(Manifest.permission.RECORD_AUDIO)
                // 安卓Q及以上版本用MediaStore不需要写权限
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        )
        val (isRecording, setRecording) = remember { mutableStateOf(false) }
        val duration = rememberAudioRecord(isRecording)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatDuration(duration, true),
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
                        .background(Color.White)
                        .clickable {
                            if (permissionState.allPermissionsGranted) {
                                setRecording(!isRecording)
                            } else {
                                permissionState.launchMultiplePermissionRequest()
                            }
                        }
                        .padding(12.dp)
                ) {
                    RecordIcon(isRecording)
                }
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
            tint = FontColor1
        )
    } else {
        val animatedSize by rememberInfiniteTransition("AudioRecorderStopIconTransition").animateFloat(
            initialValue = 30f,
            targetValue = 40f,
            animationSpec = remember {
                infiniteRepeatable(
                    tween(durationMillis = 1500, easing = LinearEasing),
                    RepeatMode.Reverse
                )
            },
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

@Composable
private fun rememberAudioRecord(isRecording: Boolean): Duration {
    val (timer, setTimer) = remember { mutableStateOf<Timer?>(null) }
    val (duration, setDuration) = remember { mutableStateOf(0.seconds) }
    val latestDuration = rememberUpdatedState(newValue = duration)
    val (mediaRecorder, setMediaRecorder) = remember { mutableStateOf<MediaRecorder?>(null) }
    val context = LocalContext.current
    val toast = rememberWeToast()

    fun stopRecording() {
        timer?.apply {
            cancel()
            setTimer(null)
        }
        mediaRecorder?.apply {
            stop()
            release()
            setMediaRecorder(null)
        }
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            // 开始录音
            setDuration(0.seconds)
            setTimer(Timer().also {
                it.schedule(timerTask {
                    setDuration(latestDuration.value + 1.seconds)
                }, 1000, 1000)
            })
            setMediaRecorder(buildAudioRecorder(context).apply {
                start()
            })
        } else if (timer != null) {
            // 结束录音
            stopRecording()
            toast.show("录音已保存", ToastIcon.SUCCESS)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopRecording()
        }
    }

    return duration
}

private fun buildAudioRecorder(context: Context): MediaRecorder {
    val audioRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MediaRecorder(context)
    } else {
        @Suppress("DEPRECATION")
        MediaRecorder()
    }

    audioRecorder.apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
    }

    val filename = "recording_${System.currentTimeMillis()}.mp3"
    val appName = context.getString(R.string.app_name)
    val relativePath = "Recordings/$appName"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }

        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        context.contentResolver.insert(contentUri, values)?.let { uri ->
            context.contentResolver.openFileDescriptor(uri, "w")?.use {
                audioRecorder.setOutputFile(it.fileDescriptor)
                audioRecorder.prepare()
            }
        }
    } else {
        val directory = File(Environment.getExternalStorageDirectory(), relativePath).apply {
            mkdirs()
        }
        val file = File(directory, filename).apply {
            createNewFile()
        }
        audioRecorder.setOutputFile(file)
        audioRecorder.prepare()
    }

    return audioRecorder
}