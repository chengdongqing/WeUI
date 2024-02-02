package top.chengdongqing.weui.ui.views.media.gallery

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.VideoView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.utils.SetupFullscreen
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewPage(uris: List<Uri>, current: Int = 0) {
    val pagerState = rememberPagerState(current) { uris.size }

    SetupFullscreen()
    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) { index ->
            val uri = uris[index]
            if (getMimeType(uri)?.startsWith("video") == true) {
                VideoPreview(uri)
            } else {
                ImagePreview(uri)
            }
        }
        PreviewInfo(uris, pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.PreviewInfo(uris: List<Uri>, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val toast = rememberWeToast()

    // 当前页
    Text(
        text = "${pagerState.currentPage + 1}/${uris.size}",
        color = Color.White,
        fontSize = 14.sp,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 50.dp)
            .background(LightColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
    // 按钮组
    Row(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 20.dp)
    ) {
        val uri = uris[pagerState.currentPage]
        val isVideo = getMimeType(uri)?.startsWith("video") == true
        if (!isVideo) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (saveImageToGallery(
                                context,
                                uri.path!!,
                                uri.lastPathSegment!!,
                                getMimeType(uri)!!
                            )
                        ) {
                            toast.show("已保存到相册", icon = ToastIcon.SUCCESS)
                        }
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = FontColor1)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = "保存",
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
    }
}

private fun getMimeType(uri: Uri): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.path)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

@Composable
private fun VideoPreview(uri: Uri) {
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

@Composable
private fun ImagePreview(uri: Uri) {
    val context = LocalContext.current as Activity
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
                    context.finish()
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, gestureZoom, _ ->
                    offset = offset.calculateNewOffset(centroid, pan, zoom, gestureZoom, size)
                    zoom = maxOf(1f, zoom * gestureZoom)
                }
            }
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom; scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
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

private suspend fun saveImageToGallery(
    context: Context,
    filepath: String,
    filename: String,
    mimeType: String
) = withContext(Dispatchers.IO) {
    val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val path =
                arrayOf(
                    Environment.getExternalStorageDirectory().path,
                    Environment.DIRECTORY_PICTURES,
                    filename
                ).joinToString("/")
            put(
                MediaStore.MediaColumns.DATA,
                path
            )
        }
    }

    try {
        context.contentResolver.insert(imageCollection, contentValues)?.also { uri1 ->
            context.contentResolver.openOutputStream(uri1)?.use { outputStream ->
                File(filepath).inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } != null
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}