package top.chengdongqing.weui.ui.components.videoplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.widget.VideoView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Stable
interface VideoPlayerState {
    /**
     * 视频容器实例
     */
    val videoView: VideoView

    /**
     * 是否准备好播放
     */
    val isPrepared: Boolean

    /**
     * 是否播放中
     */
    val isPlaying: Boolean

    /**
     * 是否静音
     */
    val isMute: Boolean

    /**
     * 总时长
     */
    val totalDuration: Int

    /**
     * 已播放时长
     */
    val currentDuration: Int

    /**
     * 当前进度百分比
     */
    val percent: Float

    /**
     * 播放
     */
    fun play()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 跳转到指定时长
     */
    fun seekTo(milliseconds: Int)

    /**
     * 设置静音
     * @param isMute 是否静音
     */
    fun setMute(isMute: Boolean)
}

@Composable
fun rememberVideoPlayerState(videoSource: Uri, isLooping: Boolean = true): VideoPlayerState {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }
    MediaPlayerLifecycle(videoView)
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val state = remember(videoSource) {
        VideoPlayerStateImpl(videoView, audioManager, videoSource, isLooping)
    }

    DisposableEffect(state) {
        onDispose {
            state.stopUpdatingProgress()
        }
    }

    return state
}

private class VideoPlayerStateImpl(
    override val videoView: VideoView,
    val audioManager: AudioManager,
    val videoSource: Uri,
    val isLooping: Boolean
) : VideoPlayerState {
    override val isPrepared: Boolean
        get() = _isPrepared
    override val isPlaying: Boolean
        get() = _isPlaying
    override val isMute: Boolean
        get() = _isMute
    override val totalDuration: Int
        get() = _totalDuration
    override val currentDuration: Int
        get() = _currentDuration
    override val percent: Float
        get() = _percent

    override fun play() {
        if (!videoView.isPlaying) {
            videoView.start()
            updateProgress()
        }
        _isPlaying = true
    }

    override fun pause() {
        if (videoView.isPlaying) {
            videoView.pause()
            stopUpdatingProgress()
        }
        _isPlaying = false
    }

    override fun seekTo(milliseconds: Int) {
        if (milliseconds <= _totalDuration) {
            videoView.seekTo(milliseconds)
            if (!videoView.isPlaying) {
                play()
            }
            _percent = calculatePercent(milliseconds)
        }
    }

    override fun setMute(isMute: Boolean) {
        val direction = if (isMute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            direction,
            0
        )
        _isMute = isMute
    }

    private fun updateProgress() {
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                _currentDuration = videoView.currentPosition
                _percent = calculatePercent(_currentDuration)
                delay(500)
            }
        }
    }

    fun stopUpdatingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun calculatePercent(milliseconds: Int): Float {
        return if (_totalDuration > 0) {
            milliseconds.toFloat() / _totalDuration * 100
        } else {
            0f
        }
    }

    init {
        videoView.apply {
            setVideoURI(videoSource)
            setOnPreparedListener {
                _isPrepared = true
                _totalDuration = it.duration
                it.isLooping = isLooping
                play()
            }
            setOnCompletionListener {
                _isPlaying = false
            }
        }
    }

    private var _isPrepared by mutableStateOf(false)
    private var _isPlaying by mutableStateOf(videoView.isPlaying)
    private var _isMute by mutableStateOf(false)
    private var _totalDuration by mutableIntStateOf(0)
    private var _currentDuration by mutableIntStateOf(0)
    private var _percent by mutableFloatStateOf(0f)
    private var progressJob: Job? = null
}

@Composable
private fun MediaPlayerLifecycle(videoView: VideoView) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousPlayingState = remember { mutableStateOf(false) }

    DisposableEffect(videoView) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    previousPlayingState.value = videoView.isPlaying
                    if (videoView.isPlaying) {
                        videoView.pause()
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    if (previousPlayingState.value) {
                        videoView.start()
                    }
                }

                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}