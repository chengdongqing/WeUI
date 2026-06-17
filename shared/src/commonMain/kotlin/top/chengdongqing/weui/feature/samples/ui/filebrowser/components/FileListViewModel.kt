package top.chengdongqing.weui.feature.samples.ui.filebrowser.components

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.feature.samples.data.repository.getFileRepository

class FileListViewModel : ViewModel() {
    private val repository = getFileRepository()

    private val _uiState = MutableStateFlow(FileBrowserUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun getFileList(filePath: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        val fileList = repository.getFileList(filePath)

        _uiState.update {
            it.copy(
                isLoading = false,
                fileList = fileList
            )
        }
    }

    suspend fun openFile(
        filePath: String,
        mimeType: String,
        showChooser: Boolean = true
    ) = repository.openFile(filePath, mimeType, showChooser)

    suspend fun deleteFile(filePath: String) = repository.deleteFile(filePath)
}

data class FileBrowserUiState(
    val isLoading: Boolean = false,
    val fileList: List<FileItem> = emptyList()
)