package top.chengdongqing.weui.ui.views.media

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.media.Gallery1
import top.chengdongqing.weui.ui.components.media.Photo
import top.chengdongqing.weui.ui.components.media.WeGallery
import java.util.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun GalleryPage() {
    Page(title = "Gallery", description = "相册", padding = PaddingValues(0.dp)) {
        WeGallery()
    }
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeGallery1() {
    val context = LocalContext.current
    val mediaItems = remember {
        mutableStateListOf<MediaItem>()
    }

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

    Column {
        val coroutineScope = rememberCoroutineScope()
        var loading by remember {
            mutableStateOf(false)
        }
        WeButton(
            "读取图片",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            loading = loading
        ) {
            if (multiplePermissionsState.allPermissionsGranted) {
                loading = true
                coroutineScope.launch {
                    mediaItems.clear()
                    mediaItems.addAll(queryMedias(context))
                    loading = false
                }
            } else {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        }
        Spacer(Modifier.height(40.dp))
        Gallery1(mediaItems.mapIndexed { index, item -> Photo(index, item.path, item.path, "") })
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