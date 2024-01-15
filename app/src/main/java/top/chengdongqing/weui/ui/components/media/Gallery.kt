package top.chengdongqing.weui.ui.components.media

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import java.util.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeGallery() {
    val context = LocalContext.current
    val mediaItems = remember {
        mutableStateListOf<MediaItem>()
    }

    val pickMultipleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) {
        // setMedias(it)
    }
    val readStoragePermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            WeButton("手动选择图片", type = ButtonType.PLAIN) {
                pickMultipleMediaLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            val coroutineScope = rememberCoroutineScope()
            var loading by remember {
                mutableStateOf(false)
            }
            WeButton("自动读取图片", loading = loading) {
                if (readStoragePermissionState.status.isGranted) {
                    loading = true
                    coroutineScope.launch {
                        mediaItems.clear()
                        mediaItems.addAll(queryMedias(context))
                        loading = false
                    }
                } else {
                    readStoragePermissionState.launchPermissionRequest()
                }
            }
        }
        Spacer(Modifier.height(40.dp))
        ThumbnailGrid(mediaItems)
    }
}

private suspend fun queryImages(context: Context): List<Uri> = withContext(Dispatchers.IO) {
    val imageList = mutableListOf<Uri>()
    val projection = arrayOf(
        MediaStore.Images.ImageColumns._ID
    )

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageList.add(uri)
        }
    }

    imageList
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
        MediaStore.Files.FileColumns.MEDIA_TYPE + " = ? OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + " = ?",
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
                    duration = cursor.getLong(durationColumn).toDuration(DurationUnit.SECONDS),
                    size = cursor.getLong(sizeColumn),
                    date = Date(cursor.getLong(dateColumn)),
                    path = cursor.getString(dataColumn)
                )
            )
        }
    }

    mediaItems
}

@Composable
private fun ThumbnailGrid(mediaItems: List<MediaItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(mediaItems) { item ->
            AsyncImage(
                model = if (item.isVideo) {
                    rememberVideoThumbnail(item.uri).value
                } else {
                    item.path
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1f)
            )
        }
    }
}

@Composable
private fun rememberVideoThumbnail(videoUri: Uri): MutableState<Bitmap?> {
    val context = LocalContext.current
    val thumbnailState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(videoUri) {
        val thumbnail = MediaMetadataRetriever().run {
            setDataSource(context, videoUri)
            extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
            extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
            getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        }
        thumbnailState.value = thumbnail
    }

    return thumbnailState
}

private data class MediaItem(
    val uri: Uri,
    val name: String,
    val isVideo: Boolean,
    val mimeType: String,
    val duration: Duration,
    val size: Long,
    val date: Date,
    val path: String
)