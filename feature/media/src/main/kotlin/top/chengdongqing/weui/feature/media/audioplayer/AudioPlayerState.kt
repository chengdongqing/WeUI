package top.chengdongqing.weui.feature.media.audioplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Stable
interface AudioPlayerState {
    /**
     * 是否播放中
     */
    val isPlaying: Boolean

    /**
     * 是否准备就绪
     */
    val isPrepared: Boolean

    /**
     * 总时长
     */
    val durationMs: Int

    /**
     * 已播放时长
     */
    val positionMs: Int

    /**
     * 进度（0.0-1.0）
     */
    val progress: Float get() = if (durationMs > 0) positionMs.toFloat() else 0f

    /**
     * 设置音源
     */
    fun setSource(context: Context, uri: Uri)

    /**
     * 开始播放
     */
    fun play()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 切换
     */
    fun toggle()

    /**
     * 跳转到指定时长
     */
    fun seekTo(milliseconds: Int)

    /**
     * 释放
     */
    fun release()
}

@Composable
fun rememberAudioPlayerState(uri: Uri): AudioPlayerState {
    val state = rememberAudioPlayerState()
    val context = LocalContext.current.applicationContext

    LaunchedEffect(uri) {
        state.setSource(context, uri)
    }

    return state
}

@Composable
fun rememberAudioPlayerState(): AudioPlayerState {
    val coroutineScope = rememberCoroutineScope()

    val player = remember {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    val state = remember(player) {
        AudioPlayerStateImpl(player, coroutineScope)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> state.pause()
                Lifecycle.Event.ON_DESTROY -> state.release()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            state.release()
        }
    }

    return state
}

private class AudioPlayerStateImpl(
    private val player: MediaPlayer,
    private val scope: CoroutineScope
) : AudioPlayerState {
    override val isPlaying: Boolean
        get() = _isPlaying
    override var isPrepared by mutableStateOf(false)
    override var durationMs by mutableIntStateOf(0)
    override var positionMs by mutableIntStateOf(0)

    override fun setSource(context: Context, uri: Uri) {
        safeReset {
            player.setDataSource(context, uri)
            prepareAsync()
        }
    }

    private fun prepareAsync() {
        isPrepared = false
        player.apply {
            setOnPreparedListener {
                isPrepared = true
                durationMs = it.duration
            }
            setOnCompletionListener {
                _isPlaying = false
                stopProgressUpdate()
                positionMs = durationMs
            }
            setOnErrorListener { _, _, _ ->
                isPrepared = false
                _isPlaying = false
                false
            }
            prepareAsync()
        }
    }

    override fun play() {
        if (isPrepared && !player.isPlaying) {
            player.start()
            _isPlaying = true
            startProgressUpdate()
        }
    }

    override fun pause() {
        if (player.isPlaying) {
            player.pause()
            _isPlaying = false
            stopProgressUpdate()
        }
    }

    override fun toggle() {
        if (isPlaying) pause() else play()
    }

    override fun seekTo(milliseconds: Int) {
        if (isPrepared && milliseconds <= durationMs) {
            player.seekTo(milliseconds)
            positionMs = milliseconds
        }
    }

    override fun release() {
        stopProgressUpdate()
        player.release()
    }

    private fun safeReset(action: () -> Unit) {
        stopProgressUpdate()
        player.reset()
        _isPlaying = false
        isPrepared = false
        positionMs = 0
        durationMs = 0
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startProgressUpdate() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                if (isPlaying) {
                    positionMs = player.currentPosition
                }
                delay(30)
            }
        }
    }

    private fun stopProgressUpdate() {
        progressJob?.cancel()
        progressJob = null
    }

    private var _isPlaying by mutableStateOf(false)
    private var progressJob: Job? = null
}
