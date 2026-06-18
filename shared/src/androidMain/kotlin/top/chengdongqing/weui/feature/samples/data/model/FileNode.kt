package top.chengdongqing.weui.feature.samples.data.model

import java.io.File

data class AndroidFileNode(
    val file: File,
    override val id: String,
    override val name: String,
    override val isDirectory: Boolean
) : FileNode(id, name, isDirectory)