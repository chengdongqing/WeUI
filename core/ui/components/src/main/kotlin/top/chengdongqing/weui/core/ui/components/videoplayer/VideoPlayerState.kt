package top.chengdongqing.weui.core.ui.components.videoplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.widget.VideoView
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.CoroutineScope
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
    fun setMuteState(isMute: Boolean)
}

@Composable
fun rememberVideoPlayerState(videoSource: Uri, isLooping: Boolean = true): VideoPlayerState {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    MediaPlayerLifecycle(videoView)

    val coroutineScope = rememberCoroutineScope()
    val state = remember(videoSource) {
        VideoPlayerStateImpl(videoView, audioManager, videoSource, isLooping, coroutineScope)
    }

    DisposableEffect(Unit) {
        onDispose {
            videoView.stopPlayback()
        }
    }

    return state
}

private class VideoPlayerStateImpl(
    override val videoView: VideoView,
    val audioManager: AudioManager,
    val videoSource: Uri,
    val isLooping: Boolean,
    private val coroutineScope: CoroutineScope
) : VideoPlayerState {
    override var isPrepared by mutableStateOf(false)
    override val isPlaying: Boolean
        get() = _isPlaying
    override var isMute by mutableStateOf(false)
    override var totalDuration by mutableIntStateOf(0)
    override var currentDuration by mutableIntStateOf(0)
    private var _isPlaying by mutableStateOf(videoView.isPlaying)

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
        if (milliseconds <= totalDuration) {
            currentDuration = milliseconds
            videoView.seekTo(milliseconds)
            if (!videoView.isPlaying) {
                play()
            }
        }
    }

    override fun setMuteState(isMute: Boolean) {
        val direction = if (isMute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            direction,
            0
        )
        this.isMute = isMute
    }

    private fun updateProgress() {
        stopUpdatingProgress()
        progressJob = coroutineScope.launch {
            while (isActive) {
                currentDuration = videoView.currentPosition
                delay(500)
            }
        }
    }

    fun stopUpdatingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    init {
        videoView.apply {
            stopPlayback()
            setVideoURI(videoSource)
            setOnPreparedListener {
                isPrepared = true
                totalDuration = it.duration
                it.isLooping = isLooping
                play()
            }
            setOnCompletionListener {
                _isPlaying = false
            }
        }
    }

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