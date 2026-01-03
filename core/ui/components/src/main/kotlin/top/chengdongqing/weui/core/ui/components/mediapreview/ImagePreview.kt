package top.chengdongqing.weui.core.ui.components.mediapreview

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState

@Composable
fun ImagePreview(
    uri: Uri,
    zoomableState: ZoomableState = rememberZoomableState(),
    onDismiss: () -> Unit
) {
    val state = rememberZoomableImageState(zoomableState)

    ZoomableAsyncImage(
        model = uri,
        state = state,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        onClick = { onDismiss() }
    )
}
