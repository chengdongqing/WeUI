package top.chengdongqing.weui.feature.demos.filebrowser.data.repository

import top.chengdongqing.weui.feature.demos.filebrowser.data.model.FileItem

interface FileRepository {
    suspend fun getListFiles(filepath: String): List<FileItem>
}