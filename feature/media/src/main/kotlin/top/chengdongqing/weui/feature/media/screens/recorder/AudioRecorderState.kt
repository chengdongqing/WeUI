package top.chengdongqing.weui.feature.media.screens.recorder

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
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.utils.MediaStoreUtils

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
    fun stop()
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
    override val isRecording: Boolean
        get() = _isRecording
    override val duration: Int
        get() = _duration

    override fun start() {
        prepare(context)
        // 重置计时
        _duration = 0
        // 开始录音
        recorder.start()
        _isRecording = true
        // 开始计时
        coroutineScope.launch {
            while (isActive && _isRecording) {
                delay(1000)
                _duration += 1
            }
        }
    }

    override fun stop() {
        // 结束录音
        recorder.stop()
        _isRecording = false
        // 更新文件状态
        _uri?.let {
            MediaStoreUtils.finishPending(it, context)
        }
    }

    private fun prepare(context: Context) {
        // 初始化参数
        recorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
        }

        // 创建媒体文件
        val contentUri = MediaStoreUtils.getContentUri(MediaType.RECORDING)
        val contentValues = MediaStoreUtils.createContentValues(
            filename = "RCD_${System.currentTimeMillis()}.mp3",
            mediaType = MediaType.RECORDING,
            mimeType = "audio/mp3",
            context
        )
        context.contentResolver.apply {
            insert(contentUri, contentValues)?.let { uri ->
                openFileDescriptor(uri, "w")?.use {
                    recorder.setOutputFile(it.fileDescriptor)
                    recorder.prepare()
                    _uri = uri
                }
            }
        }
    }

    private var _isRecording by mutableStateOf(false)
    private var _duration by mutableIntStateOf(0)
    private var _uri by mutableStateOf<Uri?>(null)
}