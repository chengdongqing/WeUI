package top.chengdongqing.weui.ui.components.audioplayer

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
interface AudioPlayerState {
    /**
     * 播放器实例
     */
    val player: MediaPlayer

    /**
     * 是否播放中
     */
    val isPlaying: Boolean

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
}

@Composable
fun rememberAudioPlayerState(audioSource: String): AudioPlayerState {
    val player = remember { MediaPlayer() }
    MediaPlayerLifecycle(player)

    val state = remember(audioSource) {
        AudioPlayerStateImpl(player, audioSource)
    }

    DisposableEffect(state) {
        onDispose {
            player.release()
            state.stopUpdatingProgress()
        }
    }

    return state
}

private class AudioPlayerStateImpl(
    override val player: MediaPlayer,
    val audioSource: String
) : AudioPlayerState {
    override val isPlaying: Boolean
        get() = _isPlaying
    override val totalDuration: Int
        get() = _totalDuration
    override val currentDuration: Int
        get() = _currentDuration
    override val percent: Float
        get() = _percent

    override fun play() {
        if (!player.isPlaying) {
            player.start()
            updateProgress()
        }
        _isPlaying = true
    }

    override fun pause() {
        if (player.isPlaying) {
            player.pause()
            stopUpdatingProgress()
        }
        _isPlaying = false
    }

    override fun seekTo(milliseconds: Int) {
        if (milliseconds <= _totalDuration) {
            player.seekTo(milliseconds)
            if (!player.isPlaying) {
                play()
            }
            _percent = calculatePercent(milliseconds)
        }
    }

    private fun updateProgress() {
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                _currentDuration = player.currentPosition
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
        player.apply {
            reset()
            setDataSource(audioSource)
            prepareAsync()
            setOnPreparedListener {
                _totalDuration = it.duration
            }
            setOnCompletionListener {
                _isPlaying = false
            }
        }
    }

    private var _isPlaying by mutableStateOf(player.isPlaying)
    private var _totalDuration by mutableIntStateOf(0)
    private var _currentDuration by mutableIntStateOf(0)
    private var _percent by mutableFloatStateOf(0f)
    private var progressJob: Job? = null
}

@Composable
private fun MediaPlayerLifecycle(player: MediaPlayer) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousPlayingState = remember { mutableStateOf(false) }

    DisposableEffect(player) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    previousPlayingState.value = player.isPlaying
                    if (player.isPlaying) {
                        player.pause()
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    if (previousPlayingState.value) {
                        player.start()
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