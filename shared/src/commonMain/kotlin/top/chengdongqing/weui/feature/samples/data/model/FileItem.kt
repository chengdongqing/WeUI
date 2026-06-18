package top.chengdongqing.weui.feature.samples.data.model

import org.jetbrains.compose.resources.DrawableResource

data class FileItem(
    val node: FileNode,
    val name: String,
    val size: String?,
    val mimeType: String,
    val isDirectory: Boolean,
    val isVisualMedia: Boolean,
    val lastModified: String?,
    val childrenCount: Int,
    val iconRes: DrawableResource,
    val isReadable: Boolean?,
    val isWriteable: Boolean?,
    val isHidden: Boolean?,
    var thumbnailUrl: String? = node.id
)