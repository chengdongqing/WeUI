package top.chengdongqing.core.data.repository

import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.MediaType

class LocalMediaRepositoryImpl(private val context: Context) : LocalMediaRepository {
    override suspend fun loadMediaList(types: Array<MediaType>): List<MediaItem> {
        return withContext(Dispatchers.IO) {
            val mediaItems = mutableListOf<MediaItem>()

            val selectionUri = MediaStore.Files.getContentUri("external")
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
            val selection =
                "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (${types.joinToString(separator = ",") { "?" }})"
            val selectionArgs = types.map { it.columnType.toString() }.toTypedArray()
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
            context.contentResolver.query(
                selectionUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val nameColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION)
                val dateColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                val mediaTypeColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val mimeTypeColumn =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val fileUri =
                        MediaStore.Files.getContentUri("external", cursor.getLong(idColumn))
                    val mediaType = MediaType.ofColumnType(cursor.getInt(mediaTypeColumn))

                    mediaItems.add(
                        MediaItem(
                            fileUri,
                            filename = cursor.getString(nameColumn),
                            mediaType = mediaType ?: MediaType.IMAGE,
                            mimeType = cursor.getString(mimeTypeColumn),
                            duration = cursor.getLong(durationColumn),
                            date = cursor.getLong(dateColumn)
                        )
                    )
                }
            }

            mediaItems
        }
    }
}