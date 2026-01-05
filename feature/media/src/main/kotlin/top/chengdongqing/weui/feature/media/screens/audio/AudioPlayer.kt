package top.chengdongqing.weui.feature.media.screens.audio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.rememberToggleState
import top.chengdongqing.weui.feature.media.audioplayer.WeAudioPlayer
import top.chengdongqing.weui.feature.media.audioplayer.rememberAudioPlayerState

@Composable
fun AudioPlayerScreen() {
    WeScreen(title = "AudioPlayer", description = "音频播放", padding = PaddingValues(24.dp)) {
        val (audioSource, toggleSource) = rememberToggleState(
            defaultValue = "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/eadb8ddc86f1791154442a928b042e2f.mp4",
            reverseValue = "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/e25d81c4922fca5ebe51877717ef9b76.mp4"
        )
        val state = rememberAudioPlayerState(audioSource.value.toUri())

        WeAudioPlayer(state)
        Spacer(modifier = Modifier.height(60.dp))
        WeButton(text = "切换音源") {
            toggleSource()
        }
    }
}