package top.chengdongqing.weui.feature.media.screens.audio

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.popup.WePopup
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.media.audioplayer.WeAudioPlayer
import top.chengdongqing.weui.feature.media.audioplayer.rememberAudioPlayerState
import top.chengdongqing.weui.feature.media.audiorecorder.WeAudioRecorder

@Composable
fun AudioRecorderScreen() {
    WeScreen(title = "AudioRecorder", description = "音频录制") {
        var uri by remember { mutableStateOf<Uri?>(null) }
        var visible by remember { mutableStateOf(false) }

        WeAudioRecorder {
            uri = it
            visible = true
        }

        WePopup(
            visible,
            onClose = { visible = false }
        ) {
            uri?.let {
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp)) {
                    WeAudioPlayer(rememberAudioPlayerState(it))
                }
            }
        }
    }
}