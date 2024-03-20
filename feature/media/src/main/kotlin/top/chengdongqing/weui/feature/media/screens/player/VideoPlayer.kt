package top.chengdongqing.weui.ui.screens.media.player

import android.net.Uri
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
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.videoplayer.WeVideoPlayer
import top.chengdongqing.weui.core.ui.components.videoplayer.rememberVideoPlayerState
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.core.utils.SetupStatusBarStyle
import top.chengdongqing.weui.core.utils.rememberPickMediasLauncher

@Composable
fun VideoPlayerScreen() {
    SetupStatusBarStyle(isDark = false)
    WeUITheme(darkTheme = true) {
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
}

@Composable
private fun VideoPicker(onChange: (Uri) -> Unit) {
    val pickMedia = rememberPickMediasLauncher {
        onChange(it.first().uri)
    }

    WeButton(text = "选择视频") {
        pickMedia(MediaType.VIDEO, 1)
    }
}