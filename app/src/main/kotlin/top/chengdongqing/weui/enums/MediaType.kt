package top.chengdongqing.weui.enums

import android.provider.MediaStore

enum class MediaType(val columnType: Int) {
    IMAGE(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
    VIDEO(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    AUDIO(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO),
    RECORDING(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO);

    companion object {
        fun ofColumnType(columnType: Int): MediaType? {
            return entries.find { it.columnType == columnType }
        }
    }
}