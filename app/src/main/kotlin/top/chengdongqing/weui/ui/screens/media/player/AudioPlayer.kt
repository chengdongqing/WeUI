package top.chengdongqing.weui.ui.screens.media.player

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.audioplayer.WeAudioPlayer
import top.chengdongqing.weui.ui.components.audioplayer.rememberAudioPlayerState
import top.chengdongqing.weui.ui.components.screen.WeScreen

private const val audioSource =
    "https://mp3.haoge500.com/upload/rank/20211219/6de3b8453a39588fbe4c83cdcf8594c4.mp3"

@Composable
fun AudioPlayerScreen() {
    WeScreen(title = "AudioPlayer", description = "音频播放", PaddingValues(24.dp)) {
        WeAudioPlayer(rememberAudioPlayerState(audioSource))
    }
}