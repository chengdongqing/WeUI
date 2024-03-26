package top.chengdongqing.weui.feature.samples.filebrowser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem
import top.chengdongqing.weui.feature.samples.filebrowser.data.repository.FileRepositoryImpl

class FileViewModel : ViewModel() {
    private val fileRepository by lazy { FileRepositoryImpl() }

    var fileList by mutableStateOf<List<FileItem>>(emptyList())
    var loading by mutableStateOf(true)

    fun refresh(filePath: String) {
        viewModelScope.launch {
            loading = true
            fileList = fileRepository.getListFiles(filePath)
            loading = false
        }
    }
}