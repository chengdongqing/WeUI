package top.chengdongqing.weui.ui.views.media.gallery.preview

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun VideoPreview(uri: Uri) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(uri)
                // 循环播放
                setOnCompletionListener {
                    start()
                }
                start()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}