package top.chengdongqing.weui.feature.samples.ui.filebrowser.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import top.chengdongqing.weui.feature.samples.data.repository.getFileRepository

class FileListViewModel(fileNode: FileNode) : ViewModel() {
    private val repository = getFileRepository()

    private val _uiState = MutableStateFlow(FileBrowserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFileList(fileNode)
        }
    }

    suspend fun getFileList(fileNode: FileNode) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        runCatching {
            val fileList = repository.getChildren(fileNode)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    fileList = fileList
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    suspend fun openFile(fileItem: FileItem) = repository.open(fileItem)

    suspend fun deleteFile(fileNode: FileNode) = repository.delete(fileNode)
}

data class FileBrowserUiState(
    val isLoading: Boolean = false,
    val fileList: List<FileItem> = emptyList()
)