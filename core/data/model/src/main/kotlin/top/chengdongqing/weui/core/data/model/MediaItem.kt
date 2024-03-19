package top.chengdongqing.weui.core.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val uri: Uri,
    val filename: String,
    val filepath: String,
    val mediaType: MediaType,
    val mimeType: String,
    val duration: Long = 0,
    val size: Long = 0,
    val date: Long = 0
) : Parcelable

fun MediaItem.isImage(): Boolean = this.mediaType == MediaType.IMAGE
fun MediaItem.isVideo(): Boolean = this.mediaType == MediaType.VIDEO