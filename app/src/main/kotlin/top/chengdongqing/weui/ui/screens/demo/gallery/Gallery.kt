package top.chengdongqing.weui.ui.screens.demo.gallery

import android.Manifest
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okio.IOException
import top.chengdongqing.weui.constant.ChineseDateWeekFormatter
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.screens.demo.gallery.preview.MediaPreviewActivity
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.formatDuration
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

@Composable
fun GalleryScreen(galleryViewModel: GalleryViewModel = viewModel()) {
    WeScreen(title = "Gallery", description = "相册", padding = PaddingValues(0.dp)) {
        val context = LocalContext.current

        if (galleryViewModel.loading) {
            WeLoadMore()
        }
        RequestMediaPermission {
            LaunchedEffect(Unit) {
                delay(300)
                galleryViewModel.refresh(context)
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(bottom = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                galleryViewModel.mediaGroups.forEach { (date, items) ->
                    val title = date.format(DateTimeFormatter.ofPattern(ChineseDateWeekFormatter))
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        )
                    }
                    itemsIndexed(items) { index, item ->
                        MediaItem(item, Modifier.clickableWithoutRipple {
                            val intent = MediaPreviewActivity.newIntent(context).apply {
                                putExtra("uris", items.map { it.path }.toTypedArray())
                                putExtra("current", index)
                                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            }
                            context.startActivity(intent)
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaItem(item: MediaItem, modifier: Modifier) {
    Box(
        modifier
            .aspectRatio(1f)
            .background(Color.LightGray)
    ) {
        AsyncImage(
            model = produceThumbnail(item).value,
            contentDescription = "Gallery Item",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        if (item.isVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                    .padding(vertical = 3.dp, horizontal = 6.dp)
            ) {
                Text(
                    text = formatDuration(item.duration),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun produceThumbnail(item: MediaItem): State<Any?> {
    val context = LocalContext.current

    return produceState<Any?>(initialValue = null, item.uri) {
        // 图片在低版本系统直接加载原图
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !item.isVideo) {
            value = item.uri
        } else {
            value = withContext(Dispatchers.IO) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 图片或视频在高版本系统支持加载缩略图
                        context.contentResolver.loadThumbnail(
                            item.uri,
                            Size(200, 200),
                            null
                        )
                    } else {
                        // 视频在低版本系统获取首帧
                        MediaMetadataRetriever().use {
                            it.setDataSource(context, item.uri)
                            it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                                ?.toInt()
                            it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                                ?.toInt()
                            it.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestMediaPermission(content: @Composable () -> Unit) {
    val permissionState = rememberMultiplePermissionsState(
        remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    )

    LaunchedEffect(permissionState) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (permissionState.allPermissionsGranted) {
        content()
    }
}

data class MediaItem(
    val uri: Uri,
    val name: String,
    val isVideo: Boolean,
    val mimeType: String,
    val duration: Duration,
    val size: Long,
    val date: Long,
    val path: String
)