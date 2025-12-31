package top.chengdongqing.weui.feature.media.audioplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import top.chengdongqing.weui.core.ui.components.R
import top.chengdongqing.weui.core.ui.components.slider.WeSlider
import top.chengdongqing.weui.core.utils.format
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WeAudioPlayer(state: AudioPlayerState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PrimaryDuration(state)
        Spacer(modifier = Modifier.height(40.dp))
        ProgressControl(state)
        Spacer(modifier = Modifier.height(60.dp))
        PlayControl(state)
    }
}

@Composable
private fun PrimaryDuration(state: AudioPlayerState) {
    Text(
        text = state.positionMs.milliseconds.format(isFull = true),
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ProgressControl(state: AudioPlayerState) {
    WeSlider(
        value = state.progress,
        range = 0f..state.durationMs.toFloat()
    ) {
        state.seekTo(it.roundToInt())
        if (!state.isPlaying) {
            state.play()
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = state.positionMs.milliseconds.format(),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = state.durationMs.milliseconds.format(),
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
                state.toggle()
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