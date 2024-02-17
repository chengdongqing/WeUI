package top.chengdongqing.weui.ui.views.media.player

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.views.template.gallery.preview.VideoPreview

@Composable
fun VideoPlayerPage() {
    WePage(title = "VideoPlayer", description = "视频播放", padding = PaddingValues(0.dp)) {
        var uri by remember { mutableStateOf<Uri?>(null) }

        uri?.let { VideoPreview(it) } ?: VideoPicker { uri = it }
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