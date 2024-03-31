package top.chengdongqing.weui.feature.samples.filebrowser.filelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import top.chengdongqing.weui.core.data.model.Result
import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem
import top.chengdongqing.weui.feature.samples.filebrowser.data.repository.FileRepositoryImpl

class FileListViewModel : ViewModel() {
    private val fileRepository = FileRepositoryImpl()

    var fileListResult by mutableStateOf<Result<List<FileItem>>>(Result.Loading)
        private set

    suspend fun refresh(filePath: String) {
        fileListResult = Result.Success(fileRepository.getFileList(filePath))
    }
}