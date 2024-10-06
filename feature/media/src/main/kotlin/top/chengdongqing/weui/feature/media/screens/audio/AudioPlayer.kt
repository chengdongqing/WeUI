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
            defaultValue = "https://webfs.kugou.com/202410060934/c15916a9d23eaec1cfbd03d5ab04800e/v3/0f57843484d3af06aaea623d66bb286c/yp/p_0_960113/ap1014_us0_mic7cb80c4fb32cac45794fcd3ed0e8f2a_pi406_mx105017519_s1500915556.mp3",
            reverseValue = "https://webfs.kugou.com/202410060924/429b7ef4514c197cca328256e9f9e26c/v3/04a21db1ab3c8e0d1d8f51f11bdb03a1/yp/full/ap1014_us0_mii0w1iw8z2ai2iphcu80ooo2ki81120_pi406_mx595775928_s3256085660.mp3"
        )
        val state = rememberAudioPlayerState(audioSource.value)

        WeAudioPlayer(state)
        Spacer(modifier = Modifier.height(60.dp))
        WeButton(text = "切换音源") {
            toggleSource()
        }
    }
}