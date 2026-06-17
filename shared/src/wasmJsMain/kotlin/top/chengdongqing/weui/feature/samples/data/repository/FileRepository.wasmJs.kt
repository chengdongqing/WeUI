package top.chengdongqing.weui.feature.samples.data.repository

import top.chengdongqing.weui.feature.samples.data.model.FileItem

class WasmFileRepository : FileRepository {

    override suspend fun getFileList(filepath: String): List<FileItem> {
        val list = mutableListOf<FileItem>()

        return list
    }

    override suspend fun openFile(filePath: String, mimeType: String, showChooser: Boolean) {
    }

    override suspend fun deleteFile(filePath: String): Boolean {
        return true
    }
}

actual fun getFileRepository(): FileRepository = WasmFileRepository()