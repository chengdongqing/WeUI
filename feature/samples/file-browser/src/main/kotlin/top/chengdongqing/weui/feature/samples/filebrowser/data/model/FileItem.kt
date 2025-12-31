package top.chengdongqing.weui.feature.samples.filebrowser.data.model

import androidx.annotation.DrawableRes

data class FileItem(
    val name: String,
    val path: String,
    val size: String,
    val mimeType: String,
    val isDirectory: Boolean,
    val isVisualMedia: Boolean,
    val lastModified: String,
    val childrenCount: Int,
    @get:DrawableRes val iconId: Int
)