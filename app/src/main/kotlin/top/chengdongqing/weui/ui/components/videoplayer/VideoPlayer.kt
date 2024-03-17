package top.chengdongqing.weui.ui.components.videoplayer

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.slider.WeSlider
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.format
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WeVideoPlayer(
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    controlBar: @Composable BoxScope.() -> Unit = {
        VideoPlayerDefaults.ControlBar(
            state,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
) {
    Box(modifier = modifier
        .fillMaxSize()
        .clickableWithoutRipple {
            if (state.isPlaying) {
                state.pause()
            } else {
                state.play()
            }
        }
    ) {
        AndroidView(
            factory = { state.videoView },
            modifier = Modifier.fillMaxSize()
        )
        PlayIcon(state)
        controlBar()
    }
}

@Composable
private fun BoxScope.PlayIcon(state: VideoPlayerState) {
    if (state.isPrepared && !state.isPlaying) {
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
}

object VideoPlayerDefaults {
    @Composable
    fun ControlBar(state: VideoPlayerState, modifier: Modifier) {
        Row(
            modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = state.currentDuration.milliseconds.format(), color = Color.White)
            WeSlider(
                value = state.currentDuration.toFloat(),
                range = 0f..state.totalDuration.toFloat(),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                state.seekTo(it.roundToInt())
            }
            Text(text = state.totalDuration.milliseconds.format(), color = Color.White)
            MuteControl(state)
        }
    }

    @Composable
    private fun MuteControl(state: VideoPlayerState) {
        IconButton(onClick = {
            state.setMute(!state.isMute)
        }) {
            if (state.isMute) {
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
}