package top.chengdongqing.weui.ui.views.media.gallery.preview

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.utils.MediaStoreUtil
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.SetupFullscreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewPage(uris: List<Uri>, current: Int = 0) {
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
        IconButton(
            onClick = {
                coroutineScope.launch {
                    if (saveMediaToGallery(context, uri)) {
                        toast.show("已保存到相册", icon = ToastIcon.SUCCESS)
                    } else {
                        toast.show("保存到相册失败", icon = ToastIcon.FAIL)
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
    val isVideo = uri.isVideoType()
    val mimeType = uri.getMimeType() ?: return false
    val filename = uri.lastPathSegment ?: return false

    return MediaStoreUtil.saveMediaToGallery(
        context,
        uri,
        filename,
        mimeType,
        mediaType = if (isVideo) MediaType.VIDEO else MediaType.IMAGE
    )
}

private fun Uri.isVideoType() = getMimeType()?.startsWith("video") == true

private fun Uri.getMimeType(): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(this.toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}