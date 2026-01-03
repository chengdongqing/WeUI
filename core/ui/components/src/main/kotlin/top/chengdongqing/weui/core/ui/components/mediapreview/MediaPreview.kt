package top.chengdongqing.weui.core.ui.components.mediapreview

import android.app.Activity
import android.content.Context
import androidx.compose.animation.core.SnapSpec
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
import androidx.compose.runtime.LaunchedEffect
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
import me.saket.telephoto.zoomable.rememberZoomableState
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.isVideo
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.ui.components.videoplayer.VideoPlayerDefaults
import top.chengdongqing.weui.core.ui.components.videoplayer.WeVideoPlayer
import top.chengdongqing.weui.core.ui.components.videoplayer.rememberVideoPlayerState
import top.chengdongqing.weui.core.utils.MediaStoreUtils
import top.chengdongqing.weui.core.utils.SetupFullscreen
import top.chengdongqing.weui.core.utils.copyToFile
import top.chengdongqing.weui.core.utils.copyUri
import top.chengdongqing.weui.core.utils.getFileExtension
import top.chengdongqing.weui.core.utils.shareFile
import java.io.File
import java.io.IOException

@Composable
fun WeMediaPreview(medias: Array<MediaItem>, current: Int = 0) {
    val pagerState = rememberPagerState(current) { medias.size }

    SetupFullscreen()
    Box {
        MediaPager(medias, pagerState)
        PagerInfo(
            total = medias.size,
            current = pagerState.currentPage + 1
        )
        ToolBar(medias, pagerState)
    }
}

@Composable
private fun MediaPager(medias: Array<MediaItem>, pagerState: PagerState) {
    val activity = LocalContext.current as Activity

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

            else -> {
                val zoomableState = rememberZoomableState()

                ImagePreview(media.uri, zoomableState) {
                    activity.finish()
                }

                // 滑到另一页后重置当前页的缩放状态
                if (pagerState.settledPage != index) {
                    LaunchedEffect(Unit) {
                        zoomableState.resetZoom(SnapSpec())
                    }
                }
            }
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

@Composable
private fun BoxScope.ToolBar(medias: Array<MediaItem>, pagerState: PagerState) {
    val context = LocalContext.current
    val toast = rememberToastState()
    val coroutineScope = rememberCoroutineScope()
    val media = medias[pagerState.currentPage]

    /**
     * 处理分享
     */
    fun handleShare() {
        coroutineScope.launch(Dispatchers.IO) {
            context.contentResolver.openInputStream(media.uri)?.use { inputStream ->
                // 创建临时文件
                val tempFile = File.createTempFile(
                    "media_",
                    media.filename.getFileExtension()
                ).apply {
                    deleteOnExit()
                }
                // 拷贝到临时文件
                inputStream.copyToFile(tempFile)
                // 调用系统分享
                context.shareFile(tempFile, media.mimeType)
            }
        }
    }

    Row(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 26.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionIcon(imageVector = Icons.Outlined.Share, label = "分享") {
            handleShare()
        }
        ActionIcon(imageVector = Icons.Outlined.Download, label = "保存") {
            coroutineScope.launch {
                if (context.saveToAlbum(media)) {
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

/**
 * 保存媒体文件到相册
 */
private suspend fun Context.saveToAlbum(media: MediaItem): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val contentUri = MediaStoreUtils.getContentUri(media.mediaType)
            val contentValues = MediaStoreUtils.createContentValues(
                media.filename,
                media.mediaType,
                media.mimeType,
                this@saveToAlbum
            )

            // 插入数据库记录（此时文件是 IS_PENDING = 1 状态）
            val tempUri =
                contentResolver.insert(contentUri, contentValues) ?: return@withContext false
            // 拷贝数据流
            val isSuccess = contentResolver.copyUri(media.uri, tempUri)

            if (isSuccess) {
                // 成功：解除挂起状态，让相册可见
                MediaStoreUtils.finishPending(tempUri, this@saveToAlbum)
                true
            } else {
                // 失败：删除数据库中的占位记录，防止相册出现空白文件
                contentResolver.delete(tempUri, null)
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}
