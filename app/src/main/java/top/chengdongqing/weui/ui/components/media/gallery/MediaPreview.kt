package top.chengdongqing.weui.ui.components.media.gallery

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreview(mediaItems: List<MediaItem>, activeIndex: Int, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val pagerState = rememberPagerState(initialPage = activeIndex) {
            mediaItems.size
        }
        HorizontalPager(state = pagerState) {
            val media = mediaItems[it]
            if (!media.isVideo) {
                ImagePreview(media.uri, onDismiss)
            } else {
                VideoPreview(media.uri)
            }
        }
    }

    // 控制状态栏的显示隐藏
    val window = (LocalContext.current as Activity).window
    DisposableEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        onDispose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
    }
}

@Composable
fun VideoPreview(uri: Uri) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(uri)

                // 添加媒体控制器
                val mediaController = MediaController(context)
                setMediaController(mediaController)
                mediaController.setAnchorView(this)

                // 设置循环播放
                setOnCompletionListener {
                    start()
                }

                start()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ImagePreview(uri: Uri, onDismiss: () -> Unit) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableFloatStateOf(1f) }

    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    zoom = if (zoom > 1f) 1f else 2f
                    offset = calculateDoubleTapOffset(zoom, size, it)
                }) {
                    onDismiss()
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures(onGesture = { centroid, pan, gestureZoom, _ ->
                    offset = offset.calculateNewOffset(
                        centroid, pan, zoom, gestureZoom, size
                    )
                    zoom = maxOf(1f, zoom * gestureZoom)
                })
            }
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom; scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
    )
}

private fun calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize,
    tapOffset: Offset
): Offset {
    val newOffset = Offset(tapOffset.x, tapOffset.y)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}

private fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
            (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}