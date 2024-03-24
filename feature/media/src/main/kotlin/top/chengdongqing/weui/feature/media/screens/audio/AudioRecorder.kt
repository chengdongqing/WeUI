package top.chengdongqing.weui.feature.media.screens.audio

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.media.audiorecorder.WeAudioRecorder

@Composable
fun AudioRecorderScreen() {
    WeScreen(title = "AudioRecorder", description = "音频录制") {
        WeAudioRecorder()
    }
}