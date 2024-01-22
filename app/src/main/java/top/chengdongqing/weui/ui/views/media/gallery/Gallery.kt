package top.chengdongqing.weui.ui.views.media.gallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.formatDuration
import java.util.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryPage(galleryViewModel: GalleryViewModel, navController: NavController) {
    val context = LocalContext.current
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    )

    LaunchedEffect(Unit) {
        snapshotFlow {
            multiplePermissionsState.allPermissionsGranted
        }.collect { allPermissionsGranted ->
            if (allPermissionsGranted) {
                delay(500)
                galleryViewModel.setItems(queryMedias(context))
            } else {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        }
    }

    Page(title = "Gallery", description = "相册", padding = PaddingValues(0.dp)) {
        MediasGrid(context, galleryViewModel.mediaItems) {
            navController.navigate("media-preview?index=$it")
        }
    }
}

@Composable
private fun MediasGrid(context: Context, mediaItems: List<MediaItem>, onTap: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        itemsIndexed(mediaItems, key = { _, item -> item.path }) { index, item ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .clickableWithoutRipple {
                        onTap(index)
                    }
            ) {
                produceState<Any?>(initialValue = null, item.uri) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !item.isVideo) {
                        // 低版本系统直接加载原图
                        value = item.uri
                    } else {
                        value = withContext(Dispatchers.IO) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                // 高版本系统支持加载缩略图展示更快
                                context.contentResolver.loadThumbnail(
                                    item.uri,
                                    Size(300, 300),
                                    null
                                )
                            } else {
                                // 低版本系统获取视频首帧截图
                                MediaMetadataRetriever().run {
                                    setDataSource(context, item.uri)
                                    extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
                                    extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
                                    getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                                }
                            }
                        }
                    }
                }.value?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }
                if (item.isVideo) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                            .background(LightColor, RoundedCornerShape(16.dp))
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
    }
}

private suspend fun queryMedias(context: Context): List<MediaItem> = withContext(Dispatchers.IO) {
    val mediaItems = mutableListOf<MediaItem>()
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.DURATION,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.DATA,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE
    )
    context.contentResolver.query(
        MediaStore.Files.getContentUri("external"),
        projection,
        MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?",
        arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        ),
        MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
        val nameColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
        val durationColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION)
        val sizeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
        val dateColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
        val mediaTypeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
        val mimeTypeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
        val dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

        while (cursor.moveToNext()) {
            val uri = ContentUris.withAppendedId(
                MediaStore.Files.getContentUri("external"),
                cursor.getLong(idColumn)
            )
            mediaItems.add(
                MediaItem(
                    uri,
                    name = cursor.getString(nameColumn),
                    isVideo = cursor.getInt(mediaTypeColumn) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                    mimeType = cursor.getString(mimeTypeColumn),
                    duration = cursor.getLong(durationColumn).toDuration(DurationUnit.MILLISECONDS),
                    size = cursor.getLong(sizeColumn),
                    date = Date(cursor.getLong(dateColumn)),
                    path = cursor.getString(dataColumn)
                )
            )
        }
    }

    mediaItems
}

data class MediaItem(
    val uri: Uri,
    val name: String,
    val isVideo: Boolean,
    val mimeType: String,
    val duration: Duration,
    val size: Long,
    val date: Date,
    val path: String
)

class GalleryViewModel : ViewModel() {
    var mediaItems by mutableStateOf<List<MediaItem>>(emptyList())
        private set

    fun setItems(items: List<MediaItem>) {
        mediaItems = items
    }
}