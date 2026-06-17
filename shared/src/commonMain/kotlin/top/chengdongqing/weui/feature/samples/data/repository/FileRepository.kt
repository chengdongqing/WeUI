package top.chengdongqing.weui.feature.samples.data.repository

import top.chengdongqing.weui.feature.samples.data.model.FileItem

interface FileRepository {
    suspend fun getFileList(filepath: String): List<FileItem>

    suspend fun openFile(
        filePath: String,
        mimeType: String,
        showChooser: Boolean = true
    )

    suspend fun deleteFile(filePath: String): Boolean
}

expect fun getFileRepository(): FileRepository