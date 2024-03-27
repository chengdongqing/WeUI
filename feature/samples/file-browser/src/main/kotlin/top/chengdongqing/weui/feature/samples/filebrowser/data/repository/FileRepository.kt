package top.chengdongqing.weui.feature.samples.filebrowser.data.repository

import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem

interface FileRepository {
    suspend fun getFileList(filepath: String): List<FileItem>
}