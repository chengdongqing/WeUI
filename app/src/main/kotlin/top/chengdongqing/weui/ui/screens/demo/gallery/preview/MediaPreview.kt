package top.chengdongqing.weui.ui.screens.demo.gallery.preview

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.utils.MediaStoreUtils
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.SetupFullscreen
import top.chengdongqing.weui.utils.isTrue
import java.io.FileInputStream
import java.io.IOException

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewScreen(uris: List<Uri>, current: Int = 0) {
    val pagerState = rememberPagerState(current) { uris.size }

    SetupFullscreen()
    Box {
        MediaPager(uris, pagerState)
        MediaPreviewInfo(uris, pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaPager(mediaUris: List<Uri>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { index ->
        val uri = mediaUris[index]
        when {
            uri.isVideoType() -> VideoPreview(uri)
            else -> ImagePreview(uri)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.MediaPreviewInfo(uris: List<Uri>, pagerState: PagerState) {
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
    )
    // 按钮组
    Row(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(y = (-80).dp)
    ) {
        val uri = uris[pagerState.currentPage]
        IconButton(
            onClick = {
                coroutineScope.launch {
                    if (saveMediaToGallery(context, uri)) {
                        toast.show(WeToastOptions("已保存到相册", icon = ToastIcon.SUCCESS))
                    } else {
                        toast.show(WeToastOptions("保存到相册失败", icon = ToastIcon.FAIL))
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
        Spacer(modifier = Modifier.width(20.dp))
    }
}

private suspend fun saveMediaToGallery(context: Context, uri: Uri): Boolean {
    val mediaType = if (uri.isVideoType()) MediaType.VIDEO else MediaType.IMAGE
    val mimeType = uri.getMimeType() ?: return false
    val filename = uri.lastPathSegment ?: return false

    return withContext(Dispatchers.IO) {
        try {
            val contentValues = MediaStoreUtils.createContentValues(
                filename, mimeType, mediaType, context
            )
            val contentUri = MediaStoreUtils.getContentUri(mediaType)
            val contentResolver = context.contentResolver
            contentResolver.insert(contentUri, contentValues)?.let { mediaUri ->
                contentResolver.openOutputStream(mediaUri)?.use { outputStream ->
                    FileInputStream(uri.toFile()).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                MediaStoreUtils.finishPending(mediaUri, context)
                true
            } ?: false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}

private fun Uri.isVideoType() = getMimeType()?.startsWith("video").isTrue()

private fun Uri.getMimeType(): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(this.toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}