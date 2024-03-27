package top.chengdongqing.weui.feature.media.screens.audio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.rememberToggleState
import top.chengdongqing.weui.feature.media.audioplayer.WeAudioPlayer
import top.chengdongqing.weui.feature.media.audioplayer.rememberAudioPlayerState

@Composable
fun AudioPlayerScreen() {
    WeScreen(title = "AudioPlayer", description = "音频播放", padding = PaddingValues(24.dp)) {
        val (audioSource, toggleSource) = rememberToggleState(
            defaultValue = "https://mp3.haoge500.com/upload/rank/20211219/6de3b8453a39588fbe4c83cdcf8594c4.mp3",
            reverseValue = "https://room.ylzmjd.com/shiting/校长-带你去旅行.mp3"
        )
        val state = rememberAudioPlayerState(audioSource.value)

        WeAudioPlayer(state)
        Spacer(modifier = Modifier.height(60.dp))
        WeButton(text = "切换音源") {
            toggleSource()
        }
    }
}