package top.chengdongqing.weui.ui.views.media.player

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeSlider
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.utils.formatDuration
import top.chengdongqing.weui.utils.rememberPlayPercent
import top.chengdongqing.weui.utils.rememberPlayProgress
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

const val audioSource =
    "https://mp3.haoge500.com/upload/rank/20211219/6de3b8453a39588fbe4c83cdcf8594c4.mp3"

@Composable
fun AudioPlayerPage() {
    WePage(title = "AudioPlayer", description = "音频播放", PaddingValues(24.dp)) {
        val audioState = rememberAudioPlayer(audioSource)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatDuration(audioState.progress.milliseconds, true),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(40.dp))
            ProgressControl(audioState)
            Spacer(modifier = Modifier.height(60.dp))
            PlayControl(
                audioState.isPlaying,
                audioState.setPlaying,
                audioState.player
            )
        }
    }
}

@Composable
private fun ProgressControl(audioState: AudioState) {
    val player = audioState.player
    val duration by rememberUpdatedState(audioState.duration)

    WeSlider(value = audioState.percent, formatter = null) {
        audioState.setProgress(it)
        player.seekTo((it.toFloat() / 100 * duration).roundToInt())
        if (!player.isPlaying) {
            player.start()
            audioState.setPlaying(true)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = formatDuration(audioState.progress.milliseconds))
        Text(text = formatDuration(audioState.duration.milliseconds))
    }
}

@Composable
private fun PlayControl(isPlaying: Boolean, setPlaying: (Boolean) -> Unit, player: MediaPlayer) {
    Box(
        modifier = Modifier
            .size(66.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable {
                setPlaying(!player.isPlaying)
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
            Icon(
                imageVector = Icons.Outlined.Pause,
                contentDescription = "暂停",
                modifier = Modifier
                    .size(44.dp),
                tint = LightColor
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_play_arrow),
                contentDescription = "播放",
                modifier = Modifier
                    .size(66.dp),
                tint = LightColor
            )
        }
    }
}

@Composable
fun rememberAudioPlayer(audioSource: String): AudioState {
    val player = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var duration by remember { mutableIntStateOf(0) }
    val progress = rememberPlayProgress(player, isPlaying)
    var percent by rememberPlayPercent(progress, duration)

    LaunchedEffect(audioSource) {
        player.apply {
            reset()
            setDataSource(audioSource)
            prepareAsync()
            setOnPreparedListener {
                duration = it.duration
            }
            setOnCompletionListener {
                isPlaying = false
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val latestIsPlaying = rememberUpdatedState(isPlaying)
    DisposableEffect(Unit) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (player.isPlaying) {
                        player.pause()
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    if (latestIsPlaying.value) {
                        player.start()
                    }
                }

                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            player.release()
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return AudioState(
        player, isPlaying, duration, progress, percent,
        setPlaying = { isPlaying = it },
        setProgress = { percent = it }
    )
}

data class AudioState(
    val player: MediaPlayer,
    val isPlaying: Boolean,
    var duration: Int,
    val progress: Int,
    var percent: Int,
    val setPlaying: (Boolean) -> Unit,
    val setProgress: (Int) -> Unit
)