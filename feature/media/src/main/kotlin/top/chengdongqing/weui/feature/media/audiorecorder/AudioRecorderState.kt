package top.chengdongqing.weui.feature.media.audiorecorder

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.utils.getFileProviderUri
import java.io.File

@Stable
interface AudioRecorderState {
    /**
     * 是否在录制中
     */
    val isRecording: Boolean

    /**
     * 已录制的时长
     */
    val duration: Int

    /**
     * 开始录音
     */
    fun start()

    /**
     * 结束录音
     */
    fun stop(): Uri
}

@Composable
fun rememberAudioRecorderState(): AudioRecorderState {
    val context = LocalContext.current
    val recorder = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val state = remember {
        AudioRecorderStateImpl(recorder, coroutineScope, context)
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder.release()
        }
    }

    return state
}

private class AudioRecorderStateImpl(
    val recorder: MediaRecorder,
    private val coroutineScope: CoroutineScope,
    private val context: Context
) : AudioRecorderState {
    override var isRecording by mutableStateOf(false)
    override var duration by mutableIntStateOf(0)

    override fun start() {
        prepare()
        // 重置计时
        duration = 0
        // 开始录音
        recorder.start()
        isRecording = true
        // 开始计时
        coroutineScope.launch {
            while (isActive && isRecording) {
                delay(1000)
                duration += 1
            }
        }
    }

    override fun stop(): Uri {
        recorder.stop()
        isRecording = false

        return context.getFileProviderUri(tempFile!!)
    }

    private fun prepare() {
        // 初始化参数
        recorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
        }

        // 保存到临时文件
        tempFile = File.createTempFile("RCD_", ".aac").apply {
            deleteOnExit()
        }
        recorder.setOutputFile(tempFile?.absolutePath)
        recorder.prepare()
    }

    private var tempFile: File? = null
}