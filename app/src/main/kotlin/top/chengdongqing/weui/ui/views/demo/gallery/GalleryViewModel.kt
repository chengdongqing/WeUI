package top.chengdongqing.weui.ui.views.demo.gallery

import android.content.Context
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GalleryViewModel : ViewModel() {
    var mediaItems by mutableStateOf<List<MediaItem>>(emptyList())
        private set
    var loading by mutableStateOf(true)
        private set

    fun refresh(context: Context) {
        viewModelScope.launch {
            loading = true
            mediaItems = queryMediaItems(context.applicationContext)
            loading = false
        }
    }

    private suspend fun queryMediaItems(context: Context): List<MediaItem> =
        withContext(Dispatchers.IO) {
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
                val nameColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION)
                val sizeColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
                val dateColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                val mediaTypeColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val mimeTypeColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
                val dataColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

                while (cursor.moveToNext()) {
                    val uri = MediaStore.Files.getContentUri("external", cursor.getLong(idColumn))
                    mediaItems.add(
                        MediaItem(
                            uri,
                            name = cursor.getString(nameColumn),
                            isVideo = cursor.getInt(mediaTypeColumn) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                            mimeType = cursor.getString(mimeTypeColumn),
                            duration = cursor.getLong(durationColumn)
                                .toDuration(DurationUnit.MILLISECONDS),
                            size = cursor.getLong(sizeColumn),
                            date = Date(cursor.getLong(dateColumn)),
                            path = cursor.getString(dataColumn)
                        )
                    )
                }
            }

            mediaItems
        }
}