package top.chengdongqing.weui.ui.screens.media.player

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.videoplayer.WeVideoPlayer
import top.chengdongqing.weui.ui.components.videoplayer.rememberVideoPlayerState

@Composable
fun VideoPlayerScreen() {
    WeScreen(
        title = "VideoPlayer",
        description = "视频播放",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        var uri by remember { mutableStateOf<Uri?>(null) }

        VideoPicker { uri = it }
        Spacer(modifier = Modifier.height(20.dp))
        uri?.let {
            WeVideoPlayer(rememberVideoPlayerState(videoSource = it))
        }
    }
}

@Composable
private fun VideoPicker(onChange: (Uri) -> Unit) {
    val pickMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let { onChange(it) }
    }

    WeButton(text = "选择视频") {
        pickMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
        )
    }
}