package top.chengdongqing.weui.feature.media.screens.audio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.media.audioplayer.WeAudioPlayer
import top.chengdongqing.weui.feature.media.audioplayer.rememberAudioPlayerState

private const val audioSource =
    "https://mp3.haoge500.com/upload/rank/20211219/6de3b8453a39588fbe4c83cdcf8594c4.mp3"

@Composable
fun AudioPlayerScreen() {
    WeScreen(title = "AudioPlayer", description = "音频播放", padding = PaddingValues(24.dp)) {
        WeAudioPlayer(rememberAudioPlayerState(audioSource))
    }
}