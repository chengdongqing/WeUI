package top.chengdongqing.weui.ui.screens.demo.gallery.preview

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import top.chengdongqing.weui.data.model.MediaItem
import top.chengdongqing.weui.data.model.isVideo
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState
import top.chengdongqing.weui.ui.components.videoplayer.VideoPlayerDefaults
import top.chengdongqing.weui.ui.components.videoplayer.WeVideoPlayer
import top.chengdongqing.weui.ui.components.videoplayer.rememberVideoPlayerState
import top.chengdongqing.weui.utils.MediaStoreUtils
import top.chengdongqing.weui.utils.SetupFullscreen
import top.chengdongqing.weui.utils.shareFile
import java.io.File
import java.io.IOException

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewScreen(medias: Array<MediaItem>, current: Int = 0) {
    val pagerState = rememberPagerState(current) { medias.size }

    SetupFullscreen()
    Box {
        MediaPager(medias, pagerState)
        PagerInfo(total = medias.size, current = pagerState.currentPage + 1)
        ToolBar(medias, pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaPager(medias: Array<MediaItem>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { index ->
        val media = medias[index]
        when {
            media.isVideo() -> {
                val state = rememberVideoPlayerState(videoSource = media.uri)
                WeVideoPlayer(state) {
                    VideoPlayerDefaults.ControlBar(
                        state,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-60).dp)
                    )
                }
            }

            else -> ImagePreview(media.uri)
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
private fun BoxScope.ToolBar(medias: Array<MediaItem>, pagerState: PagerState) {
    val context = LocalContext.current
    val toast = rememberToastState()
    val coroutineScope = rememberCoroutineScope()
    val media = medias[pagerState.currentPage]

    Row(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 26.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionIcon(imageVector = Icons.Outlined.Share, label = "分享") {
            val type = if (media.isVideo()) "video/*" else "image/*"
            context.shareFile(File(media.filepath), type)
        }
        ActionIcon(
            imageVector = Icons.Outlined.Download,
            label = "保存"
        ) {
            coroutineScope.launch {
                if (context.saveMediaToGallery(media)) {
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

private suspend fun Context.saveMediaToGallery(media: MediaItem): Boolean {
    val context = this

    return withContext(Dispatchers.IO) {
        try {
            val contentValues = MediaStoreUtils.createContentValues(
                media.filename,
                media.mimeType,
                media.mediaType,
                context
            )
            val contentUri = MediaStoreUtils.getContentUri(media.mediaType)
            context.contentResolver.apply {
                insert(contentUri, contentValues)?.let { newMediaUri ->
                    openOutputStream(newMediaUri)?.use { outputStream ->
                        openInputStream(media.uri)?.use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    MediaStoreUtils.finishPending(newMediaUri, context)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}