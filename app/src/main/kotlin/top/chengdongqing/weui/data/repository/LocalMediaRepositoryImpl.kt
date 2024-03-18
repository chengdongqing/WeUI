package top.chengdongqing.weui.data.repository

import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.data.model.MediaItem
import top.chengdongqing.weui.enums.MediaType

class LocalMediaRepositoryImpl(private val context: Context) : LocalMediaRepository {
    override suspend fun loadAllMedias(): List<MediaItem> {
        return withContext(Dispatchers.IO) {
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
                    val mediaType =
                        if (cursor.getInt(mediaTypeColumn) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                            MediaType.VIDEO
                        } else {
                            MediaType.IMAGE
                        }

                    mediaItems.add(
                        MediaItem(
                            uri,
                            filename = cursor.getString(nameColumn),
                            filepath = cursor.getString(dataColumn),
                            mediaType = mediaType,
                            mimeType = cursor.getString(mimeTypeColumn),
                            duration = cursor.getLong(durationColumn),
                            size = cursor.getLong(sizeColumn),
                            date = cursor.getLong(dateColumn)
                        )
                    )
                }
            }

            mediaItems
        }
    }
}