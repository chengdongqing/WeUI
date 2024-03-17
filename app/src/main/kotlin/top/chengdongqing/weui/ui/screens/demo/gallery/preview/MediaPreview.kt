package top.chengdongqing.weui.ui.screens.demo.gallery.preview

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState
import top.chengdongqing.weui.utils.MediaStoreUtils
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.SetupFullscreen
import top.chengdongqing.weui.utils.isTrue
import top.chengdongqing.weui.utils.shareFile
import java.io.FileInputStream
import java.io.IOException

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewScreen(uris: List<Uri>, current: Int = 0) {
    val pagerState = rememberPagerState(current) { uris.size }

    SetupFullscreen()
    Box {
        MediaPager(uris, pagerState)
        PagerInfo(total = uris.size, current = pagerState.currentPage + 1)
        ToolBar(uris, pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaPager(uris: List<Uri>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { index ->
        val uri = uris[index]
        when {
            uri.isVideoType() -> VideoPreview(uri)
            else -> ImagePreview(uri)
        }
    }
}

@Composable
private fun BoxScope.PagerInfo(total: Int, current: Int) {
    Text(
        text = "${current}/${total}",
        color = Color.White,
        fontSize = 14.sp,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 50.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.ToolBar(uris: List<Uri>, pagerState: PagerState) {
    val context = LocalContext.current
    val toast = rememberToastState()
    val coroutineScope = rememberCoroutineScope()
    val uri = uris[pagerState.currentPage]

    Row(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 26.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionIcon(imageVector = Icons.Outlined.Share, label = "分享") {
            context.shareFile(uri)
        }
        ActionIcon(
            imageVector = Icons.Outlined.Download,
            label = "保存"
        ) {
            coroutineScope.launch {
                if (saveMediaToGallery(context, uri)) {
                    toast.show("已保存到相册", icon = ToastIcon.SUCCESS)
                } else {
                    toast.show("保存失败", icon = ToastIcon.FAIL)
                }
            }
        }
    }
}

@Composable
private fun ActionIcon(imageVector: ImageVector, label: String?, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Gray.copy(0.6f)
        ),
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            imageVector,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
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
            context.contentResolver.apply {
                insert(contentUri, contentValues)?.let { mediaUri ->
                    openOutputStream(mediaUri)?.use { outputStream ->
                        FileInputStream(uri.path).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    MediaStoreUtils.finishPending(mediaUri, context)
                }
            }
            true
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