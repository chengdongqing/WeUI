package top.chengdongqing.weui.feature.media.audioplayer

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.CoroutineScope
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

    val coroutineScope = rememberCoroutineScope()
    val state = remember(audioSource) {
        AudioPlayerStateImpl(player, audioSource, coroutineScope)
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    return state
}

private class AudioPlayerStateImpl(
    override val player: MediaPlayer,
    val audioSource: String,
    private val coroutineScope: CoroutineScope
) : AudioPlayerState {
    override val isPlaying: Boolean
        get() = _isPlaying
    override var totalDuration by mutableIntStateOf(0)
    override var currentDuration by mutableIntStateOf(0)
    private var _isPlaying by mutableStateOf(player.isPlaying)
    private var progressJob: Job? = null

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
        if (milliseconds <= totalDuration) {
            currentDuration = milliseconds
            player.seekTo(milliseconds)
            if (!player.isPlaying) {
                play()
            }
        }
    }

    private fun updateProgress() {
        stopUpdatingProgress()
        progressJob = coroutineScope.launch {
            while (isActive) {
                currentDuration = player.currentPosition
                delay(500)
            }
        }
    }

    fun stopUpdatingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    init {
        player.apply {
            reset()
            setDataSource(audioSource)
            prepareAsync()
            setOnPreparedListener {
                totalDuration = it.duration
            }
            setOnCompletionListener {
                _isPlaying = false
            }
        }
    }
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