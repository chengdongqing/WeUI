package top.chengdongqing.weui.feature.samples.data.repository

import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.feature.samples.data.model.FileNode

interface FileRepository {
    suspend fun getChildren(fileNode: FileNode): List<FileItem>

    suspend fun open(fileItem: FileItem)

    suspend fun delete(fileNode: FileNode): Boolean
}

expect fun getFileRepository(): FileRepository