package top.chengdongqing.weui.ui.views.media.gallery.preview

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import top.chengdongqing.weui.R
import top.chengdongqing.weui.extensions.clickableWithoutRipple
import top.chengdongqing.weui.ui.components.form.WeSlider
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.utils.UpdateProgress
import top.chengdongqing.weui.utils.formatDuration
import top.chengdongqing.weui.utils.rememberPlayProgress
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun VideoPreview(uri: Uri) {
    val (player, setPlayer) = remember { mutableStateOf<MediaPlayer?>(null) }
    val isPlaying = remember { mutableStateOf(false) }
    val isMute = remember { mutableStateOf(false) }
    var duration by remember { mutableIntStateOf(0) }
    var passed by remember { mutableIntStateOf(0) }
    UpdateProgress(player, isPlaying.value) { passed = it }

    Box(modifier = Modifier
        .fillMaxSize()
        .clickableWithoutRipple {
            player?.let {
                isPlaying.value = !player.isPlaying
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
            }
        }
    ) {
        VideoPlayerView(
            uri,
            setPlayer = {
                setPlayer(it)
                isPlaying.value = true
            },
            setDuration = {
                duration = it
            }
        )
        player?.let {
            VideoControls(player, isPlaying, isMute, duration, passed)
        }
    }
}

@Composable
private fun VideoPlayerView(
    uri: Uri,
    setPlayer: (MediaPlayer) -> Unit,
    setDuration: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(uri)
                setOnCompletionListener { it.start() }
                setOnPreparedListener {
                    setDuration(it.duration)
                    setPlayer(it)
                    start()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun BoxScope.VideoControls(
    player: MediaPlayer,
    isPlaying: MutableState<Boolean>,
    isMute: MutableState<Boolean>,
    duration: Int,
    passed: Int
) {
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    if (!isPlaying.value) {
        Icon(
            painter = painterResource(id = R.drawable.ic_play_arrow),
            contentDescription = "播放",
            modifier = Modifier
                .align(Alignment.Center)
                .size(120.dp)
                .alpha(0.4f),
            tint = Color.White
        )
    }

    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .background(BorderColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = formatDuration(passed.milliseconds), color = Color.White)

        val progress = rememberPlayProgress(passed, duration)
        WeSlider(
            value = progress.intValue,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            formatter = null
        ) {
            progress.intValue = it
            player.seekTo((it.toFloat() / 100 * duration).roundToInt())
            if (!player.isPlaying) {
                player.start()
                isPlaying.value = true
            }
        }

        Text(text = formatDuration(duration.milliseconds), color = Color.White)
        MuteControl(isMute, audioManager)
    }
}

@Composable
private fun MuteControl(isMute: MutableState<Boolean>, audioManager: AudioManager) {
    IconButton(onClick = {
        if (isMute.value) {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_UNMUTE,
                0
            )
        } else {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_MUTE,
                0
            )
        }
        isMute.value = !isMute.value
    }) {
        if (isMute.value) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.VolumeOff,
                contentDescription = "静音",
                tint = Color.White
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                contentDescription = "取消静音",
                tint = Color.White
            )
        }
    }
}