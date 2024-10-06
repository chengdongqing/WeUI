package top.chengdongqing.weui.feature.media.audioplayer

import android.content.Context
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
     * 设置音源
     */
    fun setSource(path: String)
    fun setSource(uri: Uri)

    /**
     * 开始播放
     */
    fun play()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 跳转到指定时长
     */
    fun seekTo(milliseconds: Int)
}

@Composable
fun rememberAudioPlayerState(path: String): AudioPlayerState {
    val state = rememberAudioPlayerState()

    LaunchedEffect(path) {
        state.setSource(path)
    }

    return state
}

@Composable
fun rememberAudioPlayerState(uri: Uri): AudioPlayerState {
    val state = rememberAudioPlayerState()

    LaunchedEffect(uri) {
        state.setSource(uri)
    }

    return state
}

@Composable
fun rememberAudioPlayerState(): AudioPlayerState {
    val player = remember { MediaPlayer() }
    MediaPlayerLifecycle(player)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        AudioPlayerStateImpl(player, context, coroutineScope)
    }
}

private class AudioPlayerStateImpl(
    override val player: MediaPlayer,
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : AudioPlayerState {
    override val isPlaying: Boolean
        get() = _isPlaying
    override var totalDuration by mutableIntStateOf(0)
    override var currentDuration by mutableIntStateOf(0)

    override fun setSource(path: String) {
        reset()
        player.setDataSource(path)
        prepare()
    }

    override fun setSource(uri: Uri) {
        reset()
        player.setDataSource(context, uri)
        prepare()
    }

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

    private fun stopUpdatingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun reset() {
        player.reset()
        _isPlaying = false
        currentDuration = 0
        stopUpdatingProgress()
    }

    private fun prepare() {
        player.apply {
            prepareAsync()
            setOnPreparedListener {
                totalDuration = it.duration
            }
            setOnCompletionListener {
                _isPlaying = false
            }
        }
    }

    private var _isPlaying by mutableStateOf(player.isPlaying)
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

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
}