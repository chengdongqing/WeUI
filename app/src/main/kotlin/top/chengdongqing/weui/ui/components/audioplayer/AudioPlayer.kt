package top.chengdongqing.weui.ui.components.audioplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.slider.WeSlider
import top.chengdongqing.weui.utils.format
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WeAudioPlayer(state: AudioPlayerState) {
    PrimaryDuration(state)
    Spacer(modifier = Modifier.height(40.dp))
    ProgressControl(state)
    Spacer(modifier = Modifier.height(60.dp))
    PlayControl(state)
}

@Composable
private fun PrimaryDuration(state: AudioPlayerState) {
    Text(
        text = state.currentDuration.milliseconds.format(isFull = true),
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ProgressControl(state: AudioPlayerState) {
    WeSlider(value = state.percent) {
        state.seekTo((it / 100 * state.totalDuration).roundToInt())
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = state.currentDuration.milliseconds.format(),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = state.totalDuration.milliseconds.format(),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun PlayControl(state: AudioPlayerState) {
    Box(
        modifier = Modifier
            .size(66.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable {
                if (state.isPlaying) {
                    state.pause()
                } else {
                    state.play()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (state.isPlaying) {
            Icon(
                imageVector = Icons.Outlined.Pause,
                contentDescription = "暂停",
                modifier = Modifier
                    .size(44.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_play_arrow),
                contentDescription = "播放",
                modifier = Modifier
                    .size(66.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}