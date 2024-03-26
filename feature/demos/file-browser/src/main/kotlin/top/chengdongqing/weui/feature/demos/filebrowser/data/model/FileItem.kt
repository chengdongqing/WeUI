package top.chengdongqing.weui.feature.demos.filebrowser.data.model

import androidx.annotation.DrawableRes

data class FileItem(
    val name: String,
    val path: String,
    val size: String,
    val mimeType: String?,
    val isDirectory: Boolean,
    val lastModified: String,
    val childrenCount: Int,
    @DrawableRes val iconId: Int
)