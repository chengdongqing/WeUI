package top.chengdongqing.weui.ui.views.template.gallery.preview

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
import top.chengdongqing.weui.ui.components.slider.WeSlider
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.utils.formatDuration
import top.chengdongqing.weui.utils.rememberPlayPercent
import top.chengdongqing.weui.utils.rememberPlayProgress
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPreview(uri: Uri) {
    val (player, setPlayer) = remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isMute by remember { mutableStateOf(false) }
    var duration by remember { mutableIntStateOf(0) }
    val progress = rememberPlayProgress(player, isPlaying)

    Box(modifier = Modifier
        .fillMaxSize()
        .clickableWithoutRipple {
            player?.let {
                isPlaying = !player.isPlaying
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
            }
        }
    ) {
        VideoView(
            uri,
            setPlayer = {
                setPlayer(it)
                isPlaying = true
            },
            setDuration = {
                duration = it
            }
        )
        player?.let {
            ControlBar(
                player,
                duration,
                progress,
                isPlaying,
                isMute,
                setPlayingState = { isPlaying = it },
                setMuteState = { isMute = it }
            )
        }
    }
}

@Composable
private fun VideoView(
    uri: Uri,
    setPlayer: (MediaPlayer?) -> Unit,
    setDuration: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(uri)
                setOnPreparedListener {
                    setDuration(it.duration)
                    it.isLooping = true
                    setPlayer(it)
                    it.start()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun BoxScope.ControlBar(
    player: MediaPlayer,
    duration: Int,
    progress: Int,
    isPlaying: Boolean,
    isMute: Boolean,
    setPlayingState: (Boolean) -> Unit,
    setMuteState: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    if (!isPlaying) {
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
        Text(text = formatDuration(progress.milliseconds), color = Color.White)

        var percent by rememberPlayPercent(progress, duration)
        WeSlider(
            value = percent,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            formatter = null
        ) {
            percent = it
            player.seekTo((it.toFloat() / 100 * duration).roundToInt())
            if (!player.isPlaying) {
                player.start()
                setPlayingState(true)
            }
        }

        Text(text = formatDuration(duration.milliseconds), color = Color.White)
        MuteControl(isMute, audioManager, setMuteState)
    }
}

@Composable
private fun MuteControl(
    isMute: Boolean,
    audioManager: AudioManager,
    setMuteState: (Boolean) -> Unit
) {
    IconButton(onClick = {
        if (isMute) {
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
        setMuteState(!isMute)
    }) {
        if (isMute) {
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